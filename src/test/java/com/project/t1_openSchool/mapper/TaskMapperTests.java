package com.project.t1_openSchool.mapper;

import com.project.t1_openSchool.dto.TaskDto;
import com.project.t1_openSchool.dto.TaskDtoList;
import com.project.t1_openSchool.model.task.Status;
import com.project.t1_openSchool.model.task.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for TaskMapper")
public class TaskMapperTests {

    private final TaskDto taskDto = TaskDto.builder()
            .id(1L)
            .title("Title task done")
            .status(Status.DONE)
            .build();

    private final Task task = Task.builder()
            .id(1L)
            .title("Title task done")
            .status(Status.DONE)
            .build();

    private final TaskMapper taskMapper = new TaskMapperImpl();

    @Test
    @DisplayName("Test taskDtoToTask")
    void testTaskDtoToTask() {
        Task result = taskMapper.taskDtoToTask(taskDto);

        assertNotNull(result);
        assertEquals(taskDto.getId(), result.getId());
        assertEquals(taskDto.getTitle(), result.getTitle());
        assertEquals(taskDto.getStatus(), result.getStatus());
    }

    @Test
    @DisplayName("Test taskToTaskDto")
    void testTaskToTaskDto() {
        TaskDto result = taskMapper.taskToTaskDto(task);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getStatus(), result.getStatus());
    }

    @Test
    @DisplayName("Test taskListToTaskDtoList")
    public void testTaskListToTaskDtoList(){
        Task task2 = Task.builder()
                .id(2L)
                .build();

        List<Task> tasks = List.of(task, task2);
        TaskDtoList result = taskMapper.taskListToTaskDtoList(tasks);

        assertNotNull(result);
        assertEquals(2, result.getTaskDtoList().size());
        assertEquals(task.getId(), result.getTaskDtoList().get(0).getId());
        assertEquals(task2.getId(), result.getTaskDtoList().get(1).getId());
    }
}
