package shurona.wordfinder.word.repository.word;

import shurona.wordfinder.word.Word;

import java.util.UUID;

public interface WordRepository {
    Word save(Word word);

    Word findWordByWord(String word);

    Word[] findWordsByIds(UUID[] ids);
}
