package com.bfwg.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TransactionId implements Serializable {
    private LocalDate reservedDate;
    private String name;
    private Long user;
    private Double amount;
}



