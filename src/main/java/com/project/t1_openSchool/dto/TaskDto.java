package com.project.t1_openSchool.dto;

import com.project.t1_openSchool.model.Status;
import lombok.Data;

@Data
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private Status status;

    private Long userId;

}
