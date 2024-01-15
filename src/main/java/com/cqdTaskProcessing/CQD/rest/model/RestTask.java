package com.cqdTaskProcessing.CQD.rest.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RestTask {
    private String taskId;
    private int status;
    private int position;
    private int typos;
}