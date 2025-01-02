package shurona.wordfinder.word.dto;

import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.domain.WordEditStatus;

public record WordListForm(
    String wordId,
    String word,

    String meaning,
    WordEditStatus status,
    Long userId
) {

    public static WordListForm from(Word word, Long userId) {
        return new WordListForm(word.getUid(), word.getWord(),
            word.getMeaning(), word.getStatus(), userId);
    }
}
