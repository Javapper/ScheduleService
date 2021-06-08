package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {
    private Long tokenId;
    private String serviceFrom;
    private String serviceTo;
    private Long sendingTime;
    private double code;
}
