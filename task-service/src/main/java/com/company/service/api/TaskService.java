package com.company.service.api;

import com.company.dto.TaskDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    ResponseEntity<TaskDTO> showTaskById(long taskId);

    ResponseEntity<List<TaskDTO>> showAllTasks();

    ResponseEntity<List<TaskDTO>> showAllTasksAtDay(LocalDate date);

    ResponseEntity<?> addTask(String task) throws JsonProcessingException;

    ResponseEntity<?> deleteTask(int taskId);

    ResponseEntity<?> updateTask(String task) throws JsonProcessingException;

    ResponseEntity<?> rescheduleTask(String task) throws JsonProcessingException;

    ResponseEntity<?> makeTaskDone(int taskId);

    ResponseEntity<?> makeTaskUndone(int taskId);

    ResponseEntity<?> isAllowedRequest(String task) throws IOException, InterruptedException;


}
