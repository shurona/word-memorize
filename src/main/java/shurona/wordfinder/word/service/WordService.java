package shurona.wordfinder.word.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shurona.wordfinder.word.Word;
import shurona.wordfinder.word.repository.word.WordRepository;

import java.util.UUID;

@Service
public class WordService {
    private final WordRepository wordRepository;

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public Word saveWord(String wordInfo, String wordMeaning) {
        // 단어 정보 저장
        Word newWord = new Word();
        newWord.setWord(wordInfo);
        newWord.setMeaning(wordMeaning);
        return this.wordRepository.save(newWord);
    }

    // 단어 정보 갖고 오기
    public Word getWordByWordInfo(String wordInfo) {
        return this.wordRepository.findWordByWord(wordInfo);
    }


    public Word[] getWordByIds(UUID[] wordIds) {
        return this.wordRepository.findWordsByIds(wordIds);
    }
    //
}