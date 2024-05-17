package shurona.wordfinder.user.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public class UserForm {
    @NotEmpty
    private String nickname;

    private String loginId;
    private String password;


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

    //    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }
}
