package com.company.controller;

import com.company.dto.AllowedRequestDTO;
import com.company.service.api.AllowedRequestService;
import org.aspectj.weaver.patterns.AnyTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("allowed-requests")
public class AllowedRequestController {
    private final AllowedRequestService allowedRequestService;

    @Autowired
    public AllowedRequestController(AllowedRequestService allowedRequestService) {
        this.allowedRequestService = allowedRequestService;
    }

    @PostMapping("")
    public ResponseEntity<?> addAllowedRequest(@RequestBody @Validated AllowedRequestDTO allowedRequestDTO) {
        allowedRequestService.addAllowedRequest(allowedRequestDTO);
        if (allowedRequestDTO.getServiceFrom() == "" || allowedRequestDTO.getServiceFrom().isEmpty() ||
                allowedRequestDTO.getServiceTo() == "" || allowedRequestDTO.getServiceTo().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteAllowedRequest(@RequestBody @Validated AllowedRequestDTO allowedRequestDTO) {
        allowedRequestService.deleteAllowedRequest(allowedRequestDTO);
        if (allowedRequestDTO.getServiceFrom() == "" || allowedRequestDTO.getServiceFrom().isEmpty() ||
                allowedRequestDTO.getServiceTo() == "" || allowedRequestDTO.getServiceTo().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }
}
