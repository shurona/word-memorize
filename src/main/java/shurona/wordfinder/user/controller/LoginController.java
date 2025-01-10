package shurona.wordfinder.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import shurona.wordfinder.common.PasswdToHash;
import shurona.wordfinder.user.common.SessionConst;
import shurona.wordfinder.user.controller.dto.LoginForm;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.user.session.UserSession;

@Controller
public class LoginController {

    //TODO: 외부에서 주입하도록 변경
    private static final int LOGIN_SESSION_TIME = 60 * 60 * 2;
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
        @Validated @ModelAttribute("loginForm") LoginForm form,
        BindingResult bindingResult,
        HttpServletRequest request,
        @RequestParam(value = "redirectURL", defaultValue = "/") String redirectURL
    ) {
        User loginUser = this.userService.login(form.getLoginId(),
            PasswdToHash.doProcess(form.getPassword()));

        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "user/login/loginForm";
        }
        HttpSession session = request.getSession();
        UserSession userSession = UserSession.createUserSession(loginUser.getId(),
            loginUser.getRole());
        //TODO: 로그인 시간 외부에서 주입하도록 수정
        session.setMaxInactiveInterval(LOGIN_SESSION_TIME);
        session.setAttribute(SessionConst.LOGIN_USER, userSession);

        return "redirect:" + redirectURL;
    }

    @PostMapping("logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    /**
     * 세션 테스트용 api
     */
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
