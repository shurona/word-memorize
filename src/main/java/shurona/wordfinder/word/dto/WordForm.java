package shurona.wordfinder.word.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WordForm {
    @NotEmpty
    @Size(max = 20, message = "최대 20글자까지 입력가능합니다.")
    private String nickname;
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "영어로 입력해주세요")
    @Size(max = 20, message = "최대 20글자까지 입력가능합니다.")
    private String word;
    @NotEmpty
    @Size(max = 20, message = "최대 20글자까지 입력가능합니다.")
    private String meaning;

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

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning.strip();
    }

    public Integer getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(Integer remainCount) {
        this.remainCount = remainCount;
    }

    @Override
    public String toString() {
        return "WordForm{" +
                "nickname=" + nickname +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                '}';
    }
}
