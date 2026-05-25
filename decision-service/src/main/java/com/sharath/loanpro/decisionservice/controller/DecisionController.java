package com.sharath.loanpro.decisionservice.controller;

import com.sharath.loanpro.decisionservice.dto.DecisionRequestDto;
import com.sharath.loanpro.decisionservice.dto.DecisionResponseDto;
import com.sharath.loanpro.decisionservice.service.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/decisions")
@RequiredArgsConstructor
public class DecisionController {

    private final DecisionService decisionService;

    @PostMapping("/evaluate")
    public DecisionResponseDto evaluateLoan(
            @RequestBody DecisionRequestDto requestDto
    ) {

        return decisionService.evaluateLoan(requestDto);
    }
}