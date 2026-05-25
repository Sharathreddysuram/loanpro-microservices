package com.sharath.loanpro.loanservice.service;

import com.sharath.loanpro.loanservice.client.DecisionClient;
import com.sharath.loanpro.loanservice.dto.DecisionRequestDto;
import com.sharath.loanpro.loanservice.dto.DecisionResponseDto;
import com.sharath.loanpro.loanservice.dto.LoanRequestDto;
import com.sharath.loanpro.loanservice.dto.LoanResponseDto;
import com.sharath.loanpro.loanservice.entity.LoanApplication;
import com.sharath.loanpro.loanservice.enums.LoanStatus;
import com.sharath.loanpro.loanservice.exception.DuplicateResourceException;
import com.sharath.loanpro.loanservice.exception.ResourceNotFoundException;
import com.sharath.loanpro.loanservice.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final DecisionClient decisionClient;

    public LoanResponseDto createLoan(LoanRequestDto requestDto) {

        if (repository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException(
                    "Loan application already exists with email: "
                            + requestDto.getEmail()
            );
        }

        DecisionRequestDto decisionRequest = DecisionRequestDto.builder()
                .amount(requestDto.getAmount())
                .annualIncome(requestDto.getAnnualIncome())
                .build();

        DecisionResponseDto decisionResponse =
                decisionClient.evaluateLoan(decisionRequest);

        LoanStatus status =
                LoanStatus.valueOf(decisionResponse.getDecision());

        LoanApplication loan = LoanApplication.builder()
                .applicantName(requestDto.getApplicantName())
                .email(requestDto.getEmail())
                .loanType(requestDto.getLoanType())
                .amount(requestDto.getAmount())
                .annualIncome(requestDto.getAnnualIncome())
                .employmentStatus(requestDto.getEmploymentStatus())
                .status(status)
                .build();

        LoanApplication savedLoan = repository.save(loan);

        return mapToResponseDto(savedLoan);
    }

    public List<LoanResponseDto> getAllLoans() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public LoanResponseDto getLoanById(Long id) {

        LoanApplication loan = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan application not found with id: " + id
                        )
                );

        return mapToResponseDto(loan);
    }

    public LoanResponseDto updateStatus(Long id, LoanStatus status) {

        LoanApplication loan = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan application not found with id: " + id
                        )
                );

        loan.setStatus(status);

        LoanApplication updatedLoan = repository.save(loan);

        return mapToResponseDto(updatedLoan);
    }

    public void deleteLoan(Long id) {

        LoanApplication loan = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan application not found with id: " + id
                        )
                );

        repository.delete(loan);
    }

    private LoanResponseDto mapToResponseDto(LoanApplication loan) {

        return LoanResponseDto.builder()
                .id(loan.getId())
                .applicantName(loan.getApplicantName())
                .email(loan.getEmail())
                .loanType(loan.getLoanType())
                .amount(loan.getAmount())
                .annualIncome(loan.getAnnualIncome())
                .employmentStatus(loan.getEmploymentStatus())
                .status(loan.getStatus())
                .build();
    }
}