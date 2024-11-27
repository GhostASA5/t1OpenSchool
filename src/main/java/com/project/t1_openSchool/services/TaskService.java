package com.project.t1_openSchool.services;

import com.project.t1_openSchool.aspect.LogResult;
import com.project.t1_openSchool.aspect.LogSpendTime;
import com.project.t1_openSchool.aspect.LogThrowing;
import com.project.t1_openSchool.aspect.LogBefore;
import com.project.t1_openSchool.dto.TaskDto;
import com.project.t1_openSchool.dto.TaskDtoList;
import com.project.t1_openSchool.kafka.KafkaTaskProducer;
import com.project.t1_openSchool.mapper.TaskMapper;
import com.project.t1_openSchool.model.Task;
import com.project.t1_openSchool.repository.TaskRepository;
import com.project.t1_openSchool.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @LogBefore
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
        Optional<Task> taskOptional = taskRepository.findById(id);
        Task task = taskMapper.taskDtoToTask(taskDto);

        if (taskOptional.isPresent()) {
            Task updatedTask = taskOptional.get();
            if (!updatedTask.getStatus().equals(task.getStatus())) {
                kafkaTaskProducer.send(taskTopic, updatedTask.getId(), task.getStatus());
            }
            BeanUtils.copyNonNullProperties(task, updatedTask);
            return taskMapper.taskToTaskDto(taskRepository.save(updatedTask));
        }
        throw new RuntimeException("Task with id " + id + " not found.");
    }

    @LogThrowing
    public void deleteTaskById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        }
        throw new RuntimeException("Task with id " + id + " not found.");
    }
}
