package shurona.wordfinder.quiz.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shurona.wordfinder.quiz.domain.QuizDetail;
import shurona.wordfinder.quiz.domain.QuizSet;
import shurona.wordfinder.quiz.dto.QuizAnswerForm;
import shurona.wordfinder.quiz.dto.QuizListForm;
import shurona.wordfinder.quiz.dto.QuizResultForm;
import shurona.wordfinder.quiz.service.QuizService;
import shurona.wordfinder.user.common.SessionConst;
import shurona.wordfinder.user.session.UserSession;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.service.JoinWordUserService;

@Controller
@RequestMapping("quiz")
public class QuizController {

    private final QuizService quizService;
    private final JoinWordUserService joinWordUserService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    QuizController(JoinWordUserService joinWordUserService, QuizService quizService) {
        this.quizService = quizService;
        this.joinWordUserService = joinWordUserService;
    }

    /**
     * 정답 위치에 맞게 뜻 문제를 넣어주는 함수
     */
    private static List<String> generateRandomList(QuizSet quizInfo, Word currentWord) {
        QuizDetail currentDetail = quizInfo.getQuizDetails().get(quizInfo.getCurrentSequence());

        int toggle = 0;
        List<String> randomList = new ArrayList<>();
        for (int seq = 0; seq < 3; seq++) {
            if (seq == currentDetail.getAnswerLoc()) {
                randomList.add(currentWord.getMeaning());
                continue;
            }
            if (toggle == 0) {
                randomList.add(currentDetail.getFirstMeaning());
                toggle += 1;
            } else {
                randomList.add(currentDetail.getSecondMeaning());
            }
        }
        return randomList;
    }

    @GetMapping("intro")
    public String quizWord(
//            @ModelAttribute("user") Long user
    ) {
        return "quiz/intro";
    }

