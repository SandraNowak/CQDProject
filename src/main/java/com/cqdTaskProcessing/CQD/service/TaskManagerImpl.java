package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.model.Task;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TaskManagerImpl implements TaskManager {
    private Set<Task> tasks = new HashSet<>();

    @Override
    @Async
    public void create(String pattern, String input, UUID taskId) {
        TaskHandler taskHandler = new TaskHandler(pattern, input, taskId);
        taskHandler.run();

        tasks.add(taskHandler.getTask());
    }

    @Override
    public Optional<Task> getTaskById(UUID id) {
        return tasks
                .stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    public List<Task> getTasks() {
        return tasks
                .stream()
                .toList();
    };
}
