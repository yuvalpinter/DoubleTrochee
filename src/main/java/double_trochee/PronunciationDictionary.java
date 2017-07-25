package double_trochee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class PronunciationDictionary extends HashMap<String, Pronunciation> {

    private static final long serialVersionUID = 5759004196740539396L;

    private HashMap<String, Pronunciation> baseMap = new HashMap<>();

    public PronunciationDictionary(String phoneFile, String dictFile) throws IOException {
        // read vowels from phoneme dictionary
        BufferedReader phoneReader = new BufferedReader(new FileReader(new File(phoneFile)));
        Set<String> vowels = new HashSet<>();
        phoneReader.lines().forEach(new Consumer<String>() {
            @Override
            public void accept(String t) {
                String[] parts = t.split("\\t");
                if (parts[1].equals("vowel")) {
                    vowels.add(parts[0]);
                }
            }
        });
        phoneReader.close();

        // read pronunciation dictionary, save in map
        BufferedReader dictReader = new BufferedReader(new FileReader(new File(dictFile)));
        dictReader.lines().forEach(new Consumer<String>() {
            @Override
            public void accept(String t) {
                if (!Character.isUpperCase(t.charAt(0))) {
                    return;
                }
                int separator = t.indexOf("  ");
                if (separator < 0) {
                    return;
                }
                String word = t.substring(0, separator);
                String pronString = t.substring(separator + 2);
                baseMap.put(word.toUpperCase(), new Pronunciation(pronString, vowels));
            }
        });
        System.out.println("Assigned " + baseMap.size() + " pronunciations.");
        dictReader.close();
    }

    public Pronunciation get(String word) {
        return baseMap.get(word.toUpperCase());
    }

    public Pronunciation get(String[] words) {
        Pronunciation p = new Pronunciation();
        for (String w : words) {
            p.concat(get(w));
        }
        return p;
    }

}
