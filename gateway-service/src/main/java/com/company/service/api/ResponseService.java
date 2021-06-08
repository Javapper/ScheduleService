package com.company.service.api;

import com.company.dto.TaskDTO;
import org.aspectj.weaver.patterns.AnyTypePattern;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;


public interface ResponseService {
    ResponseEntity<TaskDTO> showTaskById(long taskId) throws IOException, InterruptedException;

    ResponseEntity<List<TaskDTO>> showAllTasks() throws IOException, InterruptedException;

    ResponseEntity<List<TaskDTO>> showAllTasksAtDay(String dateStr) throws IOException, InterruptedException;

    ResponseEntity<AnyTypePattern> addTask(TaskDTO taskDTO) throws IOException, InterruptedException;

    ResponseEntity<AnyTypePattern> deleteTask(int taskId) throws IOException, InterruptedException;

    ResponseEntity<AnyTypePattern> updateTask(TaskDTO taskDTO) throws IOException, InterruptedException;

    ResponseEntity<AnyTypePattern> rescheduleTask(TaskDTO taskDTO) throws IOException, InterruptedException;

    ResponseEntity<AnyTypePattern> makeTaskDone(int taskId) throws IOException, InterruptedException;

    ResponseEntity<AnyTypePattern> makeTaskUndone(int taskId) throws IOException, InterruptedException;

    ResponseEntity<AnyTypePattern> startSendMessagesInTelegram() throws IOException, InterruptedException;

    ResponseEntity<AnyTypePattern> stopSendMessagesInTelegram() throws IOException, InterruptedException;
}
