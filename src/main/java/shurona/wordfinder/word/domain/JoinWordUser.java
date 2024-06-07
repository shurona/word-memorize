package shurona.wordfinder.word.domain;


import jakarta.persistence.*;
import shurona.wordfinder.common.DateInfoEntity;
import shurona.wordfinder.user.domain.User;

import java.time.LocalDateTime;

@Entity
public class JoinWordUser extends DateInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "WORD_USER_ID")
    private String uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORD_ID")
    private Word word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;


    public JoinWordUser() {
        //
    }

    public JoinWordUser(User user, Word word) {
        this.user= user;
        this.word = word;

        // 날짜 지정
        this.createdAt = LocalDateTime.now();;
        this.updatedAt = LocalDateTime.now();;
    }

    public String getId() {
        return uid;
    }

    /**
     * 메모리용 때문에 남겨둠 //TODO 삭제 언제 하지
     */
    public void setId(String id) {
        this.uid = id;
    }

    public Word getWord() {
        return this.word;
    }

    public User getUser() {
        return this.user;
    }


    @Override
    public String toString() {
        return "JoinWordUser{" +
                "id=" + uid +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
