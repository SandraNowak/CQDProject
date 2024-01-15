package com.cqdTaskProcessing.CQD.rest;

import com.cqdTaskProcessing.CQD.exception.ApiException;
import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.rest.mapper.RestMapper;
import com.cqdTaskProcessing.CQD.rest.model.RestTask;
import com.cqdTaskProcessing.CQD.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<String> create(@RequestParam String pattern, @RequestParam String input) {
        return new ResponseEntity<>(taskService.create(input, pattern), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestTask> get(@PathVariable UUID id){
        return new ResponseEntity<>(mapper.toRestTask(taskService.get(id.toString())), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Task>> getAll() {
        return new ResponseEntity<>(taskService.getAll(), HttpStatus.OK);
    }

    @ExceptionHandler({ApiException.class})
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public String notFoundExceptionHandler() {
        return "task with provided id not found";
    }
}
