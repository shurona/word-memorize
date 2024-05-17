package shurona.wordfinder.user;

public class User {
    private Long id;

    private String nickname;

    private String loginId;

    private String password;

    public User() {
    }

    /**
     *
     * @param nickname
     * @param loginId
     * @param password
     */
    public User(String nickname, String loginId, String password) {
        this.nickname = nickname;
        this.loginId = loginId;

        // TODO: 해쉬로 저장해야 한다.
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", loginId='" + loginId + '\'' +
                '}';
    }
}
