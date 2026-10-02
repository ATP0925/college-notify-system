package com.college.collegenotify.service;

import com.college.collegenotify.model.Notice;
import com.college.collegenotify.model.User;
import com.college.collegenotify.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findByActiveTrueOrderByCreatedAtDesc();
    }

    public Notice createNotice(User author, String title, String content, String category) {
        Notice notice = new Notice();
        notice.setAuthor(author);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setCategory(category);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setActive(true);
        return noticeRepository.save(notice);
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
}
