package com.cqdTaskProcessing.CQD.rest;

import com.cqdTaskProcessing.CQD.exception.ApiException;
import com.cqdTaskProcessing.CQD.exception.ErrorCode;
import com.cqdTaskProcessing.CQD.rest.model.RestTask;
import com.cqdTaskProcessing.CQD.rest.model.TaskPayload;
import com.cqdTaskProcessing.CQD.rest.model.TaskResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class TaskRestEndpointTest {

    private static final String API_URL = "/task";
    private static final String GET_ALL = "/list";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldCreateTask() throws Exception {

        String createResponse = mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                        .content(objectMapper.writeValueAsString(TaskPayload.builder()
                                .input("ABCD")
                                .pattern("BCD")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Thread.sleep(5000);

        Gson gson = new Gson();
        TaskResponse taskResponse = gson.fromJson(createResponse, TaskResponse.class);
        assertFalse(taskResponse.getTaskId().isEmpty());
    }

    @Test
    void shouldGetSimpleTask() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        int expectedTypos = 0;
        int expectedPosition = 1;

        String createResponse = mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                        .content(objectMapper.writeValueAsString(TaskPayload.builder()
                                .input("ABCD")
                                .pattern("BCD")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Thread.sleep(5000);

        Gson gson = new Gson();
        TaskResponse taskResponse = gson.fromJson(createResponse, TaskResponse.class);

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/" + taskResponse.getTaskId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId", Matchers.is(taskResponse.getTaskId())))
                .andExpect(jsonPath("$.status", Matchers.not(0)))
                .andExpect(jsonPath("$.position", Matchers.is(expectedPosition)))
                .andExpect(jsonPath("$.typos", Matchers.is(expectedTypos)));
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        int expectedTypos1 = 0;
        int expectedPosition1= 1;
        int expectedTypos2 = 1;
        int expectedPosition2= 4;

        String createResponse1 = mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                        .content(objectMapper.writeValueAsString(TaskPayload.builder()
                                .input("ABCD")
                                .pattern("BCD")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String createResponse2 = mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                        .content(objectMapper.writeValueAsString(TaskPayload.builder()
                                .input("ABCDEFG")
                                .pattern("CFG")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Thread.sleep(5000);

        Gson gson = new Gson();
        TaskResponse taskResponse1 = gson.fromJson(createResponse1, TaskResponse.class);
        TaskResponse taskResponse2 = gson.fromJson(createResponse2, TaskResponse.class);

        String responseBody = mockMvc.perform(MockMvcRequestBuilders.get(API_URL + GET_ALL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Gson gson2 = new Gson();
        var list = gson2.fromJson(responseBody, RestTask[].class);
        var returnedResponse1 = Arrays.stream(list)
                .filter(restTask -> restTask.getTaskId().equals(taskResponse1.getTaskId())).findFirst();
        var returnedResponse2 = Arrays.stream(list)
                .filter(restTask -> restTask.getTaskId().equals(taskResponse2.getTaskId())).findFirst();
        RestTask response1 = returnedResponse1.orElseThrow(() ->
                new ApiException(String.format("Not Found task with id: %s", taskResponse1.getTaskId()), ErrorCode.NOT_FOUND)
        );
        RestTask response2 = returnedResponse2.orElseThrow(() ->
                new ApiException(String.format("Not Found task with id: %s", taskResponse2.getTaskId()), ErrorCode.NOT_FOUND)
        );

        assertAll(
                () -> assertEquals(taskResponse1.getTaskId(), response1.getTaskId()),
                () -> assertEquals(expectedPosition1, response1.getPosition()),
                () -> assertEquals(expectedTypos1, response1.getTypos()),
                () -> assertTrue(response1.getStatus()>0),
                () -> assertEquals(taskResponse2.getTaskId(), response2.getTaskId()),
                () -> assertEquals(expectedPosition2, response2.getPosition()),
                () -> assertEquals(expectedTypos2, response2.getTypos()),
                () -> assertTrue(response2.getStatus()>0)
        );
    }

    @Test
    void shouldReturned404_whenTaskNotExist() throws Exception {

        String exception = mockMvc.perform(MockMvcRequestBuilders.get("/task/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn().getResponse().getContentAsString();

        assertEquals(exception, "task with provided id not found");
    }
}