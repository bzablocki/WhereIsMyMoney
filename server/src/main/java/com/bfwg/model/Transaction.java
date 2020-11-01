package com.bfwg.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Transaction implements Serializable {
    private LocalDate reservedDate;
    private String name;
    private String description;
    private String cardSequenceNo;
    private String transactionField;
    private String iban;
    private String reference;
    private String dateTime;
    private String valueDate;
    private String type;
    private Double amount;
    private String user;
    private boolean isReservation = false;

}
