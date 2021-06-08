package com.company.controller;

import com.company.dto.TokenDTO;
import com.company.service.api.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tokens")
public class TokenController {
    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("create-token-from-{serviceFrom}-to-{serviceTo}")
    public ResponseEntity<TokenDTO> createToken(@PathVariable String serviceFrom, @PathVariable String serviceTo) {
        return ResponseEntity.ok(tokenService.createToken(serviceFrom, serviceTo));
    }

    @PostMapping("check-token")
    public ResponseEntity<Boolean> checkToken(@RequestBody String token) throws JsonProcessingException {
        return ResponseEntity.ok(tokenService.checkToken(token));
    }
}
