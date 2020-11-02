package com.bfwg.service;

import com.bfwg.model.Transaction;
import com.bfwg.model.User;

import java.util.List;

public interface TransactionService {
    List<TransactionService> getAll(java.lang.Long user_id);
    void saveAll(List<Transaction> transactions);
    void save(Transaction transaction);
    void deleteAll(User user);
    void deleteReserved(User user);

}
