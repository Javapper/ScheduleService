package com.company.service.api;

import com.company.dto.TaskDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskDTO showTaskById(long taskId);

    List<TaskDTO> showAllTasks();

    List<TaskDTO> showAllTasksAtDay(LocalDate date);

    void addTask(String task) throws JsonProcessingException;

    void deleteTask(int taskId);

    void updateTask(String task) throws JsonProcessingException;

    void rescheduleTask(String task) throws JsonProcessingException;

    void makeTaskDone(int taskId);

    void makeTaskUndone(int taskId);

    boolean isAllowedRequest(String task) throws IOException, InterruptedException;


}
