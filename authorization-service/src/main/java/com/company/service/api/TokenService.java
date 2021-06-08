package com.company.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface TokenService {

    ResponseEntity<?> createToken(String serviceFrom, String serviceTo);

    boolean checkToken(String token) throws JsonProcessingException;
}
