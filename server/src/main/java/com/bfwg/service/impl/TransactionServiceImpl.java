package com.bfwg.service.impl;

import com.bfwg.model.Transaction;
import com.bfwg.model.User;
import com.bfwg.repository.TransactionRepository;
import com.bfwg.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @Override
    public List<Transaction> getAll(User user) {
        return transactionRepository.findByUser(user);
    }


    @Override
    public List<Transaction> getAllSorted(User user) {
        return transactionRepository.findByUserOrderByReservedDateDesc(user);
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void deleteAll(User user) {
        List<Transaction> transactions = transactionRepository.findByUser(user);
        transactionRepository.deleteAll(transactions);
    }

    @Override
    public void deleteReserved(User user) {
        transactionRepository.deleteByReservationTrueAndUser(user);
    }
}
