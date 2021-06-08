package com.company.service.impl;

import com.company.dto.TokenDTO;
import com.company.entity.AllowedRequestEntity;
import com.company.entity.TokenEntity;
import com.company.mapper.AllowedRequestMapper;
import com.company.mapper.TokenMapper;
import com.company.service.api.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final AllowedRequestMapper allowedRequestMapper;
    private final ObjectMapper objectMapper;
    private final TokenMapper tokenMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public TokenServiceImpl(AllowedRequestMapper allowedRequestMapper, ObjectMapper objectMapper,
                            TokenMapper tokenMapper, ModelMapper modelMapper, Timer timer) {
        this.allowedRequestMapper = allowedRequestMapper;
        this.objectMapper = objectMapper;
        this.tokenMapper = tokenMapper;
        this.modelMapper = modelMapper;
        timer.schedule(new Deleter(tokenMapper), 0, 1000 * 60 * 60);
    }

    @Override
    public ResponseEntity<?> createToken(String serviceFrom, String serviceTo) {
        log.info("Получен запрос на создание нового токена");
        if (isRequestAllowed(serviceFrom, serviceTo)) {
            log.info("Запрос от одного сервиса к другому разрешён");
            TokenEntity newToken = createNewToken(serviceFrom, serviceTo);
            tokenMapper.addToken(newToken);
            return ResponseEntity.ok(modelMapper.map(newToken, TokenDTO.class));
        }
        log.info("Запрос от одного сервиса к другому запрещён");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    private boolean isRequestAllowed(String serviceFrom, String serviceTo) {
        log.info("Проверка на доступность запросов от одного сервиса к другому");
        AllowedRequestEntity request = AllowedRequestEntity.builder()
                .serviceFrom(serviceFrom)
                .serviceTo(serviceTo).build();
        AllowedRequestEntity response = allowedRequestMapper.selectAllowedRequestIfExist(request);
        return !response.getServiceFrom().isEmpty();
    }

    private TokenEntity createNewToken(String serviceFrom, String serviceTo) {
        log.info("Создание нового токена");
        return TokenEntity.builder()
                .serviceFrom(serviceFrom)
                .serviceTo(serviceTo)
                .sendingTime(new Date().getTime())
                .code(Math.random() * 1000000)
                .build();
    }

    @Override
    public boolean checkToken(String token) throws JsonProcessingException {
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        log.info("Произведён запрос на проверку токена");
        TokenEntity tokenEntity = tokenMapper.selectTokenIfExist(modelMapper.map(tokenDTO, TokenEntity.class));
        log.info("Рузльтат поиска токена в БД: " + tokenEntity);
        if ( tokenEntity!= null) {
            if (tokenEntity.getSendingTime() + 5000 > new Date().getTime()) {
                log.info("Токен валиден");
            }
            tokenMapper.deleteToken(tokenEntity);
            return true;
        }
        log.info("Токен не валиден");
        return false;
    }
}

class Deleter extends TimerTask {

    private final TokenMapper tokenMapper;

    public Deleter(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    @Override
    public void run() {
        tokenMapper.selectAllTokens()
                .forEach(token -> {
                    if (token.getSendingTime() + 5000 <= new Date().getTime()) {
                        tokenMapper.deleteToken(token);
                    }
                });
    }
}