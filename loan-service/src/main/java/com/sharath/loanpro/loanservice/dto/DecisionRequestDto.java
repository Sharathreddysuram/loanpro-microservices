package com.sharath.loanpro.loanservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecisionRequestDto {

    private BigDecimal amount;
    private BigDecimal annualIncome;
}