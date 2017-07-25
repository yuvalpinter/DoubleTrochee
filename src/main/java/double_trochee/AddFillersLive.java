/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of Oracle or the names of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package double_trochee;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

import double_trochee.Syllable.Stress;

public class AddFillersLive extends JPanel implements KeyListener {
    
    private static final long serialVersionUID = 6259283923518532405L;
    
    protected JTextField textField;
    protected JTextArea textArea;
    protected JTextArea console;

    PronunciationDictionary pronDict;
    String lastInterruptedWord = null;

    public AddFillersLive() throws IOException {
        super(new GridBagLayout());

        pronDict = new PronunciationDictionary("data/cmudict-0.7b.phones", "data/cmudict-0.7b");

        textField = new JTextField(60);
        textField.addKeyListener(this);

        textArea = new JTextArea(5, 60);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        console = new JTextArea(1, 60);
        console.setEditable(false);

        // Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
        
        add(console, c);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (Character.isWhitespace(e.getKeyChar())) {
            Pronunciation currPron = new Pronunciation();

            String text = textField.getText();
            if (lastInterruptedWord != null) {
                int lastFiller = text.lastIndexOf(lastInterruptedWord);
                if (lastFiller > 0) {
                    text = text.substring(lastFiller + lastInterruptedWord.length() + 1);
                }
            }
            String lastWord = "";
            for (String w : text.split("\\s+")) {
                Pronunciation p = pronDict.get(w);
                if (p == null) {
                    currPron = new Pronunciation(); // no chances
                } else {
                    currPron.concat(p);
                }
                lastWord = w;
            }
            textArea.append(" " + lastWord);
            console.setText(syllString(currPron));
            if (needsFill(currPron)) {
                textArea.append(Consts.FILLER);
                lastInterruptedWord = lastWord;
            }
        }
    }

    private String syllString(Pronunciation currPron) {
		StringBuilder sb = new StringBuilder();
		List<Syllable> syls = currPron.getSyllables();
		for (Syllable syl : syls) {
			sb.append(syl.getStress().intVal());
		}
		return sb.toString();
	}

	private boolean needsFill(Pronunciation pron) {
        List<Syllable> syls = pron.getSyllables();
        if (syls.size() < 4) {
            return false;
        }
        List<Syllable> revSyls = new ArrayList<>(syls);
        Collections.reverse(revSyls);
        if (revSyls.get(0).getStress() == Stress.NONE && revSyls.get(1).getStress() != Stress.NONE
                        && revSyls.get(2).getStress() == Stress.NONE && revSyls.get(3).getStress() != Stress.NONE) {
            pron.clean();
            return true;
        }
        return false;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be invoked from the event dispatch thread.
     * 
     * @throws IOException
     */
    private static void createAndShowGUI() throws IOException {
        // Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add contents to the window.
        frame.add(new AddFillersLive());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
