package com.bfwg.service;

import com.bfwg.model.Transaction;
import com.bfwg.model.User;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAll(User user);
    List<Transaction> getAllSorted(User user);
    void saveAll(List<Transaction> transactions);
    void save(Transaction transaction);
    void deleteAll(User user);
    void deleteReserved(User user);

}
