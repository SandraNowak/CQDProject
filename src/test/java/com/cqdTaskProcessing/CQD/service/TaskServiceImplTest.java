package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.model.Result;
import com.cqdTaskProcessing.CQD.model.Status;
import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.rest.model.RestTask;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;

    @Mock
    private TaskManager taskManager;

    @Test
    void shouldCreateTask() {
        //when
        String postResponse = taskService.create("BCD", "ABCD");

        //then
        assertTrue(!postResponse.isEmpty());
    }

//    @Test
//    void shouldReturned404_whenTaskNotExist() {
//        UUID taskId = UUID.randomUUID();
//        //when
//        String responseTask = taskService.get(taskId.toString());
//
//        //then
//        assertEquals(responseTask.getResult().getPosition(), 1);
//    }

    @Test
    void shouldGetTask() {



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
        this.taskManager.create("BCD", "ABCD", taskId);

        when(taskService.get(taskId.toString())).thenReturn(task);
        when(taskManager.getTaskById(taskId)).thenReturn(Optional.of(task));

        //when
        Task responseTask = taskService.get(taskId.toString());

        //then
        assertAll(
                () -> assertEquals(responseTask.getId(), taskId),
                () -> assertEquals(responseTask.getResult().getTypos(), 0),
                () -> assertEquals(responseTask.getResult().getPosition(), 1)
        );
    }

}