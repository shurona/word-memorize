package shurona.wordfinder.quiz.service;


import org.hibernate.mapping.Join;
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

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final JoinWordUserService joinWordUserService;
    private final WordService wordService;

    @Autowired
    QuizService(QuizRepository quizRepository, UserRepository userRepository, JoinWordUserService joinWordUserService, WordService wordService) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.joinWordUserService = joinWordUserService;
        this.wordService = wordService;
    }

    @Transactional
    public Long generateQuizSet(Long userId) {
        User userInfo = this.userRepository.findById(userId);

        JoinWordUser[] joinWordUsers = this.joinWordUserService.pickWordsForQuiz(userId);

        System.out.println("==================================== 페치 =============================================");

        QuizDetail[] details = new QuizDetail[joinWordUsers.length];
        for (int sequence = 0; sequence < 10; sequence++) {
            // TODO: 효율적으로 개선
            // 단어 갖고 오기
            List<RandWordMeaningDto> randomWordMeaning = this.wordService.findRandomWordMeaning(joinWordUsers[sequence].getWord().getUid());
            int answerLoc = (int)(Math.random() * 10) % 3;
            details[sequence] = QuizDetail.createQuizDetail(
                    sequence, answerLoc,  joinWordUsers[sequence], randomWordMeaning.get(0).getMeaning() , randomWordMeaning.get(1).getMeaning());
        }

        // quizSet 생성
        QuizSet quizSet = QuizSet.createQuizSet(userInfo, details);

        this.quizRepository.saveQuizSet(quizSet);

        return quizSet.getId();
    }

    @Transactional
    public int checkAnswerAndIncreaseSequence(Long quizSetId, int selectedAnswer) {
        QuizSet quizInfo = this.quizRepository.findQuizSetById(quizSetId);

        int currentSequence = quizInfo.getCurrentSequence();

        QuizDetail quizDetail = this.quizRepository.findQuizDetailById(quizInfo.getQuizDetails().get(currentSequence).getId());


        System.out.println(quizDetail.getAnswerLoc() + " : " + selectedAnswer);
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

}
