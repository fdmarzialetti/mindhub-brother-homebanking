package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.PosnetServiceDTO;
import com.mindhub.homebanking.models.*;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Random;

import static com.mindhub.homebanking.utils.Util.generateCardCvv;
import static com.mindhub.homebanking.utils.Util.generateCardNumber;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/clients/current/deleteCard")
    public ResponseEntity<Object> deleteCard(
            @RequestParam long cardID,
            Authentication auth
            ){
     Client client = clientService.findByEmail(auth.getName());
     if(client.getCards().stream().noneMatch(c->c.getId()==cardID)){
         return new ResponseEntity<> ("Card not found",HttpStatus.NO_CONTENT);
     }else{
         cardService.deleteById(cardID);
         return new ResponseEntity<>("Delete card succes",HttpStatus.ACCEPTED);
     }

    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> registCard(
            @RequestParam CardType cardType, @RequestParam ColorType colorType, Authentication authentication) {

        Client currentClient= clientService.findByEmail(authentication.getName());
        if((currentClient.getCards()
                .stream()
                .anyMatch(card-> card.getColor()==colorType && card.getType()==cardType))) {
            return new ResponseEntity<>("Already have a "+
                    String.valueOf(colorType).toLowerCase()+" "+
                    String.valueOf(cardType).toLowerCase()+" card",HttpStatus.FORBIDDEN);
        }

        Card newCard = new Card(
                cardType,
                colorType,
                currentClient.getFirstName()+" "+currentClient.getLastName(),
                generateCardNumber(cardService),
                generateCardCvv(),
                LocalDate.now().plusYears(5),
                LocalDate.now());
        currentClient.addCard(newCard);
        cardService.save(newCard);
        return new ResponseEntity<>("New card created",HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/card/payment")
    @CrossOrigin
    public ResponseEntity<Object> cardPayment(
            @RequestBody PosnetServiceDTO posnetService
            )
    {
        Card card = cardService.findByNumber(posnetService.getCardNumber());

        //Verify that the amount of the operation is greater than 0
        if(posnetService.getOperationAmount()<0.0){
            return new ResponseEntity<>("Invalid amount, must be greater than 0",HttpStatus.NOT_ACCEPTABLE);
        }

        //Verify that the description of the operation is not empty.
        if(posnetService.getOperationDescription()==""){
            return new ResponseEntity<>("Missing description",HttpStatus.NOT_ACCEPTABLE);
        }

        //Verify that the card exists.
        if(card == null){
            return new ResponseEntity<>("Card not found",HttpStatus.NOT_ACCEPTABLE);
        }

        //Verify that the card cvv is valid.
        if(card.getCvv()!=posnetService.getCardCvv()){
            return new ResponseEntity<>("Card not found",HttpStatus.NOT_ACCEPTABLE);
        }

        //Check the expiration date of the card
        if(card.isExpired()){
            return new ResponseEntity<>("the card is expired",HttpStatus.NOT_ACCEPTABLE);
        }

        Account account  = card
                .getClient()
                .getAccounts()
                .stream()
                .max(Comparator.comparing(Account::getBalance)).get();

        if(!account.canTransfer(posnetService.getOperationAmount())){
            return new ResponseEntity<>("The account does not have sufficient funds",HttpStatus.NOT_ACCEPTABLE);
        }

        Transaction newTransaction = new Transaction(
                TransactionType.DEBIT,
                -posnetService.getOperationAmount(),
                posnetService.getOperationDescription(),
                account.getBalance()-posnetService.getOperationAmount()
                );

        account.substractAmount(posnetService.getOperationAmount());
        account.addTransaction(newTransaction);
        transactionService.save(newTransaction);
        accountService.save(account);
        return new ResponseEntity<>("Payment Success",HttpStatus.ACCEPTED);
    }



}
