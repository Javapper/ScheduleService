package com.company.service.api;

import org.aspectj.weaver.patterns.AnyTypePattern;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface TelegramNotifier {

    ResponseEntity<AnyTypePattern> startSendMessages();

    ResponseEntity<AnyTypePattern> stopSendMessages();

    boolean isAllowedRequest(String token) throws IOException, InterruptedException;
}