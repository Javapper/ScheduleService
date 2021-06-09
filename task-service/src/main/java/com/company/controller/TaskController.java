package com.company.controller;

import com.company.service.api.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("tasks")
public class TaskController {

    private final TaskService taskService;
    private final String pathToTaskService = "http://task-service:8090";

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("{taskId}")
    public ResponseEntity<?> showTaskById(@PathVariable long taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + pathToTaskService + "/tasks/" + taskId + " с токеном " + token);
        if (taskId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                ResponseEntity<?> responseEntity1 = taskService.showTaskById(taskId);
                log.info("С сервиса возвращается ответ: " + responseEntity1);
                return ResponseEntity.status(responseEntity1.getStatusCode())
                        .body(responseEntity1.getBody());
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<?> showAllTasks(@RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + pathToTaskService + "/tasks");
        if (token != null) {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                ResponseEntity<?> responseEntity1 = taskService.showAllTasks();
                log.info("С сервиса возвращается тело объекта: " + responseEntity1);
                return ResponseEntity.status(responseEntity1.getStatusCode()).body(responseEntity1.getBody());
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("?date={date}")
    public ResponseEntity<?> showAllTasksAtDay(@RequestParam String date, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + pathToTaskService + "/tasks/?date=" + date);
        if (token != null) {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                try {
                    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    ResponseEntity<?> responseEntity1 = taskService.showAllTasksAtDay(localDate);
                    log.info("С сервиса возвращается тело объекта: " + responseEntity1);
                    return ResponseEntity.status(responseEntity1.getStatusCode()).body(responseEntity1.getBody());
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

        @PostMapping("/")
    public ResponseEntity<?> addTask(@RequestBody String task, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён POST-запрос по адресу " + pathToTaskService + "/tasks с телом " + task);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                ResponseEntity<?> responseEntity1 = taskService.addTask(task);
                return ResponseEntity.status(responseEntity1.getStatusCode()).body(responseEntity1.getBody());
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable int taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён DELETE-запрос по адресу " + pathToTaskService + "/tasks/" + taskId);
        if (taskId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                ResponseEntity<?> responseEntity1 = taskService.deleteTask(taskId);
                return ResponseEntity.status(responseEntity1.getStatusCode()).body(responseEntity1.getBody());
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @PutMapping("change-task")
    public ResponseEntity<?> updateTask(@RequestBody String task, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/change-task c task = " + task);
        if (task.equals("'") || task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                ResponseEntity<?> responseEntity1 = taskService.updateTask(task);
                return ResponseEntity.status(responseEntity1.getStatusCode()).body(responseEntity1.getBody());
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @PutMapping("change-date")
    public ResponseEntity<?> rescheduleTask(@RequestBody @Validated String taskDTO, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/change-date с телом = " + taskDTO);
        if (token != null) {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                ResponseEntity<?> responseEntity1 = taskService.rescheduleTask(taskDTO);
                return ResponseEntity.status(responseEntity1.getStatusCode()).body(responseEntity1.getBody());
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("{taskId}/do")
    public ResponseEntity<?> makeTaskDone(@PathVariable int taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/" + taskId + "/do");
        if (taskId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                ResponseEntity<?> responseEntity1 = taskService.makeTaskDone(taskId);
                return ResponseEntity.status(responseEntity1.getStatusCode()).body(responseEntity1.getBody());
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @PutMapping("{taskId}/undo")
    public ResponseEntity<?> makeTaskUndone(@PathVariable int taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/" + taskId + "/undo");
        if (taskId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                ResponseEntity<?> responseEntity1 = taskService.makeTaskUndone(taskId);
                return ResponseEntity.status(responseEntity1.getStatusCode()).body(responseEntity1.getBody());
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }
}
