package shurona.wordfinder.word.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.word.dto.WordForm;
import shurona.wordfinder.word.dto.WordListForm;
import shurona.wordfinder.word.service.JoinWordUserService;

import java.util.Arrays;
import java.util.List;

@Controller
public class WordController {

    private final JoinWordUserService joinWordUserService;
    private final UserService userService;

    @Autowired
    public WordController(JoinWordUserService joinWordUserService, UserService userService) {
        this.joinWordUserService = joinWordUserService;
        this.userService = userService;
    }

    @GetMapping("/word")
    public String wordForm(Model model) {
        List<Integer> userIds = this.userService.findUserIds();
//        System.out.println("userIds = " + userIds);
        model.addAttribute("userIds", userIds);
        model.addAttribute("word", new WordForm());

        return "word/registerWord";
    }

    @GetMapping("/words")
    public String wordList(Model model) {

        WordListForm[] userWordList = this.joinWordUserService.getUserWordList();

        model.addAttribute("words", userWordList);

        return "word/wordList";
    }

    @PostMapping("/word")
    public String registerWord(WordForm wordForm) {
        this.joinWordUserService.generate(wordForm.getUserId(), wordForm.getWord(), wordForm.getMeaning());

        return "redirect:/words";
    }
}
