package com.cqdTaskProcessing.CQD.domain;

import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.service.TaskHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskHandlerTest {

    @ParameterizedTest
    @MethodSource("typosData")
    public void shouldFoundTypo(String input, String pattern,UUID taskId ,int expectedTypos, int expectedPosition) {

        TaskHandler taskCreator = new TaskHandler(pattern, input, taskId);
        taskCreator.run();
        Task task = taskCreator.getTask();

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