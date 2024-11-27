package com.project.t1_openSchool.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskDtoList {

    private List<TaskDto> taskDtoList = new ArrayList<>();
}
