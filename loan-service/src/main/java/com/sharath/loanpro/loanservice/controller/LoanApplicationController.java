package com.sharath.loanpro.loanservice.controller;

import com.sharath.loanpro.loanservice.dto.LoanRequestDto;
import com.sharath.loanpro.loanservice.dto.LoanResponseDto;
import com.sharath.loanpro.loanservice.enums.LoanStatus;
import com.sharath.loanpro.loanservice.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService service;

    @PostMapping
    public LoanResponseDto createLoan(@Valid @RequestBody LoanRequestDto requestDto) {
        return service.createLoan(requestDto);
    }

    @GetMapping
    public List<LoanResponseDto> getAllLoans() {
        return service.getAllLoans();
    }

    @GetMapping("/{id}")
    public LoanResponseDto getLoanById(@PathVariable Long id) {
        return service.getLoanById(id);
    }

    @PutMapping("/{id}/status")
    public LoanResponseDto updateStatus(
            @PathVariable Long id,
            @RequestParam LoanStatus status
    ) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public String deleteLoan(@PathVariable Long id) {
        service.deleteLoan(id);
        return "Loan application deleted successfully";
    }
}