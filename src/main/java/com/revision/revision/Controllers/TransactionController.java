package com.revision.revision.Controllers;


import com.revision.revision.domain.transaction.Transactions;
import com.revision.revision.dtos.TransactionDTO;
import com.revision.revision.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transactions> create(@RequestBody TransactionDTO transactionDTO) throws Exception {
        return new ResponseEntity<Transactions>(transactionService.saveTransaction(transactionDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transactions>> findAll(){
        return new ResponseEntity<>(transactionService.findAll(), HttpStatus.OK);
    }
}
