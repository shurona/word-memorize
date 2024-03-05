package shurona.wordfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shurona.wordfinder.controller.dto.UserForm;
import shurona.wordfinder.domain.User;
import shurona.wordfinder.service.UserService;

@Controller
public class TestController {
    private final UserService userService;

    @Autowired
    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String getUserInfo(@PathVariable("id") Long id) {

        User user = this.userService.findById(id);

        if (user == null) {
            return "no user";
        }
        return user.toString();
    }

    @GetMapping("/user/new")
    public String createForm() {
        return "user/createUserForm";
    }


    @GetMapping("/home")
    public String entry() {
        return "home";
    }

    @PostMapping("/user/new")
    public String create(UserForm form) {
        Long userId = userService.join(form.getNickname());
        return "redirect:/" + userId;
    }
}
