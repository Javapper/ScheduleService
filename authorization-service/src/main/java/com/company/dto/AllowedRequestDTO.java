package com.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllowedRequestDTO {
    private long allowedRequestId;
    private String serviceFrom;
    private String serviceTo;
}
