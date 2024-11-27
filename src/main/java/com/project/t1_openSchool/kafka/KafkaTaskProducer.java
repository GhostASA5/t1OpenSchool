package com.project.t1_openSchool.kafka;

import com.project.t1_openSchool.dto.UpdateTaskStatusEvent;
import com.project.t1_openSchool.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTaskProducer {

    private final KafkaTemplate<String, UpdateTaskStatusEvent> kafkaTemplate;

    public void send(String topic, Long id, Status status) {
        try{
            UpdateTaskStatusEvent event = UpdateTaskStatusEvent.builder()
                    .taskId(id)
                    .status(status)
                    .build();
            kafkaTemplate.send(topic, event).get();
            kafkaTemplate.flush();
            log.info("Sent update task status event with taskId: {} and status: {}", id, status);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
