package com.learn.praveen.controller;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.learn.praveen.model.Account;
import com.learn.praveen.payloads.auth.AccountDTO;
import com.learn.praveen.payloads.auth.AccountViewDTO;
import com.learn.praveen.payloads.auth.AuthoritiesDTO;
import com.learn.praveen.payloads.auth.PasswordDTO;
import com.learn.praveen.payloads.auth.ProfileDTO;
import com.learn.praveen.payloads.auth.TokenDTO;
import com.learn.praveen.payloads.auth.UserLoginDTO;
import com.learn.praveen.service.AccountService;
import com.learn.praveen.service.TokenService;
import com.learn.praveen.utils.constants.AccountError;
import com.learn.praveen.utils.constants.AccountSuccess;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Tag(name = "Auth Controller", description = "Controller for Account management")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountService accountService;


    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDTO> token(@RequestBody UserLoginDTO userLogin) throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
            return ResponseEntity.ok(new TokenDTO(tokenService.generateToken(authentication)));
        } catch (Exception e) {
           log.debug(AccountError.TOKEN_GENERATION_ERROR.toString() + ": "+e.getMessage());
           return new ResponseEntity<>(new TokenDTO(null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/users/add", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "User added successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Operation(summary = "Add a new user", description = "Add a new user to the system")
    public ResponseEntity<String>addUser(@Valid @RequestBody AccountDTO accountDTO) {
        try {
            Account account = new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());
            // account.setRole("ROLE_USER");
            accountService.save(account);
            return ResponseEntity.ok(AccountSuccess.ACCOUNT_ADDED.toString());

        } catch (Exception e) {
            log.debug(AccountError.ADD_ACCOUNT_ERROR.toString()+": "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
    }

    @GetMapping(value = "/users", produces = "application/json")
    @SecurityRequirement(name = "praveen-api")
    @ApiResponse(responseCode = "200", description = "List users")
    @ApiResponse(responseCode = "401", description = "Token missing or invalid")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List users", description = "list users")
    public List<AccountViewDTO> Users() {
        List<AccountViewDTO> accounts = new ArrayList<>();

        for(Account account: accountService.findAll()) {
            accounts.add(new AccountViewDTO(account.getId(), account.getEmail(), account.getAuthorities()));

        }
       return accounts;
        
    }

    @GetMapping(value = "/profile", produces = "application/json")
    @SecurityRequirement(name = "praveen-api")
    @ApiResponse(responseCode = "200", description = "user profile")
    @ApiResponse(responseCode = "401", description = "Token missing or invalid")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "view profile")
    public ProfileDTO Profile(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            ProfileDTO profile = new ProfileDTO(account.getId(), account.getEmail(), account.getAuthorities());
            return profile;
        }
        

        return null;
    }


    @PutMapping(value = "/profile/update-password", consumes = "application/json", produces = "application/json")
    @SecurityRequirement(name = "praveen-api")
    @ApiResponse(responseCode = "200", description = "user profile update")
    @ApiResponse(responseCode = "401", description = "Token missing or invalid")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "update profile")
    public AccountViewDTO update_password(@Valid @RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            // account.setPassword(passwordDTO.getPassword());
            accountService.save(account);

            AccountViewDTO accountViewDTO = new AccountViewDTO(account.getId(), account.getEmail(), account.getAuthorities());
            return accountViewDTO;
        }
        

        return null;
    }


    @PutMapping(value = "/users/{user_id}/update-authorities", consumes = "application/json", produces = "application/json")
    @SecurityRequirement(name = "praveen-api")
    @ApiResponse(responseCode = "200", description = "User authorities updated")
    @ApiResponse(responseCode = "401", description = "Token missing or invalid")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Update user authorities", description = "Update the authorities of a user")
    public ResponseEntity<AccountViewDTO> updateAuthorities( @Valid @RequestBody AuthoritiesDTO authoritiesDTO, @PathVariable Long user_id) {
        
        Optional<Account> optionalAccount = accountService.findById(user_id);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            account.setAuthorities(authoritiesDTO.getAuthorities());
            accountService.saveWithoutPassword(account);

            AccountViewDTO accountViewDTO = new AccountViewDTO(account.getId(), account.getEmail(), account.getAuthorities());
            return ResponseEntity.ok(accountViewDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @DeleteMapping(value = "/profile/delete")
    @SecurityRequirement(name = "praveen-api")
    @ApiResponse(responseCode = "200", description = "User delete")
    @ApiResponse(responseCode = "401", description = "Token missing or invalid")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "delete user authorities", description = "Update the authorities of a user")
    public ResponseEntity<String> delete_profile(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            accountService.deleteById(account.getId());
        return ResponseEntity.ok(AccountSuccess.ACCOUNT_DELETED.toString());
        }
        

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


}

    

