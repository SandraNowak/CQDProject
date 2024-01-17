package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.exception.ApiException;
import com.cqdTaskProcessing.CQD.exception.ErrorCode;
import com.cqdTaskProcessing.CQD.model.Result;
import com.cqdTaskProcessing.CQD.model.Status;
import com.cqdTaskProcessing.CQD.model.Task;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Scheduled(cron = "1*****")
    private void updateTask() {
        for (Thread thread:threads) {
            if (thread.isAlive()) {
                updateTask(thread); //here are should fetch task from Thread
            } else {
                updateTask(thread); //here are should fetch task from Thread
                threads.remove(thread);
            }
        }
    }

    private void updateTask(Task threadTask) {
        Optional<Task> optionalTask = tasks.stream()
                .filter(task -> task.getId().equals(threadTask.getId()))
                .findFirst();
        Task taskToUpdate = optionalTask.orElseThrow(
                () -> new ApiException(String.format("Not Found task with id: %s", threadTask.getId()), ErrorCode.NOT_FOUND)
        );
        taskToUpdate.setResult(updateResult(threadTask.getResult()));
        taskToUpdate.setStatus(updateStatus(threadTask.getStatus()));
        tasks.add(taskToUpdate);
    }

    private Result updateResult(Result result) {
        return Result.builder()
                .position(result.getPosition())
                .typos(result.getTypos())
                .build();
    }

    private Status updateStatus(Status status) {
        return Status.builder()
                .progress(status.getProgress())
                .build();
    }
}
