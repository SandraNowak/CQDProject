package com.cqdTaskProcessing.CQD.rest;

import com.cqdTaskProcessing.CQD.exception.ApiException;
import com.cqdTaskProcessing.CQD.rest.mapper.RestMapper;
import com.cqdTaskProcessing.CQD.rest.model.RestTask;
import com.cqdTaskProcessing.CQD.rest.model.TaskPayload;
import com.cqdTaskProcessing.CQD.rest.model.TaskResponse;
import com.cqdTaskProcessing.CQD.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskRestEndpoint {
    private final TaskService taskService;
    private final RestMapper mapper;

    public TaskRestEndpoint(TaskService taskService, RestMapper mapper) {
        this.taskService = taskService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskPayload taskPayload) {
        return new ResponseEntity<>(taskService.create(taskPayload.getPattern(), taskPayload.getInput()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestTask> get(@PathVariable String id){
        return new ResponseEntity<>(mapper.toRestTask(taskService.get(id)), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RestTask>> getAll() {
        return new ResponseEntity<>(mapper.toRestTaskList(taskService.getAll()), HttpStatus.OK);
    }

    @ExceptionHandler({ApiException.class})
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public String notFoundExceptionHandler() {
        return "task with provided id not found";
    }
}
