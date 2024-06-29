package shurona.wordfinder.word.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.domain.WordEditStatus;
import shurona.wordfinder.word.repository.word.WordRepository;
import shurona.wordfinder.word.repository.word.repodto.RandWordMeaningDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
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

    public List<RandWordMeaningDto> findRandomWordMeaning(String wordId) {
        return this.wordRepository.findRandomWordMeaning(wordId);
    }


    /**
     * save
     */
    @Transactional
    public Word saveWord(String wordInfo, String wordMeaning, WordEditStatus editStatus) {
        // 단어 정보 저장
        Word newWord = new Word(wordInfo, wordMeaning);
        newWord.editWordStatus(editStatus);
        String wordId = this.wordRepository.save(newWord);

        Optional<Word> word = this.wordRepository.findWordById(wordId);
        boolean present = word.isPresent();
        if (!present) {
            throw new NoSuchElementException("Internal Server error");
        }
        return word.get();
    }


    /**
     * 의미 수정
     */
    @Transactional
    public void updateMeaning(String wordId, String meaning) {
        this.wordRepository.editMeaning(wordId, meaning);
    }
}
