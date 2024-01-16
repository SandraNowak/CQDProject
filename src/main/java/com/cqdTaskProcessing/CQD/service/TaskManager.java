package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskManager {
    void create(String pattern, String input, UUID taskId);
    Optional<Task> getTaskById(UUID id);
    List<Task> getTasks();
}
