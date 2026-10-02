package com.college.collegenotify.service;

import com.college.collegenotify.model.Notice;
import com.college.collegenotify.model.Submission;
import com.college.collegenotify.model.User;
import com.college.collegenotify.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public Submission createSubmission(Notice notice, User student, String link, String message) {
        Submission submission = new Submission();
        submission.setNotice(notice);
        submission.setStudent(student);
        submission.setLink(link);
        submission.setMessage(message);
        submission.setStatus("PENDING");
        return submissionRepository.save(submission);
    }

    public List<Submission> findByNotice(Notice notice) {
        return submissionRepository.findByNoticeOrderBySubmittedAtDesc(notice);
    }

    public List<Submission> findByStudent(Long studentId) {
        return submissionRepository.findByStudentIdOrderBySubmittedAtDesc(studentId);
    }

    public void updateStatus(Long id, String status) {
        Submission submission = submissionRepository.findById(id).orElseThrow();
        submission.setStatus(status);
        submissionRepository.save(submission);
    }
}
