package shurona.wordfinder.word.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.common.SessionConst;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.dto.ConnectWordForm;
import shurona.wordfinder.word.dto.WordEditForm;
import shurona.wordfinder.word.dto.WordForm;
import shurona.wordfinder.word.dto.WordListForm;
import shurona.wordfinder.word.service.JoinWordUserService;
import shurona.wordfinder.word.service.WordService;

@Controller
public class WordController {

    private final JoinWordUserService joinWordUserService;
    private final WordService wordService;
    private final UserService userService;

    @Autowired
    public WordController(JoinWordUserService joinWordUserService, WordService wordService, UserService userService) {
        this.joinWordUserService = joinWordUserService;
        this.wordService = wordService;
        this.userService = userService;
    }

    /**
     * DB에 존재하는 단어 저장
     */
    @GetMapping("/word")
    public String wordForm(
            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) Long userId,
            Model model
    ) {
        User user = this.userService.findById(userId);
        WordForm wordForm = new WordForm();
        wordForm.setNickname(user.getNickname());
        model.addAttribute("word", wordForm);

        return "word/registerWord";
    }

    @PostMapping("/word")
    public String registerWord(
            @Validated @ModelAttribute("word") ConnectWordForm wordForm,
            BindingResult bindingResult,
            @SessionAttribute(value = SessionConst.LOGIN_USER) Long userId,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            return "word/registerWord";
        }

        // checkWord
        Word wordInfo = this.wordService.getWordByWordInfo(wordForm.getWord());

        if (wordInfo == null) {
            redirectAttributes.addAttribute("word", wordForm.getWord());
            return "redirect:/word-meaning";
        }

        this.joinWordUserService.generate(userId, wordInfo.getWord(), wordInfo.getMeaning());
        return "redirect:/words";
    }



    /**
     * 단어 저장 Form
     */
    @GetMapping("/word-meaning")
    public String wordWithMeaningForm(
            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) Long userId,
            @RequestParam("word") String word,
            Model model
    ) {
        User user = this.userService.findById(userId);
        WordForm wordForm = new WordForm();
        wordForm.setNickname(user.getNickname());
        wordForm.setWord(word);
        model.addAttribute("word", wordForm);

        return "word/registerWordWithMeaning";
    }

    @PostMapping("/word-meaning")
    public String registerWordWithMeaning(
            @Validated @ModelAttribute("word") WordForm wordForm,
            BindingResult bindingResult,
            @SessionAttribute(value = SessionConst.LOGIN_USER) Long userId
    ) {
        if (bindingResult.hasErrors()) {
            return "word/registerWordWithMeaning";
        }

        this.joinWordUserService.generate(userId, wordForm.getWord(), wordForm.getMeaning());

        return "redirect:/words";
    }

    /**
     * 단어 목록 보여주기
     */
    @GetMapping("/words")
    public String wordList(
            Model model,
            @SessionAttribute(value = SessionConst.LOGIN_USER) Long userId
    ) {
        WordListForm[] userWordList = this.joinWordUserService.getUserWordList(userId);

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

    @PostMapping("/word/edit/{id}")
    public String wordEdit(
            @PathVariable("id") String id,
            @ModelAttribute("form") WordEditForm form,
            BindingResult bindingResult
    ) {
        Word wordInfo = this.wordService.getWordById(id);

        if (!wordInfo.getWord().equals(form.getWord())) {
            bindingResult.reject("wrongWord", "잘못된 수정 접근 입니다.");
        }

        if (bindingResult.hasErrors()) {
            return "word/editWord";
        }

        // 수정 진행
        this.wordService.updateMeaning(wordInfo.getUid(), form.getMeaning());
        return "redirect:/words";
    }
}
