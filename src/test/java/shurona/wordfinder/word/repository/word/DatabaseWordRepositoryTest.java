package shurona.wordfinder.word.repository.word;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.word.domain.Word;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DatabaseWordRepositoryTest {

    @Autowired
    private DatabaseWordRepository wordRepository;

    @Test
    public void 단어저장_조회() {
        //given
        String wordInfo = "hello";
        String meaning = "안녕";

        //when
        Word newWord = new Word(wordInfo, meaning);
        Word saved = this.wordRepository.save(newWord);

        //then
        assertThat(saved.getUid()).isNotEqualTo(null);
        assertThat(saved.getWord()).isEqualTo(wordInfo);
        assertThat(saved.getMeaning()).isEqualTo(meaning);
    }

    @Test
    public void 단어로_조회() {
        //given
        String wordInfo = "hello";
        String meaning = "안녕";
        Word newWord = new Word(wordInfo, meaning);
        Word saved = this.wordRepository.save(newWord);

        //when
        Word existWord = this.wordRepository.findWordByWord(wordInfo);
        Word nonExistWord = this.wordRepository.findWordByWord(wordInfo + "d");

        //then
        assertThat(existWord.getMeaning()).isEqualTo(meaning);
        assertThat(nonExistWord).isEqualTo(null);

    }

    @Test
    public void 단어_아이디로_조회() {
        // given
        String wordInfo = "hello";
        String meaning = "안녕";
        Word newWord = new Word(wordInfo, meaning);
        Word saved = this.wordRepository.save(newWord);
        // when

        Word wordById = this.wordRepository.findWordById(saved.getUid()).orElse(null);
        Word wrongId = this.wordRepository.findWordById(saved.getUid() + "dd").orElse(null);

        // then
        assertThat(wordById.getWord()).isEqualTo(wordInfo);
        assertThat(wrongId).isEqualTo(null);
    }

    @Test
    public void 단어아이디들로_조회() {
        // given
        String wordInfo = "hello";
        String meaning = "안녕";
        String[] ids = new String[10];
        for (int i = 0; i < 10; i++) {
            Word save = this.wordRepository.save(new Word(wordInfo + " " + i, meaning));
            ids[i] = save.getUid();
        }
        // when
        Word[] wordsByIds = this.wordRepository.findWordsByIds(ids);
        Word[] oneIds = this.wordRepository.findWordsByIds(new String[]{ids[0]});

        // then
        assertThat(wordsByIds.length).isEqualTo(10);
        assertThat(oneIds.length).isEqualTo(1);
    }

    @Test
    public void 단어_수정() {
        // given
        String wordInfo = "hello";
        String meaning = "안녕";
        String changeMeaning = "안녕하세요";
        Word now = this.wordRepository.save(new Word(wordInfo, meaning));
        // when
        this.wordRepository.editMeaning(now.getUid(), changeMeaning);

        // then
        Word newMeaningWord = this.wordRepository.findWordByWord(wordInfo);
        assertThat(newMeaningWord.getMeaning()).isEqualTo(changeMeaning);
    }

}