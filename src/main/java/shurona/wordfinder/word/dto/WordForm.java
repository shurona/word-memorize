package shurona.wordfinder.word.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class WordForm {
    @NotEmpty
    @Size(max = 20, message = "최대 20글자까지 입력가능합니다.")
    private String nickname;
    @NotEmpty
    @Size(max = 20, message = "최대 20글자까지 입력가능합니다.")
    private String word;
    @NotEmpty
    @Size(max = 20, message = "최대 20글자까지 입력가능합니다.")
    private String meaning;

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

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
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
