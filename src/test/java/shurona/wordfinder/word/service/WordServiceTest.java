package shurona.wordfinder.word.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.user.repository.MemoryUserRepository;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.word.Word;
import shurona.wordfinder.word.repository.word.MemoryWordRepository;
import shurona.wordfinder.word.repository.word.WordRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WordServiceTest {

    private WordRepository wordRepository;
    private WordService wordService;

    @BeforeEach
    public void beforeEach() {
        this.wordRepository = new MemoryWordRepository();
        this.wordService = new WordService(this.wordRepository);
    }

    @Test
    void saveWordTest() {
        //given
        String newWord = "Yahoo";
        //when
        Word word = wordService.saveWord(newWord);
        //then
        assertThat(word.getWord()).isEqualTo(newWord);
    }

    @Test
    void getWordByWordInfoTest() {
    }
}