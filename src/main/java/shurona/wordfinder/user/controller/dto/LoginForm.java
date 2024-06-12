package shurona.wordfinder.user.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LoginForm {

    @NotEmpty
    @Size(max = 10, message = "최대 10글자까지 입력가능합니다.")
    private String loginId;
    @NotEmpty
    @Size(max = 10, message = "최대 20글자까지 입력가능합니다.")
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
