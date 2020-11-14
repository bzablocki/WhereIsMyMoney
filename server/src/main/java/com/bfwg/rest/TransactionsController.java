package com.bfwg.rest;

import com.bfwg.model.Transaction;
import com.bfwg.model.User;
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

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by fan.jin on 2016-10-15.
 */

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionsController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final FileSystemStorage fileSystemStorage;

    @Autowired
    public TransactionsController(UserService userService,
                                  TransactionService transactionService,
                                  FileSystemStorage fileSystemStorage
    ) {
        this.userService = userService;
        this.transactionService = transactionService;
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
            // todo mark collected and matched - add DB entry
            if (matchedTransaction.isPresent()){
                Transaction transaction = matchedTransaction.get();
                transaction.getRequestTransactions().add(collectedRequest);
                transactionService.save(transaction);
            }

        }

        // alter the name for the sent, to be able to count them as your spending
        List<Transaction> sentRequests = requestTransactions.stream()
                .filter(transaction -> transaction.getAmount() <= 0.)
                .collect(Collectors.toList());

        // todo rename and update

    }

    private List<Transaction> removeDuplicates(List<Transaction> newTransactionsList) {
        List<Transaction> dbNewTransactions = new ArrayList<>();
        for (Transaction t : newTransactionsList) {
            if (isScheduledForUpdate(t) || !transactionService.findFirst(t).isPresent()){
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
}
