package shurona.wordfinder.quiz.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.quiz.domain.QuizDetail;
import shurona.wordfinder.quiz.domain.QuizSet;
import shurona.wordfinder.quiz.repository.DatabaseQuizRepository;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.DatabaseUserRepository;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.repository.joinuserword.DatabaseJoinWordRepository;
import shurona.wordfinder.word.repository.word.DatabaseWordRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class QuizServiceTest {

    @Autowired
    QuizService quizService;

    @Autowired
    DatabaseQuizRepository quizRepository ;

    @Autowired
    DatabaseUserRepository userRepository;
    @Autowired
    DatabaseWordRepository wordRepository;

    @Autowired
    DatabaseJoinWordRepository joinWordRepository;

    @Test
    public void 퀴즈셋_저장() {
        // given
        User user = new User("nickname", "loginId", "password");
        this.userRepository.save(user);

        // 단어와 word_user 저장
        for (int i = 0; i < 20; i++) {
            Word word = new Word("word" + i, "meaning");
            this.wordRepository.save(word);
            joinWordRepository.saveUserWord(user, word);
        }


        // when
//        QuizSet quizSetById = this.databaseQuizRepository.findByQuizId(after.getId());
        Long quizSetId = this.quizService.generateQuizSet(user.getId());
        QuizSet quizInfo = this.quizService.getQuizInfo(quizSetId);

        // then
        assertThat(quizInfo.getCurrentSequence()).isEqualTo(0);
        assertThat(quizInfo.getQuizDetails().size()).isEqualTo(10);
        assertThat(quizInfo.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void 페이징_테스트() {
        // given
        User user = new User("nickname", "loginId", "password");
        Word word = new Word("word", "meaning");
        this.userRepository.save(user);
        this.wordRepository.save(word);
        String idInfo = joinWordRepository.saveUserWord(user, word);
        JoinWordUser joinWordUser = joinWordRepository.findById(idInfo);
        QuizDetail quizDetail = QuizDetail.createQuizDetail(0, 0, joinWordUser, "f", "s");
        for (int i = 0; i < 20; i++) {
            QuizSet quizSet = QuizSet.createQuizSet(user, new QuizDetail[]{quizDetail});
            this.quizRepository.saveQuizSet(quizSet);
        }

        // when
        List<QuizSet> quizSetList = this.quizService.getQuizSetList(0, 3, user.getId());
        List<QuizSet> quizSetListTwo = this.quizService.getQuizSetList(1, 3, user.getId());
        List<QuizSet> nonList = this.quizService.getQuizSetList(1, 3, 40L);

        // then
        assertThat(quizSetList.size()).isEqualTo(3);
        assertThat(quizSetListTwo.size()).isEqualTo(3);
        assertThat(nonList.size()).isEqualTo(0);

        // id는 앞이 더 커야 한다.
        assertThat(quizSetList.get(0).getId()).isGreaterThan(quizSetListTwo.get(0).getId());
    }

    @Test
    void 퀴즈생성여부_확인() {
        // given
        User user = new User("nickname", "loginId", "password");
        Word word = new Word("word", "meaning");
        this.userRepository.save(user);
        this.wordRepository.save(word);
        String joinWordUserId = joinWordRepository.saveUserWord(user, word);
        JoinWordUser joinWordUser = joinWordRepository.findById(joinWordUserId);
        QuizDetail quizDetail = QuizDetail.createQuizDetail(0, 0, joinWordUser, "f", "s");
        for (int i = 0; i < 20; i++) {
            QuizSet quizSet = QuizSet.createQuizSet(user, new QuizDetail[]{quizDetail});
            this.quizRepository.saveQuizSet(quizSet);
        }

        // when
        boolean existGen = this.quizService.checkRecentGenerateQuizSet(user.getId());
        boolean firstGen = this.quizService.checkRecentGenerateQuizSet(200L);

        // then
        assertThat(firstGen).isEqualTo(true);
        assertThat(existGen).isEqualTo(false);
    }
}