package shurona.wordfinder.word.repository.word.repodto;

public class RandWordMeaningDto {
    private String wordId;
    private String meaning;

    public RandWordMeaningDto(String wordId, String meaning) {
        this.wordId = wordId;
        this.meaning = meaning;
    }

    public String getWordId() {
        return wordId;
    }

    public String getMeaning() {
        return meaning;
    }

    /*
     테스트 출력용
     */
    @Override
    public String toString() {
        return "RandWordMeaningDto{" +
                "wordId='" + wordId + '\'' +
                ", meaning='" + meaning + '\'' +
                '}';
    }
}
