package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.exception.ApiException;
import com.cqdTaskProcessing.CQD.exception.ErrorCode;
import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.rest.model.TaskResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static com.cqdTaskProcessing.CQD.exception.ErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;

    @Test
    void shouldCreateTask() {
        //when
        TaskResponse postResponse = taskService.create("BCD", "ABCD");

        //then
        assertThat(postResponse)
                .isNotNull()
                .extracting(TaskResponse::getTaskId)
                .isNotNull();
    }
    @Test
    void shouldReturned404_whenTaskNotExist() {
        //given
        UUID taskId = UUID.randomUUID();
        String expectedErrorMessage = String.format("Not Found task with id: %s", taskId);
        //when, then
        ApiException errorMessage = assertThrows(ApiException.class, () -> taskService.get(taskId.toString()));
        assertEquals(expectedErrorMessage, errorMessage.getMessage());
        assertEquals(NOT_FOUND, errorMessage.getCode());
    }

    @Test
    void shouldGetExistingTask() throws InterruptedException {
        //given
        TaskResponse postResponse = taskService.create("BCD", "ABCD");

        Thread.sleep(7000);

        //when
        Task responsedTask = taskService.get(postResponse.getTaskId());

        //then
        assertAll(
                () -> assertEquals(responsedTask.getId().toString(), postResponse.getTaskId()),
                () -> assertEquals(responsedTask.getResult().getTypos(), 0),
                () -> assertEquals(responsedTask.getResult().getPosition(), 1)
        );
    }

    @Test
    void shouldGetAllTasks() throws InterruptedException {
        int expectedTypos1 = 0;
        int expectedPosition1 = 1;
        int expectedTypos2 = 1;
        int expectedPosition2 = 1;
        //given
        TaskResponse postResponse1 = taskService.create("BCD", "ABCD");
        TaskResponse postResponse2 = taskService.create("BWD", "ABCD");

        Thread.sleep(7000);

        //when
        List<Task> responseTaskList = taskService.getAll();
        var returnedPostResponse1 = responseTaskList.stream()
                .filter(restTask -> restTask.getId().toString().equals(postResponse1.getTaskId())).findFirst();
        var returnedPostResponse2 = responseTaskList.stream()
                .filter(restTask -> restTask.getId().toString().equals(postResponse2.getTaskId())).findFirst();
        Task response1 = returnedPostResponse1.orElseThrow(() ->
                new ApiException(String.format("Not Found task with id: %s", postResponse1.getTaskId()), ErrorCode.NOT_FOUND)
        );
        Task response2 = returnedPostResponse2.orElseThrow(() ->
                new ApiException(String.format("Not Found task with id: %s", postResponse2.getTaskId()), ErrorCode.NOT_FOUND)
        );

        //then
        assertAll(
                () -> assertEquals(postResponse1.getTaskId(), response1.getId().toString()),
                () -> assertEquals(expectedTypos1, response1.getResult().getTypos()),
                () -> assertEquals(expectedPosition1, response1.getResult().getPosition()),
                () -> assertTrue(response1.getStatus().getProgress()>0),
                () -> assertEquals(postResponse2.getTaskId(), response2.getId().toString()),
                () -> assertEquals(expectedTypos2, response2.getResult().getTypos()),
                () -> assertEquals(expectedPosition2, response2.getResult().getPosition()),
                () -> assertTrue(response2.getStatus().getProgress()>0)
        );
    }
}