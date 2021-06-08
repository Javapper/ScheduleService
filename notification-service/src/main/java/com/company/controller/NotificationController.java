package com.company.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.AnyTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.company.service.api.TelegramNotifier;

@Slf4j
@RestController
@RequestMapping("notification")
public class NotificationController {

    private final TelegramNotifier telegramNotifier;
    private final String servicePath = "http://localhost:8091";


    @Autowired
    public NotificationController(TelegramNotifier telegramNotifier) {
        this.telegramNotifier = telegramNotifier;
    }

    @PutMapping("turn-on")
    public ResponseEntity<AnyTypePattern> startSendMessagesInTelegram() {
        log.info("Произведён PUT-запрос по адресу " + servicePath + "/turn-on");
        telegramNotifier.startSendMessages();
        return ResponseEntity.ok().build();
    }

    @PutMapping("turn-off")
    public ResponseEntity<AnyTypePattern> stopSendMessagesInTelegram() {
        log.info("Произведён PUT-запрос по адресу " + servicePath + "/turn-off");
        telegramNotifier.stopSendMessages();
        return ResponseEntity.ok().build();
    }
}
