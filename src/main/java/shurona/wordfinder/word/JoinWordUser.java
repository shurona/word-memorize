package shurona.wordfinder.word;


public class JoinWordUser {
    private String id;

    private String wordId;
    private Long userId;

    // 등록된 시간
    // 추후 DB 연결로 업데이트
    private String createdAt;

    private String recentCheckedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
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
