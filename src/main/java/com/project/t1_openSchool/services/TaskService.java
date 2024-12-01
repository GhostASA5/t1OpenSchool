package com.project.t1_openSchool.services;

import com.project.t1_openSchool.dto.TaskDto;
import com.project.t1_openSchool.dto.TaskDtoList;
import com.project.t1_openSchool.kafka.KafkaTaskProducer;
import com.project.t1_openSchool.mapper.TaskMapper;
import com.project.t1_openSchool.model.Status;
import com.project.t1_openSchool.model.Task;
import com.project.t1_openSchool.repository.TaskRepository;
import com.project.t1_openSchool.utils.BeanUtils;
import com.spring.project.aspect.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    @Value("${app.kafka.topic.task-status}")
    private String taskTopic;

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    private final KafkaTaskProducer kafkaTaskProducer;

    @LogSpendTime
    public TaskDtoList getAllTasks() {
        return taskMapper.taskListToTaskDtoList(taskRepository.findAll());
    }

    @LogThrowing
    @LogResult
    public TaskDto getTaskById(Long id) {
        Task task =  taskRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Task with id " + id + " not found.")
        );
        return taskMapper.taskToTaskDto(task);
    }

    @LogBefore
    public TaskDto saveTask(TaskDto task) {
        Task taskEntity = taskMapper.taskDtoToTask(task);
        return taskMapper.taskToTaskDto(taskRepository.save(taskEntity));
    }

    @LogSpendTime
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task existedTask = taskRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Task with id " + id + " not found."));
        Task newTask = taskMapper.taskDtoToTask(taskDto);
        Status oldStatus = existedTask.getStatus();

        BeanUtils.copyNonNullProperties(newTask, existedTask);
        taskRepository.save(existedTask);
        if (!oldStatus.equals(taskDto.getStatus())) {
            kafkaTaskProducer.send(taskTopic, existedTask.getId(), newTask.getStatus());
        }
        return taskMapper.taskToTaskDto(existedTask);

    }

    @LogThrowing
    public void deleteTaskById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        }
        throw new RuntimeException("Task with id " + id + " not found.");
    }
}
