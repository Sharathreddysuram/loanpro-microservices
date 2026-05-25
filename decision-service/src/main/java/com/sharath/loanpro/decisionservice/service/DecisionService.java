package com.sharath.loanpro.decisionservice.service;

import com.sharath.loanpro.decisionservice.dto.DecisionRequestDto;
import com.sharath.loanpro.decisionservice.dto.DecisionResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DecisionService {

    public DecisionResponseDto evaluateLoan(DecisionRequestDto requestDto) {

        BigDecimal income = requestDto.getAnnualIncome();
        BigDecimal amount = requestDto.getAmount();

        BigDecimal limit = income.multiply(BigDecimal.valueOf(0.5));

        if (amount.compareTo(limit) <= 0) {

            return DecisionResponseDto.builder()
                    .decision("APPROVED")
                    .reason("Applicant meets income eligibility criteria")
                    .build();
        }

        return DecisionResponseDto.builder()
                .decision("REJECTED")
                .reason("Requested amount exceeds eligibility limit")
                .build();
    }
}