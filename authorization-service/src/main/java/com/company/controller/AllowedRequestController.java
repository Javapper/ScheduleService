package com.company.controller;

import com.company.dto.AllowedRequestDTO;
import com.company.service.api.AllowedRequestService;
import org.aspectj.weaver.patterns.AnyTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<AnyTypePattern> addAllowedRequest(@RequestBody @Validated AllowedRequestDTO allowedRequestDTO) {
        allowedRequestService.addAllowedRequest(allowedRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<AnyTypePattern> deleteAllowedRequest(@RequestBody @Validated AllowedRequestDTO allowedRequestDTO) {
        allowedRequestService.deleteAllowedRequest(allowedRequestDTO);
        return ResponseEntity.ok().build();
    }
}
