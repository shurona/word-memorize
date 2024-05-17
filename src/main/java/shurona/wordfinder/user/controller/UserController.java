package shurona.wordfinder.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shurona.wordfinder.user.controller.dto.UserForm;
import shurona.wordfinder.user.User;
import shurona.wordfinder.user.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    @ResponseBody
    public String getUserInfo(@PathVariable("id") Long id) {

        User user = this.userService.findById(id);

        if (user == null) {
            return "no user";
        }
        return user.toString();
    }

    @GetMapping("new")
    public String createForm() {
        return "user/createUserForm";
    }

    /**
     * 유저 생성
     */
    @PostMapping("new")
    public String create(UserForm form) {
        Long userId = this.userService.join(form.getNickname(), form.getLoginId(), form.getPassword());
        return "redirect:/";
    }
}
