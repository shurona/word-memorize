package shurona.wordfinder.user.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public class LoginForm {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;

    public LoginForm(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginForm{" +
                "loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
