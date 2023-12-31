package com.revision.revision.services;

import com.revision.revision.domain.transaction.Transactions;
import com.revision.revision.domain.user.User;
import com.revision.revision.dtos.TransactionDTO;
import com.revision.revision.repositories.TransactionRepository;
import jakarta.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuthorizeTransactionService authorizeTransactionService;

    @Autowired
    private NotificationService notificationService;

    public Transactions saveTransaction(TransactionDTO transaction) throws Exception {
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());
        userService.validateTransaction(sender, transaction.value());

        if (!authorizeTransactionService.authorize(sender, transaction.value())){
            throw new Exception("Transação não autorizada");
        }

        Transactions newTransaction = Transactions.builder()
                .amount(transaction.value())
                .sender(sender)
                .receiver(receiver)
                .timestamp(LocalDateTime.now()).build();
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        transactionRepository.save(newTransaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);
        notificationService.sendNotification(sender, "Transação realizada com sucesso");
        notificationService.sendNotification(receiver, "Transação recebida com sucesso");

        return newTransaction;
    }

    public List<Transactions> findAll(){
        return transactionRepository.findAll();
    }

}
