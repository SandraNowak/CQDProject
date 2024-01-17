package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.model.Result;
import com.cqdTaskProcessing.CQD.model.Status;
import com.cqdTaskProcessing.CQD.model.Task;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskHandler implements Future {

    private final String pattern;
    private final String input;

    private int progress;
    private final UUID taskId;

    @Getter
    private Task task;

    public TaskHandler(String pattern, String input, UUID taskId) {
        this.pattern = pattern;
        this.input = input;
        this.taskId = taskId;
    }

    public void run() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Result result = bestPosition(input, pattern);

        buildTask(result);
    }

    public Result bestPosition(String input, String pattern) {
        int bestPosition = -1;
        int bestTypos = Integer.MAX_VALUE;

        for (int i = 0; i <= input.length() - pattern.length(); i++) {
            int typos = countTypos(input.substring(i, i + pattern.length()), pattern);

            if (typos < bestTypos || (typos == bestTypos && i < bestPosition)) {
                bestPosition = i;
                bestTypos = typos;
            }

            progress = (i * 100) / (input.length() - pattern.length() + 1);
        }
        return Result.builder()
                .position(bestPosition)
                .typos(bestTypos)
                .build();
    }

    private void buildTask(Result result) {
        task = Task.builder()
                .id(taskId)
                .result(result)
                .status(Status.builder()
                        .progress(progress)
                        .build())
                .build();
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

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return progress == 100;
    }

    @Override
    public Task get() throws InterruptedException, ExecutionException {
        return task;
    }

    @Override
    public Task get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return task;
    }
}
