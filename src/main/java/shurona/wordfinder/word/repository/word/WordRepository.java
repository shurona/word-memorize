package shurona.wordfinder.word.repository.word;

import shurona.wordfinder.word.Word;

import java.util.Optional;
import java.util.UUID;

public interface WordRepository {
    Word save(Word word);

    Word findWordByWord(String word);

    Optional<Word> findWordById(String id);

    Word[] findWordsByIds(String[] ids);

    void editMeaning(String id, String meaning);
}
