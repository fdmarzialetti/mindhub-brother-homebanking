package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Util.generateAccountNumber;


@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.findAll().stream().map(a->new AccountDTO(a)).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountService.findById(id));
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication auth){
        Client currentClient = clientService.findByEmail(auth.getName());
        List<AccountDTO> accounts = currentClient.getAccounts().stream().map(a->new AccountDTO(a)).collect(Collectors.toList());
        return accounts;
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> registAccount(
            @RequestParam AccountType accountType,
            Authentication authentication) {
        Client currentClient = clientService.findByEmail(authentication.getName());
        if(currentClient.getAccounts().size()==3){
            return new ResponseEntity<>("The account limit (3) was reached",HttpStatus.FORBIDDEN);
        }
        Account newAccount = new Account(generateAccountNumber(accountService),0.0,LocalDateTime.now(),accountType);
        currentClient.addAccount(newAccount);
        accountService.save(newAccount);
        return new ResponseEntity<>("New account created",HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/deleteAccount")
    public ResponseEntity<Object> deleteAccount(
            @RequestParam String accountNumber,
            @RequestParam long accountID,
            Authentication auth
    ){
        Client client = clientService.findByEmail(auth.getName());
        if(client.getAccounts().stream().noneMatch(a->a.getNumber().equals(accountNumber))){
            return new ResponseEntity<> ("Account not found",HttpStatus.NO_CONTENT);
        }else{
            accountService.deleteById(accountID);
            return new ResponseEntity<>("Delete account succes",HttpStatus.ACCEPTED);
        }
    }

}