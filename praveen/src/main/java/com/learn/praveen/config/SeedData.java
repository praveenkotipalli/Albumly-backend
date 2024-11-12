package com.learn.praveen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.learn.praveen.model.Account;
import com.learn.praveen.service.AccountService;
import com.learn.praveen.utils.constants.Authority;


@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        // Account account01 = new Account();
        // Account account02 = new Account();
        // Account account03 = new Account();

        // account01.setEmail("user@gmail.com") ;
        // account01.setPassword("pass987");
        // account01.setAuthorities(Authority.USER.toString());  

        // account02.setEmail("admin@gmail.com") ;
        // account02.setPassword("pass987");
        // account02.setAuthorities(Authority.ADMIN.toString()+" "+Authority.USER.toString());

        // account03.setEmail("praveen@gmail.com") ;
        // account03.setPassword("password");
        // account03.setAuthorities(Authority.ADMIN.toString()+" "+Authority.USER.toString());

        // accountService.save(account01);
        // accountService.save(account02);
        // accountService.save(account03);
    }
    
}
