package com.bfwg.repository;

import com.bfwg.model.Transaction;
import com.bfwg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, java.lang.Long> {
    List<Transaction> findByUser(User user);
//    void deleteTransactionsByReservationTrueAndUser(User user);
//    void deleteTransactionsByUser(User user);
}

