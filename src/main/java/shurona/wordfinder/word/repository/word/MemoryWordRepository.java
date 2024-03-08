package shurona.wordfinder.word.repository.word;

import shurona.wordfinder.word.Word;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryWordRepository implements WordRepository {
    private static final Map<UUID, Word> store = new HashMap<>();

    @Override
    public Word save(Word word) {
        UUID wordId = UUID.randomUUID();
        word.setUid(wordId);
        store.put(wordId, word);
        return store.get(wordId);
    }

    @Override
    public Word[] findWordsbyIds(UUID[] ids) {
        Word[] words = new Word[ids.length];
        // ids 속하는 단어 목록을 갖고 온다.
        for (int i = 0; i < words.length; i++) {
            words[i] = store.get(ids[i]);
        }
        return words;
    }
}
