package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.exception.ApiException;
import com.cqdTaskProcessing.CQD.exception.ErrorCode;
import com.cqdTaskProcessing.CQD.model.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskManager taskManager;

    public TaskServiceImpl(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public String create(String pattern, String input){
        UUID taskId = UUID.randomUUID();
        taskManager.create(pattern, input, taskId);
        return taskId.toString();
    };

    @Override
    public Task get(String id) {
        Optional<Task> taskOptional = taskManager.getTaskById(UUID.fromString(id));
        return taskOptional.orElseThrow(() ->
                new ApiException(String.format("Not Found task with id: %s", id), ErrorCode.NOT_FOUND)
        );
    };

    @Override
    public List<Task> getAll() {
        return taskManager.getTasks();
    }
}
