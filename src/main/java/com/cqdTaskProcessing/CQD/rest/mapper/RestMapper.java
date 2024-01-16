package com.cqdTaskProcessing.CQD.rest.mapper;

import com.cqdTaskProcessing.CQD.model.Result;
import com.cqdTaskProcessing.CQD.model.Status;
import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.rest.model.RestTask;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class RestMapper {

    public RestTask toRestTask(Task task) {
        return RestTask.builder()
                .taskId(task.getId().toString())
                .position(task.getResult().getPosition())
                .typos(task.getResult().getTypos())
                .status(task.getStatus().getProgress())
                .build();
    }

    public Task toTask(RestTask restTask) {
        return Task.builder()
                .id(UUID.fromString(restTask.getTaskId()))
                .status(Status.builder()
                        .progress(restTask.getStatus())
                        .build())
                .result(Result.builder()
                        .position(restTask.getPosition())
                        .typos(restTask.getTypos())
                        .build())
                .build();
    }

    public List<RestTask> toRestTaskList(List<Task> taskList) {
        List<RestTask> restTaskList =  new ArrayList<>();
        for (Task task:taskList) {
            restTaskList.add(toRestTask(task));
        }
        return restTaskList;
    }
}
