package shurona.wordfinder.user.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public class UserForm {
    @NotEmpty
    private String nickname;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

    public UserForm() {
    }

    public UserForm(String nickname, String loginId, String password) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "nickname='" + nickname + '\'' +
                ", loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
