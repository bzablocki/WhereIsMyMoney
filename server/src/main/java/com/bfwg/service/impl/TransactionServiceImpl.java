package com.bfwg.service.impl;

import com.bfwg.model.Pattern;
import com.bfwg.model.Transaction;
import com.bfwg.model.User;
import com.bfwg.repository.TransactionRepository;
import com.bfwg.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public List<Transaction> getAllReserved(User user) {
        return transactionRepository.findByUserAndReservationTrue(user);
    }

    @Override
    @Transactional
    public void saveAll(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);

    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void deleteAllByUser(User user) {
        List<Transaction> transactions = transactionRepository.findByUser(user);
        transactionRepository.deleteAll(transactions);
    }

    @Override
    public void deleteReserved(User user) {
        transactionRepository.deleteByReservationTrueAndUser(user);
    }

    @Override
    public Optional<Transaction> findFirst(Transaction t) {
        return Optional.ofNullable(transactionRepository.findFirstByReservedDateAndNameAndAmountAndUser(
                t.getReservedDate(),
                t.getName(),
                t.getAmount(),
                t.getUser()
        ));
    }

    @Override
    public void deleteAll(List<Transaction> transactions) {
        transactionRepository.deleteAll(transactions);
    }

    @Override
    public List<Transaction> findByUserAndNameOrDescriptionMatchingPattern(User user, Pattern pattern) {
        return transactionRepository.findAllByUserAndNameLikeOrDescriptionLike(user, pattern.getPattern().toLowerCase());
    }
    @Override
    public Optional<Transaction> findMatchedOriginalTransaction(Transaction collectedRequest) {
        if (collectedRequest.getDescription() != null) {
            String namePattern = "%" + collectedRequest.getDescription() + "%";
            LocalDate date = collectedRequest.getReservedDate();
            User user = collectedRequest.getUser();
            return Optional.ofNullable(transactionRepository.findFirstByUserAndNameLikeAndReservedDateLessThanEqualOrderByReservedDateDesc(
                    user,
                    namePattern,
                    date));
        } else {
            return Optional.empty();
        }

    }


}
