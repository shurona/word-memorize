package shurona.wordfinder.word.repository.word;

import shurona.wordfinder.word.domain.Word;

import java.util.Optional;

public interface WordRepository {
    Word save(Word word);

    Word findWordByWord(String word);

    Optional<Word> findWordById(String id);

    Word[] findWordsByIds(String[] ids);

    void editMeaning(String id, String meaning);
}
