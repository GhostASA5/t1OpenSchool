package com.project.t1_openSchool.controllers;

import com.project.t1_openSchool.dto.TaskDto;
import com.project.t1_openSchool.dto.TaskDtoList;
import com.project.t1_openSchool.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public TaskDtoList getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable(name = "id") Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public TaskDto createTask(@RequestBody TaskDto task) {
        return taskService.saveTask(task);
    }

    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable(name = "id") Long id, @RequestBody TaskDto task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable(name = "id") Long id) {
        taskService.deleteTaskById(id);
    }
}
