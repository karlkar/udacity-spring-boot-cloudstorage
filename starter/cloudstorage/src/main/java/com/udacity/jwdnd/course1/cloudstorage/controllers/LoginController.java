package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLogin(Model model, HttpServletRequest request) {
        model.addAttribute("errorMsg", null);
        model.addAttribute("successMsg", null);
        Map<String, ?> attrs = RequestContextUtils.getInputFlashMap(request);
        if (attrs != null && attrs.get("successMsg") != null) {
            model.addAttribute("successMsg", attrs.get("successMsg") );
        }
        if (request.getParameter("error") != null) {
            model.addAttribute("errorMsg", "Invalid username or password");
        }
        if (request.getParameter("logout") != null) {
            model.addAttribute("successMsg", "Logged out successfully");
        }
        return "login";
    }
}
