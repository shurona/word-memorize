package shurona.wordfinder.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.common.SessionConst;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.user.session.UserSession;

@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homeLoginResolver(
            @SessionAttribute(name= SessionConst.LOGIN_USER, required = false) UserSession userSession,
            Model model
    ) {
        if (userSession == null) {
            return "home";
        }

        User user = userService.findById(userSession.getUserId());
        model.addAttribute("user", user);
        return "loginHome";
    }

    @GetMapping("/home")
    public String entry() {
        return "home";
    }
}