    @PostMapping("generate")
    public String generateQuiz(
        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) UserSession userSession,
        RedirectAttributes redirectAttributes,
        @ModelAttribute("quiz") QuizAnswerForm quizForm,
        BindingResult bindingResult,
        Model model
    ) {

        int jwuLength = this.joinWordUserService.checkUserWordCount(userSession.getUserId(), true);
        if (jwuLength < 11) {
            bindingResult.reject("nonEnough", "단어를 최소 10개를 만들어주세요 \n" +
                "현재 단어 갯수 : " + jwuLength);
        }

        boolean generateAble = this.quizService.checkRecentGenerateQuizSet(userSession.getUserId());
        generateAble = true;

        if (!generateAble) {
            List<QuizSet> quizSetList = this.quizService.getQuizSetList(0, 1,
                userSession.getUserId());
            QuizSet quizSet = quizSetList.get(0);
            bindingResult.reject("fastCreate", "퀴즈는 12시간에 한 번 만들 수 있습니다. \n" +
                "생성 가능 시간 : " + quizSet.getCreatedAt().plusHours(12)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getGlobalError());
            return "quiz/intro";
        }

        this.log.info("생성중... 유저 : {}", userSession.getUserId());
        Long quizSetId = this.quizService.generateQuizSet(userSession.getUserId());

        redirectAttributes.addAttribute("id", quizSetId);

        return "redirect:/quiz/problem/{id}";
    }

    @PostMapping("problem/{id}")
    public String checkAnswer(
        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) UserSession userSession,
        @PathVariable("id") Long quizSetId,
        @ModelAttribute("quiz") QuizAnswerForm quizForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {

        if (quizForm.getSelectedAnswer() == null) {
            bindingResult.rejectValue("selectedAnswer", "not-selected", "답이 선택되지 않았습니다.");
        }

        if (bindingResult.hasErrors()) {
            quizForm.setQuizSetId(quizSetId);
            return "quiz/quiz";
        }

        QuizSet quizInfo = this.quizService.getQuizInfo(quizSetId);

        if (quizInfo == null || !Objects.equals(quizInfo.getUser().getId(),
            userSession.getUserId())) {
            return "quiz/intro";
        }

        // 정답 확인하고 퀴즈 시퀀스 하나 늘려준다
        int i = this.quizService.checkAnswerAndIncreaseSequence(quizSetId,
            quizForm.getSelectedAnswer());

        if (i < 0) {
            redirectAttributes.addAttribute("id", quizSetId);
            return "redirect:/quiz/result/{id}";
        }

        redirectAttributes.addAttribute("id", quizSetId);
        return "redirect:/quiz/problem/{id}";
    }

    @GetMapping("problem/{id}")
    public String provideUserQuiz(
        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) UserSession userSession,
        @PathVariable("id") Long quizSetId,
        Model model
    ) {
        QuizSet quizInfo = this.quizService.getQuizInfo(quizSetId);

        if (quizInfo == null || !Objects.equals(quizInfo.getUser().getId(),
            userSession.getUserId())) {
            return "quiz/intro";
        }

        // 이런 상황이 생길 지는 모르겠지만 예외로 달아놓음
        if (quizInfo.getCurrentSequence() == 10) {
            return "quiz/intro";
        }

        // Form 생성 및 초기화
        Word currentWord = quizInfo.getQuizDetails().get(quizInfo.getCurrentSequence())
            .getJoinWordUser().getWord();
        QuizAnswerForm answerForm = new QuizAnswerForm();
        answerForm.setQuizSetId(quizSetId);
        answerForm.setSelectedAnswer(-1);
        answerForm.setSequence(quizInfo.getCurrentSequence());
        answerForm.setWord(currentWord.getWord());
        List<String> randomList = generateRandomList(quizInfo, currentWord);
        answerForm.setMeaningList(randomList);
        model.addAttribute("quiz", answerForm);
        return "quiz/quiz";
    }

    @GetMapping("list")
    public String quizList(
        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) UserSession userSession,
        Model model
    ) {

        List<QuizSet> quizSetList = this.quizService.getQuizSetList(0, 20, userSession.getUserId());

        List<QuizListForm> quizListFormList = new ArrayList<>();

        for (QuizSet quizSet : quizSetList) {
            //TODO: 상수 값 common으로 이동 후 설정
            quizListFormList.add(
                new QuizListForm(quizSet.getId(), quizSet.getCurrentSequence(),
                    quizSet.getCreatedAt(), quizSet.getCurrentSequence() == 10)
            );
        }

        model.addAttribute("quiz", quizListFormList);

        return "quiz/quizList";
    }

    @GetMapping("result/{id}")
    public String quizResult(
        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) UserSession userSession,
        @PathVariable("id") Long quizId,
        Model model
    ) {

        QuizSet quizInfo = this.quizService.getQuizInfo(quizId);

        // 유저와 quizSet 정보가 불일치 하면 밴
        if (quizInfo == null || !Objects.equals(quizInfo.getUser().getId(),
            userSession.getUserId())) {
            return "/";
        }

        // 현재 시퀀스가 맞지 않으면 돌려보낸다.
        if (quizInfo.getCurrentSequence() != 10) {
            return "quiz/intro";
        }

        // quiz Detail 정보
        // word, meaning, 틀렸는 여부
        List<QuizResultForm> resultFormList = new ArrayList<>();
        for (QuizDetail quizDetail : quizInfo.getQuizDetails()) {
            Word thisWord = quizDetail.getJoinWordUser().getWord();
            resultFormList.add(
                new QuizResultForm(thisWord.getWord(), thisWord.getMeaning(),
                    quizDetail.getIsCorrect()));
        }
        model.addAttribute("quiz", resultFormList);

        // quizSet 정보
        model.addAttribute("date", quizInfo.getCreatedAt());

        return "quiz/quizResult";
    }

    @GetMapping("test")
    @ResponseBody
    public String testInfo() {

        QuizSet quizInfo = this.quizService.getQuizInfo(152L);
        System.out.println("=======================카트=======================");
        Word currentWord = quizInfo.getQuizDetails().get(quizInfo.getCurrentSequence())
            .getJoinWordUser().getWord();

//        List<RandWordMeaningDto> randomWordMeaning = this.wordService.findRandomWordMeaning(currentWord.getUid());
//
//        System.out.println(currentWord.getMeaning());
//        for (RandWordMeaningDto randWordMeaningDto : randomWordMeaning) {
//            System.out.println(randWordMeaningDto);
//        }

        return "야후";
    }

}
