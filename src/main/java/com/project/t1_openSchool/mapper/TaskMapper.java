package com.project.t1_openSchool.mapper;

import com.project.t1_openSchool.dto.TaskDto;
import com.project.t1_openSchool.dto.TaskDtoList;
import com.project.t1_openSchool.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task taskDtoToTask(TaskDto taskDto);

    TaskDto taskToTaskDto(Task task);

    default TaskDtoList taskListToTaskDtoList(List<Task> taskList) {
        TaskDtoList taskDtoList = new TaskDtoList();
        taskDtoList.setTaskDtoList(taskList.stream().map(this::taskToTaskDto)
                .collect(Collectors.toList()));
        return taskDtoList;
    }
}
