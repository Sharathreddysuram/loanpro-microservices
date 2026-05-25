package com.sharath.loanpro.decisionservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DecisionResponseDto {

    private String decision;

    private String reason;
}