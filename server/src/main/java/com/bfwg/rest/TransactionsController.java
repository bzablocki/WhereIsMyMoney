package com.bfwg.rest;

import com.bfwg.model.Category;
import com.bfwg.model.Pattern;
import com.bfwg.model.Transaction;
import com.bfwg.model.User;
import com.bfwg.service.CategoryService;
import com.bfwg.service.FileSystemStorage;
import com.bfwg.service.TransactionService;
import com.bfwg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by fan.jin on 2016-10-15.
 */

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionsController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final FileSystemStorage fileSystemStorage;

    @Autowired
    public TransactionsController(UserService userService,
                                  TransactionService transactionService,
                                  CategoryService categoryService,
                                  FileSystemStorage fileSystemStorage
    ) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.fileSystemStorage = fileSystemStorage;
    }

    @RequestMapping("/getTransactionsFromDB")
    @PreAuthorize("hasRole('USER')")
    public List<Transaction> getTransactionsFromDB() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transactionService.getAllSorted(user);
    }

    @RequestMapping("/deleteTransactions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> deleteTransaction() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transactionService.deleteAllByUser(user);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);

    }

    @RequestMapping("/getTransactionsFromPdf")
    @PreAuthorize("hasRole('USER')")
    public List<Transaction> getTransactionsFromPdf() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        PdfController pdfController = new PdfController("server\\src\\main\\resources\\bank_statement.pdf");
//        PdfController pdfController = new PdfController(user, "server\\src\\main\\resources\\bank_statement2.pdf");
//        List<Transaction> transactionsList = pdfController.getTransactionsList();
//        this.transactionService.deleteAll(user);
//        transactionsList.get(1).setReservation(true);
//        this.transactionService.deleteReserved(user);
//        this.transactionService.saveAll(transactionsList);

        return Collections.emptyList();
    }

    @RequestMapping(path = "/api/upload-pdf", method = POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> uploadSingleFile(@RequestParam("file") MultipartFile file) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Uploading pdf...");
        String uploadedFileName = fileSystemStorage.saveFile(user.getId(), file);
        System.out.println(uploadedFileName + " uploaded.");

        PdfController pdfController = new PdfController(user, uploadedFileName);
        List<Transaction> newTransactionsList = pdfController.getTransactionsList();

        processReservedTransactions(user, newTransactionsList);
        newTransactionsList = removeDuplicates(newTransactionsList);
        transactionService.saveAll(newTransactionsList);
        linkRequestsToTransactions(newTransactionsList);
//        linkTransactionsToCategory(newTransactionsList);

        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }


    private void linkRequestsToTransactions(List<Transaction> newTransactionsList) {
        List<Transaction> requestTransactions = newTransactionsList.stream()
                .filter(Transaction::isRequest)
                .collect(Collectors.toList());

        // find an original transaction for every collectedRequest and
        // subtract the amount to calculate the real spending
        List<Transaction> collectedRequests = requestTransactions.stream()
                .filter(transaction -> transaction.getAmount() > 0.)
                .collect(Collectors.toList());

        for (Transaction collectedRequest : collectedRequests) {
            Optional<Transaction> matchedTransaction = transactionService.findMatchedOriginalTransaction(collectedRequest);

            if (matchedTransaction.isPresent()) {
                Transaction transaction = matchedTransaction.get();
                transaction.getRequestTransactions().add(collectedRequest);
                transaction.setAdjustedAmount(transaction.getAdjustedAmount() + collectedRequest.getAdjustedAmount());
                collectedRequest.setAdjustedAmount(0.0);
                transactionService.save(transaction);
                transactionService.save(collectedRequest);
            }

        }

    }

    private List<Transaction> removeDuplicates(List<Transaction> newTransactionsList) {
        List<Transaction> dbNewTransactions = new ArrayList<>();
        for (Transaction t : newTransactionsList) {
            if (isScheduledForUpdate(t) || !transactionService.findFirst(t).isPresent()) {
                dbNewTransactions.add(t);
            }
        }
        return dbNewTransactions;
    }

    private boolean isScheduledForUpdate(Transaction t) {
        return t.getId() != null;
    }

    private void processReservedTransactions(User user, List<Transaction> newTransactionsList) {
        List<Transaction> reservedTransactionsDB = transactionService.getAllReserved(user);

        for (Transaction t : reservedTransactionsDB) {
            Optional<Transaction> matchedNewTransaction = newTransactionsList.stream()
                    .filter(transaction -> t.getCardSequenceNo() != null && t.getCardSequenceNo().equals(transaction.getCardSequenceNo()))
                    .findFirst();
            // todo: update the new transaction with the old fields like "tag" or "category" added by a user
            matchedNewTransaction.ifPresent(mt -> mt.setId(t.getId()));
        }

    }


    @RequestMapping(path = "/refreshTransactionToCategoryMapping", method = GET)
    public ResponseEntity<Boolean> refreshTransactionToCategoryMapping() {
        // find all patterns for user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category unknownCategory = categoryService.findUnknownCategory();

        List<Pattern> patterns = user.getPatterns();

        // for each pattern, find all transactions where user=user and description like pattern or name like pattern
        for (Pattern p : patterns) {
            List<Transaction> transactionsMatchingPattern = transactionService.findByUserAndNameOrDescriptionMatchingPattern(user, p);
            // check if matching already exist
            for (Transaction t : transactionsMatchingPattern) {
                boolean match = t.getCategories().contains(p.getCategory());
                // if matching exists, dismiss, if not create new matching - done with Set
                if (!match) {
                    t.getCategories().remove(unknownCategory);
                    t.getCategories().add(p.getCategory());
                }
            }
            transactionService.saveAll(transactionsMatchingPattern);
        }

        List<Transaction> allTransactions = transactionService.getAll(user);
        Set<Transaction> transactionsWithoutCategory = allTransactions.stream()
                .filter(transaction -> transaction.getCategories().size() == 0)
                .collect(Collectors.toSet());
        transactionsWithoutCategory.forEach(transaction -> transaction.getCategories().add(unknownCategory));
        transactionService.saveAll(transactionsWithoutCategory);


        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
}
