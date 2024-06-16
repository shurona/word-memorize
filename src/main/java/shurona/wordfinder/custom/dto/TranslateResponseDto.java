package shurona.wordfinder.custom.dto;

public class TranslateResponseDto {

    private String word;
    private String meaning;

    public TranslateResponseDto(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }
}
