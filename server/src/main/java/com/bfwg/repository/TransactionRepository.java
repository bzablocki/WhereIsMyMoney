package com.bfwg.repository;

import com.bfwg.model.Transaction;
import com.bfwg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, java.lang.Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserAndReservationTrue(User user);
    List<Transaction> findByUserOrderByReservedDateDesc(User user);
    @Transactional
    void deleteByReservationTrueAndUser(User user);
    @Transactional
    void deleteByNbIn(List<Long> nbs);
//    void deleteTransactionsByNb(List<Long> nbs)
}

