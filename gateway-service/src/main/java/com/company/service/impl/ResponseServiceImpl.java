package com.company.service.impl;

import com.company.dto.TaskDTO;
import com.company.dto.TokenDTO;
import com.company.service.api.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.AnyTypePattern;
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
import java.util.List;


@Slf4j
@Service
public class ResponseServiceImpl implements ResponseService {
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    private final String nameOfTaskService = "task-service";
    private final String nameOfNotificationService = "notification-service";

    private final String pathToTaskService = "http://task-service:8090";
    private final String pathToNotificationService = "http://notification-service:8091";
    private final String pathToAuthorizationService = "http://authorization-service:8092";

    @Autowired
    public ResponseServiceImpl(HttpClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<TaskDTO> showTaskById(long taskId) throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfTaskService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        HttpRequest request = HttpRequest.newBuilder().GET()
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToTaskService + "/tasks/"+ taskId))
                .build();
        log.info("Создался GET-запрос по адресу " + request.uri() + " с токеном " + tokenDTO);
        TaskDTO response = objectMapper.readValue(sendRequest(request).getBody(), TaskDTO.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<TaskDTO>> showAllTasks() throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfTaskService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        HttpRequest request = HttpRequest.newBuilder().GET()
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToTaskService + "/tasks"))
                .build();
        log.info("Создался GET-запрос по адресу " + request.uri() + " с токеном " + tokenDTO);
        List<TaskDTO> response = objectMapper.readValue(sendRequest(request).getBody(), List.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<TaskDTO>> showAllTasksAtDay(String dateStr) throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfTaskService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        HttpRequest request = HttpRequest.newBuilder().GET()
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToTaskService + "/tasks/?date="+ dateStr))
                .build();
        log.info("Создался GET-запрос по адресу " + request.uri() + " с токеном " + tokenDTO);
        List<TaskDTO> response = objectMapper.readValue(sendRequest(request).getBody(), List.class);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<AnyTypePattern> addTask(TaskDTO taskDTO) throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfTaskService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        String task = objectMapper.writeValueAsString(taskDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToTaskService + "/tasks/"))
                .build();
        log.info("Создался POST-запрос по адресу " + request.uri() +
                " с телом " + taskDTO.toString() +
                " с токеном " + tokenDTO);
        return sendRequestWithResponseAnyTypePattern(request);
    }

    public ResponseEntity<AnyTypePattern> deleteTask(int taskId) throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfTaskService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        HttpRequest request = HttpRequest.newBuilder().DELETE()
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToTaskService + "/tasks/"+ taskId))
                .build();
        log.info("Создался DELETE-запрос по адресу " + request.uri() + " с токеном " + tokenDTO);
        return sendRequestWithResponseAnyTypePattern(request);
    }

    public ResponseEntity<AnyTypePattern> updateTask(TaskDTO taskDTO) throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfTaskService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        String task = objectMapper.writeValueAsString(taskDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(task))
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToTaskService + "/tasks/change-task"))
                .build();
        log.info("Создался PUT-запрос по адресу " + request.uri() +
                " с task = " + taskDTO.getTask() +
                " с токеном " + tokenDTO);
        return sendRequestWithResponseAnyTypePattern(request);
    }

    public ResponseEntity<AnyTypePattern> rescheduleTask(TaskDTO taskDTO) throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfTaskService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        String task = objectMapper.writeValueAsString(taskDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(task))
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToTaskService + "/tasks/change-date"))
                .build();

        return sendRequestWithResponseAnyTypePattern(request);
    }

    public ResponseEntity<AnyTypePattern> makeTaskDone(int taskId) throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfTaskService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToTaskService + "/tasks/"
                        + taskId + "/do"))
                .build();
        log.info("Создался PUT-запрос по адресу " + request.uri() + " с токеном " + tokenDTO);
        logPutRequest(tokenDTO, request);
        return sendRequestWithResponseAnyTypePattern(request);
    }

    public ResponseEntity<AnyTypePattern> makeTaskUndone(int taskId) throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfTaskService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToTaskService + "/tasks/"
                        + taskId + "/undo"))
                .build();
        log.info("Создался PUT-запрос по адресу " + request.uri() + " с токеном " + tokenDTO);
        logPutRequest(tokenDTO, request);
        return sendRequestWithResponseAnyTypePattern(request);
    }

    public ResponseEntity<AnyTypePattern> startSendMessagesInTelegram() throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfNotificationService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToNotificationService + "/notification/turn-on"))
                .build();
        logPutRequest(tokenDTO, request);
        return sendRequestWithResponseAnyTypePattern(request);
    }

    public ResponseEntity<AnyTypePattern> stopSendMessagesInTelegram() throws IOException, InterruptedException {
        TokenDTO tokenDTO = createToken(nameOfNotificationService);
        String token = objectMapper.writeValueAsString(tokenDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(5))
                .header("token", token)
                .uri(URI.create(pathToNotificationService + "/turn-off"))
                .build();
        logPutRequest(tokenDTO, request);
        return sendRequestWithResponseAnyTypePattern(request);
    }

    private void logPutRequest(TokenDTO tokenDTO, HttpRequest request) {
        log.info("Создался PUT-запрос по адресу " + request.uri() + " с токеном " + tokenDTO);
    }

    private ResponseEntity<AnyTypePattern> sendRequestWithResponseAnyTypePattern(HttpRequest request) throws IOException, InterruptedException {
        ResponseEntity<String> response = sendRequest(request);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    private TokenDTO createToken(String serviceTo) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(5))
                .uri(URI.create(pathToAuthorizationService + "/tokens/create-token-from-gateway-service-to-" + serviceTo))
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Создался токен: " + response.body());
        if (response.body() != null) {
            return objectMapper.readValue(response.body(), TokenDTO.class);
        }
        return null;
    }

    public ResponseEntity<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        log.info("Отправлен запрос " + request.toString());
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Код ответа: " + response.statusCode() + ", тело ответа: " + response.body() );
        return ResponseEntity.ok(response.body());
    }
}
