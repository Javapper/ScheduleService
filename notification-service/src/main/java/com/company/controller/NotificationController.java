package com.company.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.AnyTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.company.service.api.TelegramNotifier;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("notification")
public class NotificationController {

    private final TelegramNotifier telegramNotifier;
    private final String servicePath = "http://notification-service:8091";


    @Autowired
    public NotificationController(TelegramNotifier telegramNotifier) {
        this.telegramNotifier = telegramNotifier;
    }

    @PutMapping("turn-on")
    public ResponseEntity<AnyTypePattern> startSendMessagesInTelegram(@RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + servicePath + "/turn-on");
        if (telegramNotifier.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            telegramNotifier.startSendMessages();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @PutMapping("turn-off")
    public ResponseEntity<AnyTypePattern> stopSendMessagesInTelegram(@RequestHeader String token) throws IOException, InterruptedException {
        log.info("Произведён PUT-запрос по адресу " + servicePath + "/turn-off");
        if (telegramNotifier.isAllowedRequest(token)) {
            log.info("Токен прошёл проверку");
            telegramNotifier.stopSendMessages();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
}
