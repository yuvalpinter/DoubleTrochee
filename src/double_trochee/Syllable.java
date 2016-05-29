package double_trochee;

import java.util.List;

import double_trochee.Syllable.Stress;

public class Syllable {

    public enum Stress {

        NONE, PRIMARY, SECONDARY;

        public static Stress parse(int stressInt) {
            switch (stressInt) {
                case 0:
                    return NONE;
                case 1:
                    return PRIMARY;
                case 2:
                    return SECONDARY;
            }
            return null;
        }

    }

    private Stress stress;

    private List<Phoneme> phonemes;

    public Syllable(Stress stress, List<Phoneme> phonemes) {
        this.stress = stress;
        this.phonemes = phonemes;
    }
    
    public Stress getStress() {
        return stress;
    }

    public List<Phoneme> getPhonemes() {
        return phonemes;
    }

    public void setStress(Stress newStress) {
        stress = newStress;
    }

}
