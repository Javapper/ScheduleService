package com.company.service.impl;

import com.company.dto.AllowedRequestDTO;
import com.company.entity.AllowedRequestEntity;
import com.company.mapper.AllowedRequestMapper;
import com.company.service.api.AllowedRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AllowedRequestServiceImpl implements AllowedRequestService {
    private final AllowedRequestMapper allowedRequestMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public AllowedRequestServiceImpl(AllowedRequestMapper allowedRequestMapper, ModelMapper modelMapper) {
        this.allowedRequestMapper = allowedRequestMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<?> addAllowedRequest(AllowedRequestDTO allowedRequestDTO) {
        if (allowedRequestDTO == null) {
            return ResponseEntity.badRequest().build();
        } else {
            try {
                AllowedRequestEntity allowedRequestEntity = modelMapper.map(allowedRequestDTO, AllowedRequestEntity.class);
                allowedRequestMapper.addAllowedRequest(allowedRequestEntity);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @Override
    public ResponseEntity<?> deleteAllowedRequest(AllowedRequestDTO allowedRequestDTO) {
        if (allowedRequestDTO == null) {
            return ResponseEntity.badRequest().build();
        } else {
            try {
                AllowedRequestEntity allowedRequestEntity = modelMapper.map(allowedRequestDTO, AllowedRequestEntity.class);
                allowedRequestMapper.deleteAllowedRequest(allowedRequestEntity);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}
