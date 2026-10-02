package com.college.collegenotify.controller;

import com.college.collegenotify.model.Notice;
import com.college.collegenotify.model.Role;
import com.college.collegenotify.model.Submission;
import com.college.collegenotify.model.User;
import com.college.collegenotify.service.NoticeService;
import com.college.collegenotify.service.SubmissionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DashboardController {

    private final NoticeService noticeService;
    private final SubmissionService submissionService;

    public DashboardController(NoticeService noticeService, SubmissionService submissionService) {
        this.noticeService = noticeService;
        this.submissionService = submissionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Notice> notices = noticeService.getAllNotices();
        List<Submission> submissions = submissionService.findByStudent(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("notices", notices);
        model.addAttribute("submissions", submissions);
        model.addAttribute("isAdminOrFaculty", user.getRole() == Role.ADMIN || user.getRole() == Role.FACULTY);
        return "dashboard";
    }

    @PostMapping("/notices/create")
    public String createNotice(@RequestParam String title,
                               @RequestParam String content,
                               @RequestParam String category,
                               HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.FACULTY) {
            noticeService.createNotice(user, title, content, category);
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/submissions/create")
    public String createSubmission(@RequestParam Long noticeId,
                                  @RequestParam String link,
                                  @RequestParam String message,
                                  HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Notice> notices = noticeService.getAllNotices();
        for (Notice notice : notices) {
            if (notice.getId().equals(noticeId)) {
                submissionService.createSubmission(notice, user, link, message);
                break;
            }
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/submissions/update-status")
    public String updateStatus(@RequestParam Long submissionId,
                               @RequestParam String status,
                               HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.FACULTY) {
            submissionService.updateStatus(submissionId, status);
        }
        return "redirect:/dashboard";
    }
}
