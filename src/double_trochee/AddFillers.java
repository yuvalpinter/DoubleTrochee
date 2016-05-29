package double_trochee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import double_trochee.Syllable.Stress;

public class AddFillers {

    public static void main(String[] args) throws IOException {
        PronunciationDictionary pronDict = new PronunciationDictionary("data/cmudict-0.7b.phones", "data/cmudict-0.7b");
        Pronunciation currPron = new Pronunciation();
        Scanner scanner = new Scanner(System.in);
        String currWord;
        while (!(currWord = scanner.next()).equalsIgnoreCase("QUIT")) {
            Pronunciation pron = pronDict.get(currWord);
            if (pron != null) {
                currPron.concat(pron);
            } else {
                currPron = new Pronunciation(); // no guesses
            }
            fillIfNeeded(currPron);
            continue;
        }
        scanner.close();
    }

    public static void fillIfNeeded(Pronunciation pron) {
        List<Syllable> syls = pron.getSyllables();
        if (syls.size() < 4) {
            return;
        }
        List<Syllable> revSyls = new ArrayList<>(syls);
        Collections.reverse(revSyls);
        if (revSyls.get(0).getStress() == Stress.NONE && revSyls.get(1).getStress() != Stress.NONE
                        && revSyls.get(2).getStress() == Stress.NONE && revSyls.get(3).getStress() != Stress.NONE) {
            System.out.print(" " + Consts.FILLER + " ");
            pron.clean();
        }
    }

}
