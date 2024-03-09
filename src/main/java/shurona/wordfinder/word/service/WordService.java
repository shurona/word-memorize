package shurona.wordfinder.word.service;

import org.springframework.beans.factory.annotation.Autowired;
import shurona.wordfinder.word.Word;
import shurona.wordfinder.word.repository.word.WordRepository;

public class WordService {
    private final WordRepository wordRepository;

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public Word saveWord(String wordInfo) {
        // 단어 정보 저장
        Word newWord = new Word();
        newWord.setWord(wordInfo);
        return this.wordRepository.save(newWord);
    }

    // 단어 정보 갖고 오기
    public Word getWordByWordInfo(String wordInfo) {
        return this.wordRepository.findWordByWord(wordInfo);
    }

    //
}
