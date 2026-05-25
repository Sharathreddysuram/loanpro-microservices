package com.sharath.loanpro.decisionservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DecisionRequestDto {

    private BigDecimal amount;

    private BigDecimal annualIncome;
}