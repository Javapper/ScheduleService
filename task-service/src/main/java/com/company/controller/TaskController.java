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
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            TaskDTO taskDTO = taskService.showTaskById(taskId);
            log.info("С сервиса возвращается тело объекта: " + taskDTO);
            return ResponseEntity.ok(taskDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @GetMapping("")
    public ResponseEntity<List<TaskDTO>> showAllTasks(@RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + pathToTaskService + "/tasks/all");
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            List<TaskDTO> tasksDTO = taskService.showAllTasks();
            log.info("С сервиса возвращается тело объекта: " + tasksDTO);
            return ResponseEntity.ok(tasksDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();

    }

    @GetMapping("?date={date}")
    public ResponseEntity<List<TaskDTO>> showAllTasksAtDay(@RequestParam String dateStr, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + pathToTaskService + "/tasks/?date=" + dateStr);
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<TaskDTO> tasksDTO = taskService.showAllTasksAtDay(date);
            log.info("С сервиса возвращается тело объекта: " + tasksDTO);
            return ResponseEntity.ok(tasksDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();

    }

    @PostMapping("/")
    public ResponseEntity<AnyTypePattern> addTask(@RequestBody String task, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён POST-запрос по адресу " + pathToTaskService + "/tasks с телом " + task);
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            taskService.addTask(task);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();

    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<AnyTypePattern> deleteTask(@PathVariable int taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён DELETE-запрос по адресу " + pathToTaskService + "/tasks/" + taskId);
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            taskService.deleteTask(taskId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @PutMapping("change-task")
    public ResponseEntity<AnyTypePattern> updateTask(@RequestBody String task, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/change-task c task = " + task);
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            taskService.updateTask(task);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @PutMapping("change-date")
    public ResponseEntity<AnyTypePattern> rescheduleTask(@RequestBody @Validated String date, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/change-date с date = " + date);
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            taskService.rescheduleTask(date);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @PutMapping("{taskId}/do")
    public ResponseEntity<AnyTypePattern> makeTaskDone(@PathVariable int taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/" + taskId + "/do");
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            taskService.makeTaskDone(taskId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @PutMapping("{taskId}/undo")
    public ResponseEntity<AnyTypePattern> makeTaskUndone(@PathVariable int taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + pathToTaskService + "/tasks/" + taskId + "/undo");
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            taskService.makeTaskUndone(taskId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
}
