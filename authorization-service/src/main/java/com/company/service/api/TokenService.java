package com.company.service.api;

import com.company.dto.TokenDTO;
import com.company.entity.AllowedRequestEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface TokenService {

    TokenDTO createToken(String serviceFrom, String serviceTo);

    boolean checkToken(String token) throws JsonProcessingException;
}
