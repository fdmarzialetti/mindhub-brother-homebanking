package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Util.generateAccountNumber;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;


    @PostMapping("/clients")
    public ResponseEntity<Object> registClient(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {


        String errorMsg="Missing: ";
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            if(firstName.isEmpty())errorMsg+= " First name";
            if (lastName.isEmpty())errorMsg+=" Last name";
            if(email.isEmpty())errorMsg+=" Email";
            if(password.isEmpty()) errorMsg+=" Password";
            return new ResponseEntity<>(errorMsg, HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        Account newAccount = new Account(generateAccountNumber(accountService),0.0, LocalDateTime.now(), AccountType.CHEKING);
        newClient.addAccount(newAccount);
        clientService.save(newClient);
        accountService.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.findAll()
                .stream()
                .map(c->new ClientDTO(c))
                .collect(Collectors.toList());
    }
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientService.findById(id));
    }
    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }

}


