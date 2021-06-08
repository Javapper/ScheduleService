package com.company.service.impl;

import com.company.dto.AllowedRequestDTO;
import com.company.entity.AllowedRequestEntity;
import com.company.mapper.AllowedRequestMapper;
import com.company.service.api.AllowedRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void addAllowedRequest(AllowedRequestDTO allowedRequestDTO) {
        AllowedRequestEntity allowedRequestEntity = modelMapper.map(allowedRequestDTO, AllowedRequestEntity.class);
        allowedRequestMapper.addAllowedRequest(allowedRequestEntity);
    }

    @Override
    public void deleteAllowedRequest(AllowedRequestDTO allowedRequestDTO) {
        AllowedRequestEntity allowedRequestEntity = modelMapper.map(allowedRequestDTO, AllowedRequestEntity.class);
        allowedRequestMapper.deleteAllowedRequest(allowedRequestEntity);
    }
}
