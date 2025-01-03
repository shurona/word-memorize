package shurona.wordfinder.quiz.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.quiz.domain.QuizDetail;
import shurona.wordfinder.quiz.domain.QuizSet;
import shurona.wordfinder.quiz.repository.QuizRepository;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.UserRepository;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.repository.word.repodto.RandWordMeaningDto;
import shurona.wordfinder.word.service.JoinWordUserService;
import shurona.wordfinder.word.service.WordService;

@Service
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final JoinWordUserService joinWordUserService;
    private final WordService wordService;

    @Autowired
    QuizService(QuizRepository quizRepository, UserRepository userRepository,
        JoinWordUserService joinWordUserService, WordService wordService) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.joinWordUserService = joinWordUserService;
        this.wordService = wordService;
    }

    @Transactional
    public Long generateQuizSet(Long userId) {
        User userInfo = this.userRepository.findById(userId);

        JoinWordUser[] joinWordUsers = this.joinWordUserService.pickWordsForQuiz(userId);

//        System.out.println("야후 : " + joinWordUsers.length);

        QuizDetail[] details = new QuizDetail[joinWordUsers.length];
        for (int sequence = 0; sequence < 10; sequence++) {
            // 단어 갖고 오기
            List<RandWordMeaningDto> randomWordMeaning = this.wordService.findRandomWordMeaning(
                joinWordUsers[sequence].getWord().getUid());
            int answerLoc = (int) (Math.random() * 10) % 3;
            details[sequence] = QuizDetail.createQuizDetail(
                sequence, answerLoc, joinWordUsers[sequence], randomWordMeaning.get(0).getMeaning(),
                randomWordMeaning.get(1).getMeaning());
        }

        // quizSet 생성
        QuizSet quizSet = QuizSet.createQuizSet(userInfo, details);

        // 이후로 저장
        this.quizRepository.saveQuizSet(quizSet);

//         update Join User Word UpdateAt
        for (JoinWordUser joinWordUser : joinWordUsers) {
            joinWordUser.updateDataWhenCreateQuiz();
        }
        return quizSet.getId();
    }

    @Transactional
    public int checkAnswerAndIncreaseSequence(Long quizSetId, int selectedAnswer) {
        QuizSet quizInfo = this.quizRepository.findQuizSetById(quizSetId);

        int currentSequence = quizInfo.getCurrentSequence();

        QuizDetail quizDetail = this.quizRepository.findQuizDetailById(
            quizInfo.getQuizDetails().get(currentSequence).getId());

        // 정답이면 true로 변경
        if (quizDetail.getAnswerLoc() == selectedAnswer) {
            quizDetail.goodAnswer();
        }

        return quizInfo.increaseQuizSequence();
    }


    public QuizSet getQuizInfo(Long quizSetId) {
        return this.quizRepository.findQuizSetById(quizSetId);
    }

    public List<QuizSet> getQuizSetList(int pageNumber, int len, Long userId) {

        return this.quizRepository.findQuizSetList(pageNumber, len, userId);
    }

    public boolean checkRecentGenerateQuizSet(Long userId) {
        Optional<QuizSet> recentQuizSet = this.quizRepository.findRecentQuizSet(userId);

        // 처음 만드는 경우이므로 패스
        return recentQuizSet.map(
                quizSet -> LocalDateTime.now().isAfter(quizSet.getCreatedAt().plusHours(12)))
            .orElse(true);

        // 최근 날짜 비교
    }

}
