package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.model.Result;
import com.cqdTaskProcessing.CQD.model.Status;
import com.cqdTaskProcessing.CQD.model.Task;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class TaskHandlerImpl implements TaskHandler {

    private Set<Task> tasks;

    @PostConstruct
    private void postConstruct() {
        tasks = new HashSet<>();
    }

    @Override
    public void run(String pattern, String input, UUID taskId) {
        processTask(input, pattern, taskId);
    }

    @Override
    public Optional<Task> getTask(UUID id) {
        return tasks
                .stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Task> getTasks() {
        return tasks
                .stream()
                .toList();
    }

    public void processTask(String input, String pattern, UUID taskId) {
        int bestPosition = -1;
        int bestTypos = Integer.MAX_VALUE;

        for (int i = 0; i <= input.length() - pattern.length(); i++) {
            int typos = countTypos(input.substring(i, i + pattern.length()), pattern);

            if (typos < bestTypos || (typos == bestTypos && i < bestPosition)) {
                bestPosition = i;
                bestTypos = typos;
            }

            int progress = ((i+1) * 100) / (input.length() - pattern.length() + 1);

            updateTask(Result.builder()
                    .position(bestPosition)
                    .typos(bestTypos)
                    .build(), taskId, progress);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateTask(Result result, UUID taskId, int progress) {
        Task task = tasks.stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElse(Task.builder().id(taskId)
                        .result(result)
                        .status(Status.builder().progress(progress)
                                .build())
                        .build());

        task.setResult(result);
        task.setStatus(Status.builder()
                .progress(progress)
                .build());
        tasks.remove(task);
        tasks.add(task);
    }

    private int countTypos(String input, String pattern) {
        int typos = 0;

        for (int i = 0; i < pattern.length(); i++) {
            if (input.charAt(i) != pattern.charAt(i)) {
                typos++;
            }
        }

        return typos;
    }
}
