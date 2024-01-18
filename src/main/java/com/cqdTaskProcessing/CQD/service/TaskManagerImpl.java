package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TaskManagerImpl implements TaskManager {

    @Autowired
    private TaskHandler taskHandler;

    @Override
    @Async
    public void create(String pattern, String input, UUID taskId) {
        taskHandler.run(pattern, input, taskId);
    }

    @Override
    public Optional<Task> getTaskById(UUID id) {
        return taskHandler.getTask(id);
    }

    public List<Task> getTasks() {
       return taskHandler.getTasks();
    }

}