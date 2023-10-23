package com.revision.revision.services;


import com.revision.revision.domain.transaction.Transactions;
import com.revision.revision.domain.user.User;
import com.revision.revision.domain.user.UserType;
import com.revision.revision.dtos.TransactionDTO;
import com.revision.revision.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;


    @Mock
    private UserService userService;

    @Mock
    private AuthorizeTransactionService authorizeTransactionService;

    @Mock
    private NotificationService notificationService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deverá trazer uma transaction criada com sucesso")
    void createTransactionCase() throws Exception {
        User sender = getBuildSender();
        User receiver = getBuildReceiver();

        when(userService.findUserById(1l)).thenReturn(sender);
        when(userService.findUserById(2l)).thenReturn(receiver);
        when(authorizeTransactionService.authorize(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1l, 2l);

        transactionService.saveTransaction(request);

        verify(transactionRepository, times(1)).save(any());
        sender.setBalance(new BigDecimal(90));
        verify(userService, times(1)).saveUser(sender);
        receiver.setBalance(new BigDecimal(120));
        verify(userService, times(1)).saveUser(receiver);
        verify(notificationService, times(1)).sendNotification(sender, "Transação realizada com sucesso");
        verify(notificationService, times(1)).sendNotification(receiver, "Transação recebida com sucesso");

    }

    @Test
    @DisplayName("Deverá trazer uma transaction não criada com error")
    void createTransactionErrorCase() throws Exception {
        User sender = getBuildSender();
        User receiver = getBuildReceiver();

        when(userService.findUserById(1l)).thenReturn(sender);
        when(userService.findUserById(2l)).thenReturn(receiver);
        when(authorizeTransactionService.authorize(any(), any())).thenReturn(false);

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1l, 2l);
            transactionService.saveTransaction(request);
        });

        Assertions.assertEquals("Transação não autorizada", thrown.getMessage());
    }

    @Test
    @DisplayName("Deverá trazer uma lista de transações com sucesso")
    void findAllCase(){
        when(transactionRepository.findAll()).thenReturn(getTransactions());

        List<Transactions> response = transactionService.findAll();

        Assertions.assertEquals(response.get(0).getSender(), getBuildSender());
        Assertions.assertEquals(response.get(0).getReceiver(), getBuildReceiver());
        Assertions.assertEquals(response.get(0).getAmount(), new BigDecimal(10));
    }

    private User getBuildSender() {
        return User.builder()
                .id(1l)
                .password("123")
                .lastName("last")
                .userType(UserType.COMMON)
                .firstName("Jose")
                .email("email")
                .document("document")
                .balance(new BigDecimal(100)).build();
    }

    private User getBuildReceiver() {
        return User.builder()
                .id(2l)
                .password("1234")
                .lastName("last")
                .userType(UserType.COMMON)
                .firstName("Maria")
                .email("email1")
                .document("document1")
                .balance(new BigDecimal(100)).build();
    }

    private List<Transactions> getTransactions() {
        List<Transactions> response = new ArrayList<>();
        response.add(Transactions.builder()
                .amount(new BigDecimal(10))
                .sender(getBuildSender())
                .receiver(getBuildReceiver())
                .timestamp(LocalDateTime.now()).build());
        return response;
    }
}
