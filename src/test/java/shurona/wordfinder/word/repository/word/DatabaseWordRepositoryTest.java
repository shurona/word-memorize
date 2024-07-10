package shurona.wordfinder.word.repository.word;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.DatabaseUserRepository;
import shurona.wordfinder.user.repository.UserRepository;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.repository.joinuserword.DatabaseJoinWordRepository;
import shurona.wordfinder.word.repository.word.repodto.RandWordMeaningDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DatabaseWordRepositoryTest {

    @Autowired
    private DatabaseWordRepository wordRepository;
    @Autowired
    private DatabaseUserRepository userRepository;
    @Autowired
    private DatabaseJoinWordRepository joinWordRepository;

    @Test
    public void 단어저장_조회() {
        //given
        String wordInfo = "hello";
        String meaning = "안녕";

        //when
        Word newWord = new Word(wordInfo, meaning);
        String savedWordId= this.wordRepository.save(newWord);
        Word saved = this.wordRepository.findWordById(savedWordId).get();

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
        this.wordRepository.save(newWord);

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
        String savedWordId = this.wordRepository.save(newWord);
        Word saved = this.wordRepository.findWordById(savedWordId).get();
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
            String savedId = this.wordRepository.save(new Word(wordInfo + " " + i, meaning));
            ids[i] = savedId;
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
        String nowId = this.wordRepository.save(new Word(wordInfo, meaning));
        Word now = this.wordRepository.findWordById(nowId).get();
        // when
        this.wordRepository.editMeaning(now.getUid(), changeMeaning);

        // then
        Word newMeaningWord = this.wordRepository.findWordByWord(wordInfo);
        assertThat(newMeaningWord.getMeaning()).isEqualTo(changeMeaning);
    }

    @Test
    public void 랜덤단어_선택() {
        // given
        Long userOneId = this.userRepository.save(new User("nicknameOne", "loginId", "password"));

        User userOne = this.userRepository.findById(userOneId);

        String wordString = "wd";
        String wordMeaning = "meaning";

        List<String> wordIdList = new ArrayList<>();
        List<String> jwuIdList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            String wordId = this.wordRepository.save(new Word(wordString + i, wordMeaning + " " + i));
            Word word = this.wordRepository.findWordById(wordId).get();
            String jwuId = this.joinWordRepository.saveUserWord(userOne, word);
            wordIdList.add(wordId);
            jwuIdList.add(jwuId);
        }

        for (int i = 0; i < 10; i++) {
            JoinWordUser jwuInfo = this.joinWordRepository.findById(jwuIdList.get(i));
            jwuInfo.hideWordVisible();
        }
        // when
        List<RandWordMeaningDto> randomWordMeaning = this.wordRepository.findRandomWordMeaning(wordIdList.get(0));

        // then
        assertThat(randomWordMeaning.size()).isEqualTo(3);
        for (RandWordMeaningDto randWordMeaningDto : randomWordMeaning) {
            String s = randWordMeaningDto.getMeaning().split(" ")[1];
            assertThat(Integer.parseInt(s)).isGreaterThan(9);
        }
    }

}