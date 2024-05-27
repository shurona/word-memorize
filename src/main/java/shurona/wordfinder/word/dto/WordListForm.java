package shurona.wordfinder.word.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public class WordListForm {
    private String wordId;
    private String word;
    private String meaning;
    private Long userId;

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
