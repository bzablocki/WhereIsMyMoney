package com.bfwg.service;

import com.bfwg.model.Pattern;
import com.bfwg.model.Transaction;
import com.bfwg.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> getAll(User user);
    List<Transaction> getAllSorted(User user);
    List<Transaction> getAllReserved(User user);
    void saveAll(Collection<Transaction> transactions);
    Optional<Transaction> findFirst(Transaction transaction);
    void save(Transaction transaction);
    void deleteAllByUser(User user);
    void deleteReserved(User user);
    void deleteAll(List<Transaction> transactions);
//    Transaction findFirstByNameLikeAndReservedDateLessThanEqual(String name, LocalDate date);
    Optional<Transaction> findMatchedOriginalTransaction(Transaction collectedRequest);
    List<Transaction> findByUserAndNameOrDescriptionMatchingPattern(User user, Pattern pattern);
}
