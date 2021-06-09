package com.company.controller;

import lombok.extern.slf4j.Slf4j;
import com.company.dto.TaskDTO;
import com.company.service.impl.ResponseServiceImpl;
import org.aspectj.weaver.patterns.AnyTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class GatewayController {
    private final ResponseServiceImpl responseService;
    private final String gatewayService = "http://gateway-service:8081";

    @Autowired
    public GatewayController(ResponseServiceImpl responseService) {
        this.responseService = responseService;
    }

    @GetMapping("tasks/{taskId}")
    public ResponseEntity<TaskDTO> showTaskById(@PathVariable long taskId) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + gatewayService + "/tasks/" + taskId);
        return responseService.showTaskById(taskId);
    }

    @GetMapping("tasks")
    public ResponseEntity<List<TaskDTO>> showAllTasks() throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу "+ gatewayService + "/tasks");
        return responseService.showAllTasks();
    }

    @GetMapping("tasks/?date={date}")
    public ResponseEntity<List<TaskDTO>> showAllTasksAtDay(@RequestParam String date) throws IOException, InterruptedException {
        log.info("Произведён GET-запрос по адресу " + gatewayService + "/tasks/?date=" + date);
        return responseService.showAllTasksAtDay(date);
    }

    @PostMapping("tasks")
    public ResponseEntity<AnyTypePattern> addTask(@RequestBody @Validated TaskDTO taskDTO) throws IOException, InterruptedException {
        log.info("Произведён POST-запрос по адресу " + gatewayService + "/tasks c телом " + taskDTO.toString());
        return responseService.addTask(taskDTO);
    }

    @DeleteMapping("tasks/{taskId}")
    public ResponseEntity<AnyTypePattern> deleteTask(@PathVariable int taskId) throws IOException, InterruptedException {
        log.info("Произведён DELETE-запрос по адресу " + gatewayService + "/tasks/" + taskId);
        return responseService.deleteTask(taskId);
    }

    @PutMapping("tasks/change-task")
    public ResponseEntity<AnyTypePattern> updateTask(@RequestBody @Validated TaskDTO taskDTO) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + gatewayService + "/tasks/change-task с task = " + taskDTO.getTask());
        return responseService.updateTask(taskDTO);
    }

    @PutMapping("tasks/change-date")
    public ResponseEntity<AnyTypePattern> rescheduleTask(@RequestBody @Validated TaskDTO taskDTO) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + gatewayService + "/tasks/change-date с date = " + taskDTO.getDate());
        return responseService.rescheduleTask(taskDTO);
    }

    @PutMapping("tasks/{taskId}/do")
    public ResponseEntity<AnyTypePattern> makeTaskDone(@PathVariable int taskId) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + gatewayService + "/tasks/" + taskId + "/do");
        return responseService.makeTaskDone(taskId);
    }

    @PutMapping("tasks/{taskId}/undo")
    public ResponseEntity<AnyTypePattern> makeTaskUndone(@PathVariable int taskId) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + gatewayService + "/tasks/" + taskId + "/undo");
        return responseService.makeTaskUndone(taskId);
    }

    @PutMapping("notification/turn-on")
    public ResponseEntity<AnyTypePattern> startSendMessagesInTelegram() throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + gatewayService + "/notification/turn-on");
        return responseService.startSendMessagesInTelegram();
    }

    @PutMapping("notification/turn-off")
    public ResponseEntity<AnyTypePattern> stopSendMessagesInTelegram() throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + gatewayService + "/notification/turn-off");
        return responseService.stopSendMessagesInTelegram();
    }
}
