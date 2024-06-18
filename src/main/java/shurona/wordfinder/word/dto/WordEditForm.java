package shurona.wordfinder.word.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WordEditForm {

    @NotNull
    @Size(max = 20, message = "최대 20글자까지 입력가능합니다.")
    private String wordId;
    @NotNull
    @Size(max = 20, message = "최대 20글자까지 입력가능합니다.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "입력값이 잘못 되었습니다.")
    private String word;
    @NotNull
    @Size(max = 20, message = "최대 20글자까지 입력가능합니다.")
    private String meaning;

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

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }
}
