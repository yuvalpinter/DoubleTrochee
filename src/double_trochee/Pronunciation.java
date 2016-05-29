package double_trochee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import double_trochee.Syllable.Stress;

public class Pronunciation {

    private List<Syllable> syllables = new ArrayList<>();

    public Pronunciation(List<Syllable> syllables) {
        this.syllables = syllables;
    }

    public Pronunciation() {}

    public Pronunciation(String pronString, Set<String> vowels) {
        // first impl: ignore consonants
        for (String phone : pronString.split("\\s+")) {
            if (phone.length() > 2 && vowels.contains(phone.substring(0, 2))) {
                int stressInt = Integer.parseInt(phone.substring(2));
                Stress stress = Stress.parse(stressInt);
                syllables.add(new Syllable(stress, Collections.singletonList(Phoneme.VOWEL)));
            }
        }
        fixStress();
    }

    private void fixStress() {
        for (int i = 0; i < syllables.size() - 1; i++) {
            if (syllables.get(i).getStress() == Stress.SECONDARY
                            && syllables.get(i + 1).getStress() == Stress.PRIMARY) {
                syllables.get(i).setStress(Stress.NONE);
            }
        }
    }

    public void concat(Pronunciation other) {
        syllables.addAll(other.getSyllables());
    }

    public List<Syllable> getSyllables() {
        return syllables;
    }

    public void clean() {
        syllables = new ArrayList<>();
    }

}
