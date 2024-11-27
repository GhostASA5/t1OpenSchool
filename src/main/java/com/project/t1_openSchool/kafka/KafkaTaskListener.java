package com.project.t1_openSchool.kafka;

import com.project.t1_openSchool.dto.UpdateTaskStatusEvent;
import com.project.t1_openSchool.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTaskListener {

    private final NotificationService notificationService;

    @KafkaListener(id = "${app.kafka.consumer.group-id}",
            topics = "${app.kafka.topic.task-status}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload UpdateTaskStatusEvent event,
                          Acknowledgment ack,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        log.info("Task consumer: получено новое сообщение event - {}, topic - {}, key - {}", event, topic, key);
        try {
            notificationService.sendNotification(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Ошибка при обработке события {}: {}", event, e.getMessage(), e);
        }
        log.info("Task consumer: записи обработаны");
    }
}