package shurona.wordfinder.word.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shurona.wordfinder.word.Word;
import shurona.wordfinder.word.repository.word.WordRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class WordService {
    private final WordRepository wordRepository;

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    // 단어 정보 갖고 오기
    public Word getWordByWordInfo(String wordInfo) {
        return this.wordRepository.findWordByWord(wordInfo);
    }

    public Word getWordById(String uuid) {
        Optional<Word> wordById = this.wordRepository.findWordById(uuid);

        return wordById.orElse(null);
    }


    public Word[] getWordByIds(String[] wordIds) {
        return this.wordRepository.findWordsByIds(wordIds);
    }
    //


    /**
     * save
     */
    public Word saveWord(String wordInfo, String wordMeaning) {
        // 단어 정보 저장
        Word newWord = new Word();
        newWord.setWord(wordInfo);
        newWord.setMeaning(wordMeaning);
        return this.wordRepository.save(newWord);
    }


    // Update
    public void updateMeaning(String wordId, String meaning) {
        this.wordRepository.editMeaning(wordId, meaning);
    }
}
