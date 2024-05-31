package shurona.wordfinder.word.domain;

import jakarta.persistence.*;
import shurona.wordfinder.common.DateInfoEntity;

@Entity
public class Word extends DateInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "WORD_ID")
    private String uid;

    @Column(unique = true)
    private String word;

    private String meaning;

    public Word() {

    }

    public Word(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void editMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return "Word{" +
                "uid='" + uid + '\'' +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                '}';
    }
}
