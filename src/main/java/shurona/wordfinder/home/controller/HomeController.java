package shurona.wordfinder.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homeLoginResolver() {
        // TODO: 로그인 시 loginHome


        return "home";
    }

    @GetMapping("/home")
    public String entry() {
        return "home";
    }
}
