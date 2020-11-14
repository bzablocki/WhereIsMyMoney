package com.bfwg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "TRANSACTIONS")
@IdClass(TransactionId.class)
public class Transaction implements Serializable {
    @Column(name = "nb", columnDefinition = "serial", insertable = false, updatable = false)
    private Long nb;
    @Id
    @Column(name = "reserved_date", columnDefinition = "DATE")
    private LocalDate reservedDate;
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "cardsequenceno")
    private String cardSequenceNo;
    @Column(name = "transactionfield")
    private String transactionField;
    @Column(name = "iban")
    private String iban;
    @Column(name = "reference")
    private String reference;
    @Column(name = "datetime")
    private String dateTime;
    @Column(name = "valuedate")
    private String valueDate;
    @Column(name = "type")
    private String type;
    @Id
    @Column(name = "amount")
    private Double amount;
    @Column(name = "reservation")
    private boolean reservation = false;
    @Column(name = "request")
    private boolean request = false;
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    public TransactionId getId(){
        return new TransactionId(this.reservedDate, this.name, this.user.getId(), this.amount);
    }
}
