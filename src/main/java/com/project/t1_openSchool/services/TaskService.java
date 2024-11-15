package com.project.t1_openSchool.services;

import com.project.t1_openSchool.aspect.LogResult;
import com.project.t1_openSchool.aspect.LogSpendTime;
import com.project.t1_openSchool.aspect.LogThrowing;
import com.project.t1_openSchool.aspect.LogBefore;
import com.project.t1_openSchool.model.Task;
import com.project.t1_openSchool.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    @LogSpendTime
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @LogBefore
    @LogThrowing
    @LogResult
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Task with id " + id + " not found.")
        );
    }

    @LogBefore
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @LogSpendTime
    public Task updateTask(Long id, Task task) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
            Task updatedTask = taskOptional.get();
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setUserId(task.getUserId());
            return taskRepository.save(updatedTask);
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
