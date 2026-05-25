package com.sharath.loanpro.loanservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecisionResponseDto {

    private String decision;
    private String reason;
}