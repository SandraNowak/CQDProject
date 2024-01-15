package com.cqdTaskProcessing.CQD.rest.mapper;

import com.cqdTaskProcessing.CQD.model.Result;
import com.cqdTaskProcessing.CQD.model.Status;
import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.rest.model.RestTask;
import org.junit.jupiter.api.Test;

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
}