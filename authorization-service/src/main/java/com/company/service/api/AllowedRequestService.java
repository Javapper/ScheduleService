package com.company.service.api;

import com.company.dto.AllowedRequestDTO;
import org.springframework.http.ResponseEntity;

public interface AllowedRequestService {

    ResponseEntity addAllowedRequest(AllowedRequestDTO allowedRequestDTO);

    ResponseEntity<?> deleteAllowedRequest(AllowedRequestDTO allowedRequestDTO);
}
