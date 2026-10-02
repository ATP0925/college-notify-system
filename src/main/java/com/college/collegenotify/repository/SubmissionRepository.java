package com.college.collegenotify.repository;

import com.college.collegenotify.model.Notice;
import com.college.collegenotify.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByNoticeOrderBySubmittedAtDesc(Notice notice);
    List<Submission> findByStudentIdOrderBySubmittedAtDesc(Long studentId);
}
