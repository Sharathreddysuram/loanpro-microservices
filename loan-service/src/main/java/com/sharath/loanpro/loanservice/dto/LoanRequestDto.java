package com.sharath.loanpro.loanservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequestDto {

    @NotBlank(message = "Applicant name is required")
    private String applicantName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Annual income is required")
    @Positive(message = "Annual income must be positive")
    private BigDecimal annualIncome;

    @NotBlank(message = "Employment status is required")
    private String employmentStatus;
}