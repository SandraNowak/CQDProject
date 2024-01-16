package com.cqdTaskProcessing.CQD.rest.mapper;

import com.cqdTaskProcessing.CQD.model.Result;
import com.cqdTaskProcessing.CQD.model.Status;
import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.rest.model.RestTask;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestMapperTest {

    private final RestMapper mapper = new RestMapper();

    @Test
    void shouldMapTaskToRestTask() {
        //given
        UUID taskId = UUID.randomUUID();
        Task task = Task.builder()
                .id(taskId)
                .result(Result.builder()
                        .typos(1)
                        .position(2)
                        .build())
                .status(Status.builder()
                        .progress(47)
                        .build())
                .build();
        //when
        RestTask mappedRestTask = mapper.toRestTask(task);

        //then
        assertAll(
                () -> assertEquals(task.getId().toString(), mappedRestTask.getTaskId()),
                () -> assertEquals(task.getResult().getTypos(), mappedRestTask.getTypos()),
                () -> assertEquals(task.getResult().getPosition(), mappedRestTask.getPosition()),
                () -> assertEquals(task.getStatus().getProgress(), mappedRestTask.getStatus())
        );
    }

    @Test
    void shouldMapRestTaskToTask() {
        //given
        UUID taskId = UUID.randomUUID();
        RestTask restTask = RestTask.builder()
                .taskId(taskId.toString())
                .typos(2)
                .position(0)
                .status(67)
                .build();
        //when
        Task mappedTask = mapper.toTask(restTask);

        //then
        assertAll(
                () -> assertEquals(restTask.getTaskId(), mappedTask.getId().toString()),
                () -> assertEquals(restTask.getTypos(), mappedTask.getResult().getTypos()),
                () -> assertEquals(restTask.getPosition(), mappedTask.getResult().getPosition()),
                () -> assertEquals(restTask.getStatus(), mappedTask.getStatus().getProgress())
        );
    }
    @Test
    void shouldMapRestTaskListToTaskList() {
        //given
        UUID taskId1 = UUID.randomUUID();
        UUID taskId2 = UUID.randomUUID();
        Task task1 = Task.builder()
                .id(taskId1)
                .result(Result.builder()
                        .typos(1)
                        .position(2)
                        .build())
                .status(Status.builder()
                        .progress(47)
                        .build())
                .build();
        Task task2 = Task.builder()
                .id(taskId2)
                .result(Result.builder()
                        .typos(0)
                        .position(0)
                        .build())
                .status(Status.builder()
                        .progress(5)
                        .build())
                .build();
        //when
        List<RestTask> mappedTask = mapper.toRestTaskList(List.of(task1, task2));

        //then
        assertAll(
                () -> assertEquals(task1.getId().toString(), mappedTask.get(0).getTaskId()),
                () -> assertEquals(task1.getResult().getTypos(), mappedTask.get(0).getTypos()),
                () -> assertEquals(task1.getResult().getPosition(), mappedTask.get(0).getPosition()),
                () -> assertEquals(task1.getStatus().getProgress(), mappedTask.get(0).getStatus()),
                () -> assertEquals(task2.getId().toString(), mappedTask.get(1).getTaskId()),
                () -> assertEquals(task2.getResult().getTypos(), mappedTask.get(1).getTypos()),
                () -> assertEquals(task2.getResult().getPosition(), mappedTask.get(1).getPosition()),
                () -> assertEquals(task2.getStatus().getProgress(), mappedTask.get(1).getStatus())
        );
    }
}