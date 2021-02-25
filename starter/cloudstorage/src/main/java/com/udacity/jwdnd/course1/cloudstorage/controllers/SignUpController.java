package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignup() {
        return "signup";
    }

    @PostMapping
    public String postSignup(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        String signupError = null;
        if (user == null) {
            signupError = "User is null. It is very bad...";
        } else {
            if (!userService.isUsernameAvailable(user.getUsername())) {
                signupError = "Username already exists in the database";
            }
        }

        if (signupError == null) {
            int resultId = userService.createUser(user);
            if (resultId < 0) {
                signupError = "Adding user to the database failed";
            }
        }

        if (signupError == null) {
            redirectAttributes.addFlashAttribute("successMsg", "Signup succeeded");
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", signupError);
            return "signup";
        }
    }
}
