package com.company.controller;

import com.company.dto.TaskDTO;
import com.company.service.api.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.AnyTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public ResponseEntity<TaskDTO> showTaskById(@PathVariable long taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + pathToTaskService + "/tasks/" + taskId + " с токеном " + token);
        if (taskId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                TaskDTO taskDTO = taskService.showTaskById(taskId);
                log.info("С сервиса возвращается тело объекта: " + taskDTO);
                return ResponseEntity.ok(taskDTO);
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<TaskDTO>> showAllTasks(@RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + pathToTaskService + "/tasks");
        if (token != null) {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                List<TaskDTO> tasksDTO = taskService.showAllTasks();
                log.info("С сервиса возвращается тело объекта: " + tasksDTO);
                return ResponseEntity.ok(tasksDTO);
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("?date={date}")
    public ResponseEntity<List<TaskDTO>> showAllTasksAtDay(@RequestParam String date, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + pathToTaskService + "/tasks/?date=" + date);
        if (token != null) {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                try {
                    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    List<TaskDTO> tasksDTO = taskService.showAllTasksAtDay(localDate);
                    log.info("С сервиса возвращается тело объекта: " + tasksDTO);
                    return ResponseEntity.ok(tasksDTO);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

        @PostMapping("/")
    public ResponseEntity<AnyTypePattern> addTask(@RequestBody String task, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён POST-запрос по адресу " + pathToTaskService + "/tasks с телом " + task);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                taskService.addTask(task);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<AnyTypePattern> deleteTask(@PathVariable int taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён DELETE-запрос по адресу " + pathToTaskService + "/tasks/" + taskId);
        if (taskId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                taskService.deleteTask(taskId);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @PutMapping("change-task")
    public ResponseEntity<AnyTypePattern> updateTask(@RequestBody String task, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/change-task c task = " + task);
        if (task.equals("'") || task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                taskService.updateTask(task);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @PutMapping("change-date")
    public ResponseEntity<AnyTypePattern> rescheduleTask(@RequestBody @Validated String taskDTO, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/change-date с телом = " + taskDTO);
        if (token != null) {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                taskService.rescheduleTask(taskDTO);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("{taskId}/do")
    public ResponseEntity<AnyTypePattern> makeTaskDone(@PathVariable int taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/" + taskId + "/do");
        if (taskId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                taskService.makeTaskDone(taskId);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @PutMapping("{taskId}/undo")
    public ResponseEntity<AnyTypePattern> makeTaskUndone(@PathVariable int taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/" + taskId + "/undo");
        if (taskId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            ResponseEntity<?> responseEntity = taskService.isAllowedRequest(token);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Токен прошёл проверку");
                taskService.makeTaskUndone(taskId);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }
}
