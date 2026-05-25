package com.sharath.loanpro.loanservice.client;

import com.sharath.loanpro.loanservice.dto.DecisionRequestDto;
import com.sharath.loanpro.loanservice.dto.DecisionResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "DECISION-SERVICE")
public interface DecisionClient {

    @PostMapping("/api/decisions/evaluate")
    DecisionResponseDto evaluateLoan(DecisionRequestDto requestDto);
}