package com.sharath.loanpro.loanservice.entity;

import com.sharath.loanpro.loanservice.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "loan_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;

    @Column(nullable = false, unique = true)
    private String email;

    private String loanType;

    private BigDecimal amount;

    private BigDecimal annualIncome;

    private String employmentStatus;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}