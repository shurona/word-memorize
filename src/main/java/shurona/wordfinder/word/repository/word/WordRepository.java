package shurona.wordfinder.word.repository.word;

import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.repository.word.repodto.RandWordMeaningDto;

import java.util.List;
import java.util.Optional;

public interface WordRepository {
    String save(Word word);

    Word findWordByWord(String word);

    List<RandWordMeaningDto> findRandomWordMeaning(String exceptWordId);

    Optional<Word> findWordById(String id);

    Word[] findWordsByIds(String[] ids);

    void editMeaning(String id, String meaning);


}
