package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskHandler {
    void run(String pattern, String input, UUID taskId);

    Optional<Task> getTask(UUID id);

    List<Task> getTasks();
}