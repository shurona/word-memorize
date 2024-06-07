package shurona.wordfinder.quiz.dto;

public class QuizResultForm {
    private String word;
    private String meaning;

    private boolean isCorrect;


    public QuizResultForm(String word, String meaning, boolean isCorrect) {
        this.word = word;
        this.meaning = meaning;
        this.isCorrect = isCorrect;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
