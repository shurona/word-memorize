package shurona.wordfinder.word.dto;

import jakarta.validation.constraints.NotEmpty;

public class ConnectWordForm {
    @NotEmpty
    private String nickname;
    @NotEmpty
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
        this.word = word;
    }
}
