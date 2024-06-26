package shurona.wordfinder.word.repository.word;

import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.repository.word.repodto.RandWordMeaningDto;

import java.util.*;

//@Repository
public class MemoryWordRepository implements WordRepository {
        private static final Map<String, Word> store = new HashMap<>();

    @Override
    public String save(Word word) {
        String wordId = UUID.randomUUID().toString();
        word.setUid(wordId);
        store.put(wordId, word);
        return store.get(wordId).getUid();
    }

    @Override
    public Word findWordByWord(String word) {

        Set<String> uuids = store.keySet();

        Optional<String> output = uuids.stream().filter(id -> Objects.equals(store.get(id).getWord(), word)).findFirst();
        return store.get(output.orElse(null));
    }

    @Override
    public Optional<Word> findWordById(String id) {
        return store.values().stream().filter(word -> word.getUid().equals(id)).findAny();
    }

    @Override
    public Word[] findWordsByIds(String[] ids) {
        Word[] words = new Word[ids.length];
        // ids 속하는 단어 목록을 갖고 온다.
        for (int i = 0; i < words.length; i++) {
            words[i] = store.get(ids[i]);
        }
        return words;
    }

    @Override
    public void editMeaning(String id, String meaning) {
        Word word = findWordById(id).get();
        word.editMeaning(meaning);
    }

    public void clearStore() {
        store.clear();
    }

    /*
     여기서부터는 메모리 미구현
     */

    @Override
    public List<RandWordMeaningDto> findRandomWordMeaning(String exceptWordId) {
        return new ArrayList<>();
    }
}
