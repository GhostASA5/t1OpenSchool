package com.project.t1_openSchool.services;

import com.project.t1_openSchool.dto.TaskDto;
import com.project.t1_openSchool.dto.TaskDtoList;
import com.project.t1_openSchool.mapper.TaskMapper;
import com.project.t1_openSchool.model.task.Status;
import com.project.t1_openSchool.model.task.Task;
import com.project.t1_openSchool.repository.TaskRepository;
import com.project.t1_openSchool.utils.BeanUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for TaskService")
public class TaskServiceTest {

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

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("Test for getAllTasks")
    void getAllTasks() {
        List<Task> tasks = List.of(task);
        List<TaskDto> taskDtoList = List.of(taskDto);
        TaskDtoList response = new TaskDtoList();
        response.setTaskDtoList(taskDtoList);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.taskListToTaskDtoList(tasks)).thenReturn(response);

        TaskDtoList result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(taskDtoList.size(), result.getTaskDtoList().size());
        verify(taskRepository, times(1)).findAll();
        verify(taskMapper, times(1)).taskListToTaskDtoList(tasks);
    }

    @Test
    @DisplayName("Test with result TaskDto for getTaskById")
    public void getTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto taskDto = taskService.getTaskById(1L);

        assertNotNull(taskDto);
        assertEquals("Title task done", taskDto.getTitle());
        assertEquals(Status.DONE, taskDto.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskMapper, times(1)).taskToTaskDto(any());
    }

    @Test
    @DisplayName("Test throw exception while try to get task by id")
    public void getTaskByIdException() {
        assertThrows(RuntimeException.class,
                () -> taskService.getTaskById(1L));
    }

    @Test
    @DisplayName("Test to save task")
    public void saveTask() {
        when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.saveTask(taskDto);

        assertNotNull(result);
        assertEquals(taskDto.getTitle(), result.getTitle());
        assertEquals(taskDto.getStatus(), result.getStatus());
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).taskDtoToTask(taskDto);
        verify(taskMapper, times(1)).taskToTaskDto(task);
    }

    @Test
    @DisplayName("Test update task when task exist")
    public void updateTask() {
        TaskDto updateTaskDto = TaskDto.builder()
                .title("Title updated task done")
                .status(Status.DONE)
                .build();
        Task updateTask = Task.builder()
                .title("Title updated task done")
                .build();

        when(taskMapper.taskDtoToTask(updateTaskDto)).thenReturn(updateTask);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        BeanUtils.copyNonNullProperties(updateTask, task);
        task.setTitle("Title updated task done");
        taskDto.setTitle("Title updated task done");
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.updateTask(1L, updateTaskDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(updateTaskDto.getTitle(), result.getTitle());
        verify(taskMapper, times(1)).taskDtoToTask(updateTaskDto);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).taskToTaskDto(task);
    }

    @Test
    @DisplayName("Test not found task for update")
    public void updateTaskException() {
        assertThrows(RuntimeException.class,
                () -> taskService.updateTask(1L, taskDto));
    }

    @Test
    @DisplayName("Test deleteTask")
    public void deleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTaskById(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test not found task for update")
    public void deleteTaskException() {
        assertThrows(RuntimeException.class,
                () -> taskService.deleteTaskById(10L));
    }
}
