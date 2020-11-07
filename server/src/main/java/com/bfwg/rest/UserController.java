package com.bfwg.rest;

import com.bfwg.exception.ResourceConflictException;
import com.bfwg.model.Transaction;
import com.bfwg.model.User;
import com.bfwg.model.UserRequest;
import com.bfwg.service.FileSystemStorage;
import com.bfwg.service.TransactionService;
import com.bfwg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by fan.jin on 2016-10-15.
 */

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final FileSystemStorage fileSystemStorage;

    @Autowired
    public UserController(UserService userService, TransactionService transactionService, FileSystemStorage fileSystemStorage) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.fileSystemStorage = fileSystemStorage;
    }

    @RequestMapping(method = GET, value = "/user/{userId}")
    public User loadById(@PathVariable Long userId) {
        return this.userService.findById(userId);
    }

    @RequestMapping(method = GET, value = "/user/all")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @RequestMapping(method = GET, value = "/user/reset-credentials")
    public ResponseEntity<Map> resetCredentials() {
        this.userService.resetCredentials();
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        return ResponseEntity.accepted().body(result);
    }


    @RequestMapping(method = POST, value = "/signup")
    public ResponseEntity<?> addUser(@RequestBody UserRequest userRequest,
                                     UriComponentsBuilder ucBuilder) {

        User existUser = this.userService.findByUsername(userRequest.getUsername());
        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists");
        }
        User user = this.userService.save(userRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{userId}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    /*
     * We are not using userService.findByUsername here(we could), so it is good that we are making
     * sure that the user has role "ROLE_USER" to access this endpoint.
     */
    @RequestMapping("/whoami")
    @PreAuthorize("hasRole('USER')")
    public User user() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        String upfile = fileSystemStorage.saveFile(user.getId(), file);
        System.out.println(upfile);

//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/api/download/")
//                .path(upfile)
//                .toUriString();

//        return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(upfile,fileDownloadUri,"File uploaded with success!"));
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
}
