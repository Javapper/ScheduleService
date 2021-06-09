package com.company.service.impl;

import com.company.dto.TaskDTO;
import com.company.entity.TaskEntity;
import com.company.mapper.TaskMapper;
import com.company.service.api.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final String pathToAuthorizationService = "http://authorization-service:8092";
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final TaskMapper taskMapper;
    private final HttpClient client;


    @Autowired
    public TaskServiceImpl(ObjectMapper objectMapper, ModelMapper modelMapper, TaskMapper taskMapper, HttpClient client) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.taskMapper = taskMapper;
        this.client = client;
    }

    @Override
    public TaskDTO showTaskById(long taskId) {
        log.info("Запрос в БД на показ задания с id = " + taskId);
        return modelMapper.map(taskMapper.showTaskById(taskId), TaskDTO.class);
    }

    @Override
    public List<TaskDTO> showAllTasks() {
        log.info("Запрос в БД на показ всех заданий");
        return taskMapper.showAllTasks().stream()
                .map(taskEntity -> modelMapper.map(taskEntity, TaskDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> showAllTasksAtDay(LocalDate date) {
        log.info("Запрос в БД на показ всех заданий с date = " + date);
        return taskMapper.showAllTasksAtDay(date).stream()
                .map(taskEntity -> modelMapper.map(taskEntity, TaskDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addTask(String task) throws JsonProcessingException {
        TaskDTO taskDTO = objectMapper.readValue(task, TaskDTO.class);
        log.info("Запрос в БД на добавления задания с task = {} и date = {}", taskDTO.getTask(), taskDTO.getDate());
        taskMapper.addTask(modelMapper.map(taskDTO, TaskEntity.class));
    }

    @Override
    public void deleteTask(int taskId) {
        log.info("Запрос в БД на удаление задания с id = " + taskId);
        taskMapper.deleteTask(taskId);
    }

    @Override
    public void updateTask(String task) throws JsonProcessingException {
        TaskDTO taskDTO = objectMapper.readValue(task, TaskDTO.class);
        log.info("Запрос в БД на обновление task = {} у задания с id = {}", taskDTO.getTask(), taskDTO.getTaskId());
        taskMapper.updateTask(modelMapper.map(taskDTO, TaskEntity.class));
    }

    @Override
    public void rescheduleTask(String task) throws JsonProcessingException {
        TaskDTO taskDTO = objectMapper.readValue(task, TaskDTO.class);
        log.info("Запрос в БД на обновление date = {} у задания с id = {}", taskDTO.getDate(), taskDTO.getTaskId());
        taskMapper.rescheduleTask(modelMapper.map(taskDTO, TaskEntity.class));
    }

    @Override
    public void makeTaskDone(int taskId) {
        log.info("Запрос в БД на обновление isDone = true у задания с id = " + taskId);
        taskMapper.makeTaskDone(taskId);
    }

    @Override
    public void makeTaskUndone(int taskId) {
        log.info("Запрос в БД на обновление isDone = false у задания с id = " + taskId);
        taskMapper.makeTaskUndone(taskId);
    }

    public ResponseEntity<?> isAllowedRequest(String token) throws IOException, InterruptedException {
        log.info("Запрос на сервер авторизации для проверки токена");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(token))
                    .timeout(Duration.ofSeconds(5))
                    .uri(URI.create(pathToAuthorizationService + "/tokens/check-token"))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Получен результат от сервера аторизации: " + response.statusCode());
            if (response.statusCode() == 200) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
