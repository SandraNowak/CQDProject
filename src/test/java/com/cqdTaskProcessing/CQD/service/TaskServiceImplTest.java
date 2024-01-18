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
        int progress = 0;
        Task responsedTask;
        //given
        TaskResponse postResponse = taskService.create("BCD", "ABCD");

        Thread.sleep(1700);
        //when
        do {
            responsedTask = taskService.get(postResponse.getTaskId());
            progress = responsedTask.getStatus().getProgress();
        } while(progress!=100);

        //then
        Task finalResponsedTask = responsedTask;
        assertAll(
                () -> assertEquals(finalResponsedTask.getId().toString(), postResponse.getTaskId()),
                () -> assertEquals(0, finalResponsedTask.getResult().getTypos()),
                () -> assertEquals(1, finalResponsedTask.getResult().getPosition()),
                () -> assertEquals(100, finalResponsedTask.getStatus().getProgress())
        );
    }

    @Test
    void shouldGetAllTasks() throws InterruptedException {
        int progress1 = 0;
        int progress2 = 0;
        List<Task> responseTaskList;
        Task response1;
        Task response2;
        int expectedTypos1 = 0;
        int expectedPosition1 = 1;
        int expectedTypos2 = 1;
        int expectedPosition2 = 1;
        //given
        TaskResponse postResponse1 = taskService.create("BCD", "ABCD");
        TaskResponse postResponse2 = taskService.create("BWD", "ABCD");

        Thread.sleep(7000);

        //when
        do{
        responseTaskList = taskService.getAll();
        var returnedPostResponse1 = responseTaskList.stream()
                .filter(restTask -> restTask.getId().toString().equals(postResponse1.getTaskId())).findFirst();
        var returnedPostResponse2 = responseTaskList.stream()
                .filter(restTask -> restTask.getId().toString().equals(postResponse2.getTaskId())).findFirst();
        response1 = returnedPostResponse1.orElseThrow(() ->
                new ApiException(String.format("Not Found task with id: %s", postResponse1.getTaskId()), ErrorCode.NOT_FOUND)
        );
        progress1 = response1.getStatus().getProgress();
        response2 = returnedPostResponse2.orElseThrow(() ->
                new ApiException(String.format("Not Found task with id: %s", postResponse2.getTaskId()), ErrorCode.NOT_FOUND)
        );
        progress2 = response2.getStatus().getProgress();
        } while(progress1!=100 && progress2!=100);

        //then
        Task finalResponse = response1;
        Task finalResponse1 = response2;
        assertAll(
                () -> assertEquals(postResponse1.getTaskId(), finalResponse.getId().toString()),
                () -> assertEquals(expectedTypos1, finalResponse.getResult().getTypos()),
                () -> assertEquals(expectedPosition1, finalResponse.getResult().getPosition()),
                () -> assertEquals(100, finalResponse.getStatus().getProgress()),
                () -> assertEquals(postResponse2.getTaskId(), finalResponse1.getId().toString()),
                () -> assertEquals(expectedTypos2, finalResponse1.getResult().getTypos()),
                () -> assertEquals(expectedPosition2, finalResponse1.getResult().getPosition()),
                () -> assertEquals(100, finalResponse1.getStatus().getProgress())
        );
    }
}