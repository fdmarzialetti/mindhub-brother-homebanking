package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transfer(
            @RequestParam String originNumber,
            @RequestParam String destinationNumber,
            @RequestParam Double amount,
            @RequestParam String description,

            Authentication authentication){

//        403 forbidden, si el monto o la descripción estan vacios
        if(amount.equals(null)||description.equals(null)){
            return new ResponseEntity<>("The amount or the description are empty",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si alguno de los numeros de cuenta estan vacios
        if(originNumber.equals(null)||destinationNumber.equals(null)){
            return new ResponseEntity<>("Some of the account numbers are empty",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si la cuenta de origen no existe
        if(!accountService.existsByNumber(originNumber)){
            return new ResponseEntity<>("Origin account does not exist",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si la cuenta de destino no existe
        if(!accountService.existsByNumber(destinationNumber)){
            return new ResponseEntity<>("Destination account does not exist",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si la cuenta de origen no pertenece al cliente autenticado
        if(!clientService.findByEmail(authentication.getName()).getAccounts().stream().noneMatch(a->a.getNumber()==originNumber)){
            return new ResponseEntity<>("The origin account does not belong to the authenticated client",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si el cliente no tiene fondos
        if(!accountService.findByNumber(originNumber).canTransfer(amount)){
            return new ResponseEntity<>("The client has no funds in this account",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si la cuenta de origen es la misma que la destino
        if(originNumber==destinationNumber){
            return new ResponseEntity<>("The origin account is the same as the destination",HttpStatus.FORBIDDEN);
        }
            Account originAccount = accountService.findByNumber(originNumber);
            Account destinationAccount = accountService.findByNumber(destinationNumber);
            Transaction debitTransfer = new Transaction(TransactionType.DEBIT, -amount, description, originAccount.getBalance()-amount);
            Transaction creditTransfer = new Transaction(TransactionType.CREDIT, amount, description,destinationAccount.getBalance()+amount);
            originAccount.substractAmount(amount);
            originAccount.addTransaction(debitTransfer);
            destinationAccount.addAmount(amount);
            destinationAccount.addTransaction(creditTransfer);
            transactionService.save(debitTransfer);
            transactionService.save(creditTransfer);
            //  éxito: 201 created
            return new ResponseEntity<>("The transfer between accounts "+originNumber+" and "+destinationNumber+" has been successful",HttpStatus.CREATED);
    }

    @GetMapping("/transactions")
    public ResponseEntity<Object> getTransactionsBetweenDates(
            Authentication auth,
            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate
            ){

        try {
            LocalDate.parse(startDate);
        }
        catch (DateTimeParseException error) {
           return new ResponseEntity<>("Error Start Date",HttpStatus.BAD_REQUEST);
        }
        try {
            LocalDate.parse(endDate);
        }
        catch (DateTimeParseException error) {
            return new ResponseEntity<>("Error End Date",HttpStatus.BAD_REQUEST);
        }

        LocalDateTime localDateStart = LocalDate.parse(startDate).atTime(0,0);
        LocalDateTime localDateEnd = LocalDate.parse(endDate).atTime(0,0).plusDays(1);

        if(localDateStart.isAfter(localDateEnd)){
            return new ResponseEntity<>("The start date must be before than end date",HttpStatus.BAD_REQUEST);
        }

        if(!accountService.existsByNumber(accountNumber)){
            return new ResponseEntity<>("the account does not exist", HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.findByNumber(accountNumber);

        if(!clientService.findByEmail(auth.getName()).getAccounts().contains(account)){
            return new ResponseEntity<>("the account does not belong to the authorized user", HttpStatus.BAD_REQUEST);
        }

        List<TransactionDTO> transactions= transactionService.findAllByDateBetweenAndAccount(localDateStart,localDateEnd,account)
                .stream()
                .map(t->new TransactionDTO(t))
                .collect(Collectors.toList());

        return new ResponseEntity<>(transactions,HttpStatus.ACCEPTED);
    }
}
