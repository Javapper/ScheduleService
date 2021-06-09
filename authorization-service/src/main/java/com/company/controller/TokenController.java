package com.company.controller;

import com.company.dto.TokenDTO;
import com.company.service.api.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("tokens")
public class TokenController {
    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("create-token-from-{serviceFrom}-to-{serviceTo}")
    public ResponseEntity<?> createToken(@PathVariable String serviceFrom, @PathVariable String serviceTo) {
        if (serviceFrom.equals("") || serviceTo.equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        ResponseEntity responseFromServer = tokenService.createToken(serviceFrom, serviceTo);
        return ResponseEntity.status(responseFromServer.getStatusCode()).body(responseFromServer.getBody());

    }

    @PostMapping("check-token")
    public ResponseEntity<?> checkToken(@RequestBody String token) throws JsonProcessingException {
        if (tokenService.checkToken(token)) {
            log.info("С сервера возвращается информация о том, что токен валиден");
            return ResponseEntity.ok().build();
        }
        log.info("С сервера возвращается информация о том, что токен не валиден");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
}
