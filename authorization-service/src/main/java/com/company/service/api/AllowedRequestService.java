package com.company.service.api;

import com.company.dto.AllowedRequestDTO;

public interface AllowedRequestService {

    void addAllowedRequest(AllowedRequestDTO allowedRequestDTO);

    void deleteAllowedRequest(AllowedRequestDTO allowedRequestDTO);
}
