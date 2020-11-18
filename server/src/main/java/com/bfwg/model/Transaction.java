package com.bfwg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "TRANSACTIONS")
//@IdClass(TransactionId.class)
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
    @Column(name = "card_sequence_no")
    private String cardSequenceNo;
    @Column(name = "transaction_field")
    private String transactionField;
    @Column(name = "iban")
    private String iban;
    @Column(name = "reference")
    private String reference;
    @Column(name = "date_time")
    private String dateTime;
    @Column(name = "value_date")
    private String valueDate;
    @Column(name = "type")
    private String type;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "adjusted_amount")
    private Double adjustedAmount;
    @Column(name = "reservation")
    private boolean reservation = false;
    @Column(name = "request")
    private boolean request = false;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "request_transaction",
            joinColumns = @JoinColumn(name = "transaction_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id"))
    private List<Transaction> requestTransactions = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "transaction_pattern",
            joinColumns = @JoinColumn(name = "transaction_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "pattern_id", referencedColumnName = "id"))
    private List<Pattern> patterns = new ArrayList<>();

}
