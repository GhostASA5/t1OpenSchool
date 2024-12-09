package com.project.t1_openSchool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.t1_openSchool.dto.TaskDto;
import com.project.t1_openSchool.model.task.Status;
import com.project.t1_openSchool.model.task.Task;
import com.project.t1_openSchool.repository.TaskRepository;
import com.project.t1_openSchool.testConteiner.PostgresContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Tests fot TaskController")
public class TaskControllerTest extends PostgresContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        saveTestTasks();
    }

    @Test
    @DisplayName("Test for getAllTasks")
    public void getAllTasks() throws Exception {
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskDtoList").isArray())
                .andExpect(jsonPath("$.taskDtoList").isNotEmpty())
                .andExpect(jsonPath("$.taskDtoList[0].id").value(1))
                .andExpect(jsonPath("$.taskDtoList[0].title").value("Task 1"))
                .andExpect(jsonPath("$.taskDtoList[1].id").value(2))
                .andExpect(jsonPath("$.taskDtoList[1].title").value("Task 2"));
    }

    @Test
    @DisplayName("Test getTaskById")
    public void getTaskById() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    @DisplayName("Test getTaskById throws exception when task not found")
    public void getTaskByIdShouldThrowExceptionWhenTaskNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", 100))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(RuntimeException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Exception in method: Task with id " + 100 + " not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("Test createTask")
    public void createTask() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .id(3L)
                .title("New Task")
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    @DisplayName("Test updateTask")
    public void updateTask() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .title("Update Task")
                .build();

        mockMvc.perform(put("/api/v1/tasks/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Update Task"));
    }

    @Test
    @DisplayName("Test deleteTask")
    public void deleteTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test deleteTask throws exception when task not found")
    public void deleteTaskShouldThrowExceptionWhenTaskNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{id}", 100))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(RuntimeException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Exception in method: Task with id " + 100 + " not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    private void saveTestTasks() {
        Task task1 = Task.builder()
                .id(1L)
                .title("Task 1")
                .status(Status.DONE)
                .build();
        Task task2 = Task.builder()
                .id(2L)
                .title("Task 2")
                .status(Status.DONE)
                .build();

        taskRepository.saveAll(List.of(task1, task2));
    }
}
