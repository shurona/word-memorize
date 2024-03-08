package shurona.wordfinder.word;

import java.util.UUID;

public class JoinWordUser {
    private UUID id;

    private UUID wordId;
    private Long userId;

    // 등록된 시간
    // 추후 DB 연결로 업데이트
    private String createdAt;

    private String recentCheckedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWordId() {
        return wordId;
    }

    public void setWordId(UUID wordId) {
        this.wordId = wordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRecentCheckedAt() {
        return recentCheckedAt;
    }

    public void setRecentCheckedAt(String recentCheckedAt) {
        this.recentCheckedAt = recentCheckedAt;
    }

    @Override
    public String toString() {
        return "JoinWordUser{" +
                "id=" + id +
                ", wordId=" + wordId +
                ", userId=" + userId +
                ", createdAt='" + createdAt + '\'' +
                ", recentCheckedAt='" + recentCheckedAt + '\'' +
                '}';
    }
}
