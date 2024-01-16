package com.cqdTaskProcessing.CQD.service;

import com.cqdTaskProcessing.CQD.model.Task;
import com.cqdTaskProcessing.CQD.rest.model.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    TaskResponse create(String pattern, String input);
    Task get(String id);
    List<Task> getAll();
}
