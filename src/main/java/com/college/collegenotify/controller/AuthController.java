package com.college.collegenotify.controller;

import com.college.collegenotify.model.Role;
import com.college.collegenotify.model.User;
import com.college.collegenotify.service.AuthService;
import com.college.collegenotify.service.NoticeService;
import com.college.collegenotify.service.SubmissionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;
    private final NoticeService noticeService;
    private final SubmissionService submissionService;

    public AuthController(AuthService authService, NoticeService noticeService, SubmissionService submissionService) {
        this.authService = authService;
        this.noticeService = noticeService;
        this.submissionService = submissionService;
    }

    @GetMapping({"/", "/login"})
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        return authService.login(email, password)
                .map(user -> {
                    session.setAttribute("loggedUser", user);
                    return "redirect:/dashboard";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Invalid email or password");
                    return "login";
                });
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid User user, Model model) {
        try {
            authService.register(user);
            model.addAttribute("success", "Account created successfully. Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Try again.");
            model.addAttribute("roles", Role.values());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
