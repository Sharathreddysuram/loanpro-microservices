package com.sharath.loanpro.loanservice.dto;

import com.sharath.loanpro.loanservice.enums.LoanStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponseDto {

    private Long id;

    private String applicantName;

    private String email;

    private String loanType;

    private BigDecimal amount;

    private BigDecimal annualIncome;

    private String employmentStatus;

    private LoanStatus status;
}