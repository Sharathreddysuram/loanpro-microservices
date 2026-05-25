package com.sharath.loanpro.loanservice.repository;

import com.sharath.loanpro.loanservice.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    boolean existsByEmail(String email);
}