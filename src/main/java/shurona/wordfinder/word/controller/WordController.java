package shurona.wordfinder.word.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WordController {
    @GetMapping("/word")
    public String home() {
        return "word/word";
    }
}
