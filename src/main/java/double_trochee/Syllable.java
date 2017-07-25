package double_trochee;

import java.util.List;

public class Syllable {

    public enum Stress {

        NONE(0), PRIMARY(1), SECONDARY(2);
    	
    	private int intVal;

		Stress(int intVal) {
    		this.intVal = intVal;
    	}
		
		public int intVal() {
			return this.intVal;
		}

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
