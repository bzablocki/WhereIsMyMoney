package com.bfwg.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "TRANSACTIONS")
public class Transaction implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reserved_date", columnDefinition = "DATE")
    private LocalDate reservedDate;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "cardSequenceNo")
    private String cardSequenceNo;
    @Column(name = "transactionField")
    private String transactionField;
    @Column(name = "iban")
    private String iban;
    @Column(name = "reference")
    private String reference;
    @Column(name = "dateTime")
    private String dateTime;
    @Column(name = "valueDate")
    private String valueDate;
    @Column(name = "type")
    private String type;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "is_reservation")
    private boolean isReservation = false;

    @Column(name = "user_id")
    private Long user;

}
