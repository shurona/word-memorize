package shurona.wordfinder.word;

import java.util.UUID;

public class Word {
    private String uid;

    private String word;

    private String meaning;

    public Word() {

    }

    public Word(String uid, String word, String meaning) {
        this.uid = uid;
        this.word = word;
        this.meaning = meaning;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

}
