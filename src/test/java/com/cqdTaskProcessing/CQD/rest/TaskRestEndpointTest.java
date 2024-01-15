package com.cqdTaskProcessing.CQD.rest;

import com.cqdTaskProcessing.CQD.exception.ApiException;
import com.cqdTaskProcessing.CQD.model.Result;
import com.cqdTaskProcessing.CQD.model.Status;
import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.service.TaskHandler;
import com.cqdTaskProcessing.CQD.service.TaskManager;
import com.cqdTaskProcessing.CQD.service.TaskManagerImpl;
import com.cqdTaskProcessing.CQD.service.TaskService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TaskRestEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    TaskService taskService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldCreateTask() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pattern", "BCD");
        params.add("input" , "ABCD");


        String response = mockMvc.perform(MockMvcRequestBuilders.post("/task").params(params))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn().getResponse().getContentAsString();
        assertTrue(!response.isEmpty());
    }

    @Test
    void shouldGetSimpleTask() throws Exception {
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

        when(taskService.get(taskId.toString())).thenReturn(task);
        //when
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/task/" + taskId))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void shouldReturned404_whenTaskNotExist() throws Exception {

        String exception = mockMvc.perform(MockMvcRequestBuilders.get("/task/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn().getResponse().getContentAsString();

        assertEquals(exception, "task with provided id not found");
    }
}