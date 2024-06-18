package shurona.wordfinder.word.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ConnectWordForm {
    @NotEmpty
    private String nickname;
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "영어로 입력해주세요.")
    @Size(max = 20)
    private String word;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word.strip();
    }
}
