package com.cqdTaskProcessing.CQD.rest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private String taskId;
}