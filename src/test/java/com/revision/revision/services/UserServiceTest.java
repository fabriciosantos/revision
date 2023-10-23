package com.revision.revision.services;

import com.revision.revision.domain.user.User;
import com.revision.revision.domain.user.UserType;
import com.revision.revision.repositories.TransactionRepository;
import com.revision.revision.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Autowired
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    // validateTransaction method throws exception if sender is of type MERCHANT
    @Test
    public void test_validate_transaction_throws_exception_if_sender_is_merchant() {
        // Given
        User sender = new User();
        sender.setUserType(UserType.MERCHANT);
        BigDecimal amount = new BigDecimal("100");

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            userService.validateTransaction(sender, amount);
        });

        // Then
        assertEquals("Usuario do tipo logista não pode realizar essa transação", exception.getMessage());
    }

}