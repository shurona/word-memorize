package shurona.wordfinder.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "user/createUserForm";
    }

    /**
     * 유저 생성
     */
    @PostMapping("new")
    public String create(
        @Validated @ModelAttribute("userForm") UserForm form,
        BindingResult bindingResult
    ) {

        System.out.println("form = " + form);

        // 중복 체크
        boolean nicknameCheck = this.userService.checkUserNicknameDup(form.getNickname());
        boolean loginIdCheck = this.userService.checkUserLoginIdDup(form.getLoginId());

        if (nicknameCheck) {
            bindingResult.rejectValue("nickname", "dup","중복 닉네임입니다.");
        }

        if (loginIdCheck) {
            bindingResult.rejectValue("loginId", "dup", "중복 아이디입니다.");
        }

        if (bindingResult.hasErrors()) {
            return "user/createUserForm";
        }

        Long userId = this.userService.join(form.getNickname(), form.getLoginId(), form.getPassword());
        //

        return "redirect:/";
    }
}
