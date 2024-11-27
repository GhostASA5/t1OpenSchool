package com.project.t1_openSchool.services;

import com.project.t1_openSchool.dto.UpdateTaskStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    private static final String RECIPIENT_EMAIL = "artemsvist1809@gmail.com";

    public void sendNotification(UpdateTaskStatusEvent event) {
        log.info("Notification service get event {}", event);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(RECIPIENT_EMAIL);
        message.setSubject("Notification. Update Task Status");
        message.setText(MessageFormat.format("Task with id {0} has been updated to status {1}", event.getTaskId(), event.getStatus()));
        mailSender.send(message);

        log.info("Notification service send event {}", event);
    }
}
