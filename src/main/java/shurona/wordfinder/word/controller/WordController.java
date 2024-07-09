package shurona.wordfinder.word.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.common.SessionConst;
import shurona.wordfinder.user.domain.UserRole;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.user.session.UserSession;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.domain.WordEditStatus;
import shurona.wordfinder.word.dto.ConnectWordForm;
import shurona.wordfinder.word.dto.WordEditForm;
import shurona.wordfinder.word.dto.WordForm;
import shurona.wordfinder.word.dto.WordListForm;
import shurona.wordfinder.word.service.JoinWordUserService;
import shurona.wordfinder.word.service.WordExternalDtoService;
import shurona.wordfinder.word.service.WordService;
import shurona.wordfinder.word.utils.CacheWordLimit;
import shurona.wordfinder.word.utils.MemoryCacheWordLimit;

@Controller
public class WordController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JoinWordUserService joinWordUserService;
    private final WordService wordService;
    private final UserService userService;
    private final CacheWordLimit cacheWordLimit;

    private final WordExternalDtoService wordExternalDtoService;

    @Autowired
    public WordController(JoinWordUserService joinWordUserService, WordService wordService, UserService userService, MemoryCacheWordLimit memoryCacheWordLimit, WordExternalDtoService wordExternalDtoService) {
        this.joinWordUserService = joinWordUserService;
        this.wordService = wordService;
        this.userService = userService;
        this.cacheWordLimit = memoryCacheWordLimit;
        this.wordExternalDtoService = wordExternalDtoService;
    }

    /**
     * DB에 존재하는 단어 저장
     */
    @GetMapping("/word")
    public String wordForm(
            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) UserSession userSession,
            Model model
    ) {
        User user = this.userService.findById(userSession.getUserId());
        ConnectWordForm wordForm = new ConnectWordForm();
        wordForm.setNickname(user.getNickname());
        wordForm.setRemainCount(this.cacheWordLimit.checkCount(userSession.getUserId()));
        model.addAttribute("word", wordForm);

        return "word/registerWord";
    }

    @PostMapping("/word")
    public String registerWord(
            @Validated @ModelAttribute("word") ConnectWordForm wordForm,
            BindingResult bindingResult,
            @SessionAttribute(value = SessionConst.LOGIN_USER) UserSession userSession,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        // 하루에 저장할 수 있는 단어 넘었는지 체크
        if (!userSession.getRole().equals(UserRole.ADMIN)) {
            // check remain count
            int remainCount = this.cacheWordLimit.checkCount(userSession.getUserId());
            if (remainCount <= 0) {
                bindingResult.reject("remainZero", "하루에 단어는 12개 저장가능합니다");
            }
        }

        checkWordDuplication(wordForm, bindingResult, userSession);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getGlobalError());
            return "word/registerWord";
        }

        // ADMIN이 아니면 차감한다.
        if (!userSession.getRole().equals(UserRole.ADMIN)) {
            // 넘기기 전에 단어 횟수 차감 후 시작
            this.cacheWordLimit.useWordCount(userSession.getUserId());
        }

        //==== 조회 및 접근 제어 로직 완료 후 로직 시작

        // checkWord
        Word wordInfo = this.wordService.getWordByWordInfo(wordForm.getWord());

        // Init variable
        String meaningInfo;
        String wordString;
        WordEditStatus wordEditInfo = WordEditStatus.COMPLETE;

        // 단어가 없으면 뜻을 갖고 온다.
        if (wordInfo == null) {
            try {
                meaningInfo = this.wordExternalDtoService.getMeaningInfo(wordForm.getWord());
                wordString = wordForm.getWord();
                // 뜻을 잘못 갖고오면 수정할 수 있게 해준다.
                if (meaningInfo == null) {
                    meaningInfo = wordString;
                    wordEditInfo = WordEditStatus.EDITABLE;
                }
            } catch (Exception e) {
                // 여기서 오류 발생 시 카운트 롤백
                this.cacheWordLimit.rollBackCount(userSession.getUserId());
                this.log.error("Error occur during get meaning info from deepl {}", e.getMessage());
                bindingResult.reject("deeplError", "에러가 발생하였습니다. 반복되면 개발자에 문의주세요");
                model.addAttribute("errors", bindingResult.getGlobalError());
                return "word/registerWord";
            }
        } else {
            meaningInfo = wordInfo.getMeaning();
            wordString = wordInfo.getWord();
        }

        this.joinWordUserService.generate(userSession.getUserId(), wordString, meaningInfo, wordEditInfo);
        return "redirect:/words";
    }

    /**
     * 단어 저장 Form
     */
    @GetMapping("/word-meaning")
    public String wordWithMeaningForm(
            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) UserSession userSession,
            @RequestParam("word") String word,
            Model model
    ) {
        User user = this.userService.findById(userSession.getUserId());
        WordForm wordForm = new WordForm();
        wordForm.setNickname(user.getNickname());
        wordForm.setWord(word);
        wordForm.setRemainCount(this.cacheWordLimit.checkCount(userSession.getUserId()));
        model.addAttribute("word", wordForm);

        return "word/registerWordWithMeaning";
    }

    @PostMapping("/word-meaning")
    public String registerWordWithMeaning(
            @Validated @ModelAttribute("word") WordForm wordForm,
            BindingResult bindingResult,
            @SessionAttribute(value = SessionConst.LOGIN_USER) UserSession userSession,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getGlobalError());
            return "word/registerWordWithMeaning";
        }

        this.joinWordUserService.generate(userSession.getUserId(), wordForm.getWord(), wordForm.getMeaning(), WordEditStatus.COMPLETE);
        return "redirect:/words";
    }

    /**
     * 단어 목록 보여주기
     */
    @GetMapping("/words")
    public String wordList(
            Model model,
            @SessionAttribute(value = SessionConst.LOGIN_USER) UserSession userSession
    ) {
        WordListForm[] userWordList = this.joinWordUserService.getUserWordList(userSession.getUserId());

        model.addAttribute("words", userWordList);

        return "word/wordList";
    }

    /**
     * 단어 정보 수정
     */
    @GetMapping("/word/edit/{id}")
    public String wordEditForm(
        @PathVariable("id") String uuid,
        @ModelAttribute("form") WordEditForm form
        ) {
        Word word = this.wordService.getWordById(uuid);

        if (word == null) {
            return "word/wordList";
        }

        form.setWord(word.getWord());
        form.setWordId(word.getUid());
        form.setMeaning(word.getMeaning());
        return "word/editWord";
    }

