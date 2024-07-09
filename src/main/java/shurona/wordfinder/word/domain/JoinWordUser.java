package shurona.wordfinder.word.domain;


import jakarta.persistence.*;
import shurona.wordfinder.common.DateInfoEntity;
import shurona.wordfinder.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name="join_word_user", uniqueConstraints = {
        @UniqueConstraint(
                name = "word_owned_unique",
                columnNames = {"WORD_ID", "USER_ID"}
        )})
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

    private Boolean visible;

    private LocalDateTime lastSelectedQuiz;

    public JoinWordUser() {
        //
    }

    public JoinWordUser(User user, Word word) {
        this.user= user;
        this.word = word;

        this.visible = true;
        this.lastSelectedQuiz = LocalDateTime.now();

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

    public Boolean getVisible() {
        return visible;
    }

    public LocalDateTime getLastSelectedQuiz() {
        return lastSelectedQuiz;
    }

    /*
        도메인 로직
     */

    /**
     * 퀴즈에 선택된 경우 날짜를 오늘로 업데이트 해준다.
     */
    public void updateDataWhenCreateQuiz() {
        this.lastSelectedQuiz = LocalDateTime.now();
    }

    /**
     * 단어 visible false 처리
     */
    public void hideWordVisible() {
        this.visible = false;
    }

    @Override
    public String toString() {
        return "JoinWordUser{" +
                "id=" + uid +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
