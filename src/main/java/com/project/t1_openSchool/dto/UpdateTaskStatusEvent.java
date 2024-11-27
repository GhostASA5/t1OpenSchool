package com.project.t1_openSchool.dto;

import com.project.t1_openSchool.model.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTaskStatusEvent {

    private Long taskId;

    private Status status;
}
