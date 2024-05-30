package shurona.wordfinder.word;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class JoinWordUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "WORD_USER_ID")
    private String uid;

    private String wordId;
    private Long userId;

    // 등록된 시간
    // 추후 DB 연결로 업데이트
    private LocalDateTime createdAt;

    public JoinWordUser() {
        //
    }

    public JoinWordUser(Long userId, String wordId, LocalDateTime createdAt) {
        this.wordId = wordId;
        this.userId = userId;
        this.createdAt = createdAt;
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

    public String getWordId() {
        return wordId;
    }

    public Long getUserId() {
        return userId;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "JoinWordUser{" +
                "id=" + uid +
                ", wordId=" + wordId +
                ", userId=" + userId +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
