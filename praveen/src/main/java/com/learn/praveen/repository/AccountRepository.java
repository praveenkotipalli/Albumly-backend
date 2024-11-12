package com.learn.praveen.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.praveen.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByEmail(String email);
    
}
