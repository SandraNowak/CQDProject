package com.cqdTaskProcessing.CQD.domain;

import com.cqdTaskProcessing.CQD.exception.ApiException;
import com.cqdTaskProcessing.CQD.exception.ErrorCode;
import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.service.TaskHandler;
import com.cqdTaskProcessing.CQD.service.TaskHandlerImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskHandlerImplTest {

    @Autowired
    TaskHandler taskCreator;

    @ParameterizedTest
    @MethodSource("typosData")
    public void shouldFoundTypo(String input, String pattern,UUID taskId ,int expectedTypos, int expectedPosition) {

        taskCreator.run(pattern, input, taskId);
        Optional<Task> optionalTask = taskCreator.getTask(taskId);
        Task task = optionalTask.orElseThrow(() ->
                new ApiException(String.format("Not Found task with id: %s", taskId), ErrorCode.NOT_FOUND)
        );

        //then
        assertAll(
                () -> assertEquals(task.getResult().getTypos(), expectedTypos),
                () -> assertEquals(task.getResult().getPosition(), expectedPosition)
        );
    }

    private static Stream<Arguments> typosData() {
        return Stream.of(
                Arguments.of("ABCD", "BCD", UUID.randomUUID(), 0, 1),
                Arguments.of("ABCD", "BWD", UUID.randomUUID(), 1, 1),
                Arguments.of("ABCDEFG", "CFG", UUID.randomUUID(), 1, 4),
                Arguments.of("ABCABC", "ABC", UUID.randomUUID(), 0, 0),
                Arguments.of("ABCDEFG", "TDD", UUID.randomUUID(), 2, 1)
        );
    }
}