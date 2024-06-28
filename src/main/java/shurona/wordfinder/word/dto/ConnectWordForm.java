package shurona.wordfinder.word.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 뜻 없이 단어만 입력받는 Form
 */
public class ConnectWordForm {
    @NotEmpty
    private String nickname;
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "영어로 입력해주세요.")
    @Size(max = 20)
    private String word;

    private Integer remainCount;

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

    public Integer getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(Integer remainCount) {
        this.remainCount = remainCount;
    }
}
