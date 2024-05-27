package shurona.wordfinder.word.dto;

import jakarta.validation.constraints.NotEmpty;

public class WordForm {
    @NotEmpty
    private String nickname;
    @NotEmpty
    private String word;
    @NotEmpty
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
