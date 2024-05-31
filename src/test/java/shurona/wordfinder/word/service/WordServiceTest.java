package shurona.wordfinder.word.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.repository.word.MemoryWordRepository;
import shurona.wordfinder.word.repository.word.WordRepository;

import static org.assertj.core.api.Assertions.assertThat;

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
        String meaning = "야후";
        //when
        Word word = wordService.saveWord(newWord, meaning);
        //then
        assertThat(word.getWord()).isEqualTo(newWord);
    }

    @Test
    void getWordByWordInfoTest() {
    }

    @Test
    void getWordById() {
        this.wordService.saveWord("hello", "안녕");

        Word word1 = this.wordService.getWordByWordInfo("hello");
        Word word2 = this.wordService.getWordById(word1.getUid());
        this.wordService.getWordById("dkdkdkdk");

        assertThat(word1.getWord()).isEqualTo(word2.getWord());
        assertThat(word1.getUid()).isEqualTo(word2.getUid());
    }

    @Test
    void 단어_의미_수정() {
        //given
        Word word = this.wordService.saveWord("hello", "안녕");
        assertThat(word.getMeaning()).isEqualTo("안녕");

        //when
        wordService.updateMeaning(word.getUid(), "안녕하세요");

        // then
        Word newWord = wordService.getWordById(word.getUid());

        assertThat(word.getMeaning()).isEqualTo("안녕하세요");

    }
}