//    @PostMapping("/word/edit-request")
//    public String requestEdit() {
//
//
//        return "word/wordList";
//    }

    @PostMapping("/word/edit/{id}")
    public String wordEdit(
            @PathVariable("id") String id,
            @ModelAttribute("form") WordEditForm form,
            BindingResult bindingResult
    ) {
        Word wordInfo = this.wordService.getWordById(id);

        if (!wordInfo.getWord().equals(form.getWord()) || !wordInfo.getStatus().equals(WordEditStatus.EDITABLE)) {
            bindingResult.reject("wrongWord", "잘못된 수정 접근 입니다.");
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/words";
        }

        // 수정 진행
        this.wordService.updateMeaning(wordInfo.getUid(), form.getMeaning());
        return "redirect:/words";
    }

    @PostMapping("word/hide/{id}")
    public String hideWord(
            @PathVariable("id") String wordUid,
            @SessionAttribute(value = SessionConst.LOGIN_USER) UserSession userSession
    ) {
        // 단어 확인
        JoinWordUser jwuInfo = this.joinWordUserService.findJwuByUserIdAndWordId(userSession.getUserId(), wordUid);
        if (jwuInfo == null || !jwuInfo.getVisible()) {
            return "redirect:/words";
        }

        // 만약 비 소유 단어면 단어 목록 화면으로 돌아간다.
        boolean isWrite = this.joinWordUserService.checkWordUserSet(userSession.getUserId(), jwuInfo.getWord().getWord());
        if (!isWrite) {
            return "redirect:/words";
        }

        try {
            this.joinWordUserService.hideWordsByUser(userSession.getUserId(), wordUid);
        } catch (Exception e) {
            //
            this.log.error("숨기는 중에 문제가 발견하였습니다. {}", e.getMessage());
        }

        return "redirect:/words";
    }

    /*
     * 접근 제어 메서드
     */

    /**
     * 이미 저장한 단어 인지 확인
     */
    private void checkWordDuplication(ConnectWordForm wordForm, BindingResult bindingResult, UserSession userSession) {
        boolean check = this.joinWordUserService.checkWordUserSet(userSession.getUserId(), wordForm.getWord());
        if (check) {
            bindingResult.reject("alreadyExist", "이미 저장한 단어입니다.");
        }
    }
}
