package com.bfwg.repository;

import com.bfwg.model.Transaction;
import com.bfwg.model.TransactionId;
import com.bfwg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserAndReservationTrue(User user);
    List<Transaction> findByUserOrderByReservedDateDesc(User user);

    @Transactional
    void deleteByReservationTrueAndUser(User user);
    @Transactional
    Transaction findFirstByNameLikeAndReservedDateLessThanEqualOrderByReservedDateDesc(String name, LocalDate date);
    Transaction findFirstByReservedDateAndNameAndAmountAndUser(LocalDate date, String name, Double amount, User user);

}

