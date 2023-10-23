package com.revision.revision.services;

import com.revision.revision.domain.user.User;
import com.revision.revision.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;


@Service
public class AuthorizeTransactionService {

    @Autowired
    private RestTemplate restTemplate;

    public Boolean authorize(User user, BigDecimal value) throws Exception {
//        ResponseEntity<Map> response =  restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);
//        if (response.getStatusCode() == HttpStatus.OK ){
//            String message = (String) response.getBody().get("message");
//            return "Autorizado".equalsIgnoreCase(message);
//        } else return false;
        return true;
    }
}
