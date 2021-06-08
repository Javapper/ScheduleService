package com.company.controller;

import com.company.dto.TaskDTO;
import com.company.service.api.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final String servicePath = "http://localhost:8090/tasks";

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("id-{taskId}")
    public ResponseEntity<TaskDTO> showTaskById(@PathVariable long taskId, @RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + servicePath + "/id-" + taskId + " с токеном " + token);
        if (taskService.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            TaskDTO taskDTO = taskService.showTaskById(taskId);
            log.info("С сервиса возвращается тело объекта: " + taskDTO);
            return ResponseEntity.ok(taskDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @GetMapping("all")
    public ResponseEntity<List<TaskDTO>> showAllTasks() {
        log.info("Произведён GET-запрос по адресу " + servicePath + "/all");
        return ResponseEntity.ok(taskService.showAllTasks());
    }

    @GetMapping("date-{dateStr}")
    public ResponseEntity<List<TaskDTO>> showAllTasksAtDay(@PathVariable String dateStr) {
        log.info("Произведён GET-запрос по адресу " + servicePath + "/date-" + dateStr);
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return ResponseEntity.ok(taskService.showAllTasksAtDay(date));
    }

    @PostMapping("/")
    public ResponseEntity<AnyTypePattern> addTask(@RequestBody String task) throws JsonProcessingException {
        log.info("Произведён POST-запрос по адресу " + servicePath + " с телом " + task);
        taskService.addTask(task);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<AnyTypePattern> deleteTask(@PathVariable int taskId) {
        log.info("Произведён DELETE-запрос по адресу " + servicePath + "/id-" + taskId);
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("change-task")
    public ResponseEntity<AnyTypePattern> updateTask(@RequestBody String task) throws JsonProcessingException {
        log.info("Произведён PUT-запрос по адресу " + servicePath + "/change-task c task = " + task);
        taskService.updateTask(task);
        return ResponseEntity.ok().build();
    }

    @PutMapping("change-date")
    public ResponseEntity<AnyTypePattern> rescheduleTask(@RequestBody @Validated String date) throws JsonProcessingException {
        log.info("Произведён PUT-запрос по адресу " + servicePath + "/change-date с date = " + date);
        taskService.rescheduleTask(date);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{taskId}/make-done")
    public ResponseEntity<AnyTypePattern> makeTaskDone(@PathVariable int taskId) {
        log.info("Произведён PUT-запрос по адресу " + servicePath + "/" + taskId + "/make-done");
        taskService.makeTaskDone(taskId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{taskId}/make-undone")
    public ResponseEntity<AnyTypePattern> makeTaskUndone(@PathVariable int taskId) {
        log.info("Произведён PUT-запрос по адресу " + servicePath + "/" + taskId + "/make-undone");
        taskService.makeTaskUndone(taskId);
        return ResponseEntity.ok().build();
    }
}
