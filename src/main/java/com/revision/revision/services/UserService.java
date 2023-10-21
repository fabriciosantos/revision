package com.revision.revision.services;

import com.revision.revision.domain.user.User;
import com.revision.revision.domain.user.UserType;
import com.revision.revision.dtos.UserDTO;
import com.revision.revision.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Usuario do tipo logista não pode realizar essa transação");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception {
       return userRepository.findById(id).orElseThrow(() -> new Exception("Usuario não foi encontrado"));
    }

    public User createUser(UserDTO user) {
        return userRepository.save(User.builder()
                .balance(user.balance())
                .document(user.document())
                .email(user.email())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .password(user.password())
                .userType(user.userType()).build());
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
