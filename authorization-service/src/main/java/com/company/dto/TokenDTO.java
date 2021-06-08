package com.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private long tokenId;
    private String serviceFrom;
    private String serviceTo;
    private Long sendingTime;
    private double code;
}
