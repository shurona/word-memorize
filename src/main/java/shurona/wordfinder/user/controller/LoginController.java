package shurona.wordfinder.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shurona.wordfinder.user.User;
import shurona.wordfinder.user.common.SessionConst;
import shurona.wordfinder.user.controller.dto.LoginForm;
import shurona.wordfinder.user.service.UserService;

import java.util.UUID;

@Controller
public class LoginController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {

        return "user/login/loginForm";
    }

    @PostMapping("login")
    public String loginProcess(
            @ModelAttribute("loginForm") LoginForm form,
            HttpServletRequest request,
            @RequestParam(defaultValue = "/") String redirectURL
    ) {
        User loginUser = this.userService.login(form.getLoginId(), form.getPassword());

        if (loginUser == null) {
            return "user/login/loginForm";
        }

        String uuid = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, loginUser.getId());

        return "redirect:" + redirectURL;
    }

    @ResponseBody
    @GetMapping("yahoo")
    public String testSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(SessionConst.LOGIN_USER);

        String output = "";
        if (attribute == null) {
            output = "엄서요";
        } else {
            output = "있어요";
        }
        log.info("session 저장 정보 {}", attribute);

        return output;
    }
}
