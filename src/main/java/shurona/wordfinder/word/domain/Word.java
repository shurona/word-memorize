package shurona.wordfinder.word.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import shurona.wordfinder.common.DateInfoEntity;

import java.time.LocalDateTime;

@Entity
public class Word extends DateInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "WORD_ID")
    private String uid;

    @Column(unique = true)
    private String word;

    private String meaning;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'COMPLETE'")
    private WordEditStatus status;

    public Word() {

    }

    public Word(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;

        // 날짜 생성
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public WordEditStatus getStatus() {return status;}


    public void editMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void editWordStatus(WordEditStatus status) {
        this.status = status;
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
