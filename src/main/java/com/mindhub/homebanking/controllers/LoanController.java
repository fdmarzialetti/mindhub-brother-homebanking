package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.findAll().stream().map(l->new LoanDTO(l)).collect(Collectors.toList());
    }

    @PostMapping("/admin/loan")
    public ResponseEntity<Object> createLoan(
            @RequestParam String name,
            @RequestParam Double maxAmount,
            @RequestParam Double percent,
            @RequestParam List<Integer> payments,
            Authentication auth){

        Boolean errorFound=false;
//      respuestas de error:
        List<String> errorMsgs = new ArrayList<>();
        if(name==""){
            errorFound=true;
            errorMsgs.add("Missing loan name");
        }
        if(maxAmount<1 || maxAmount>200000){
            errorFound=true;
            errorMsgs.add("Invalid loan max amount");
        }
        if(percent<0){
            errorFound=true;
            errorMsgs.add("Invalid loan percent");
        }
        if(payments.isEmpty()){
            errorFound=true;
            errorMsgs.add("Missing payments");
        }
        if(loanService.existsByName(name)){
            errorFound =true;
            errorMsgs.add("Loan with the same name already exists");
        }
//      Send ErrorMsgs
        if(errorFound){
            return new ResponseEntity<>(errorMsgs,HttpStatus.FORBIDDEN);
        }

        Loan newLoan = new Loan(name,maxAmount,payments,percent);
        loanService.save(newLoan);
        return new ResponseEntity<>("Create Loan Success",HttpStatus.CREATED);

    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> requestLoan(@RequestBody LoanApplicationDTO loan, Authentication auth){

        if(loan.getId()<=0){
            return new ResponseEntity<>("Invalid loan id",HttpStatus.FORBIDDEN);
        }
        if(loan.getAmount()<1){
            return new ResponseEntity<>("Invalid amount (must be 1 or higher)",HttpStatus.FORBIDDEN);
        }
        if(loan.getPayments()<=0){
            return new ResponseEntity<>("Invalid payments amount",HttpStatus.FORBIDDEN);
        }
        if(loan.getAccountNumber()==""){
            return new ResponseEntity<>("Missing account number",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si la cuenta de destino no existe
        if(!accountService.existsByNumber(loan.getAccountNumber())){
            return new ResponseEntity<>("Destiny account does not exist",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si la cuenta de destino no pertenece al cliente autenticado
        if(!(accountService.findByNumber(loan.getAccountNumber()).getClient().getEmail()).equals(auth.getName())){
            return new ResponseEntity<>("The destination account does not belong to the authenticated client",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si el préstamo no existe
        if(!loanService.existsById(loan.getId())){
            return new ResponseEntity<>("Loan does not exist",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si el monto solicitado supera el monto máximo permitido del préstamo solicitado
        if(loanService.findById(loan.getId()).getMaxAmount()<loan.getAmount()){
            return new ResponseEntity<>("Requested amount exceeds the maximum allowable amount of the requested loan",HttpStatus.FORBIDDEN);
        }
//        403 forbidden, si la cantidad de cuotas no está disponible para el préstamo solicitado
        if(loanService.findById(loan.getId()).getPayments().stream().noneMatch(p->p==loan.getPayments())){
            return new ResponseEntity<>(loan.getPayments()+" is not a payment available for the requested loan",HttpStatus.FORBIDDEN);
        }

        if(clientService.findByEmail(auth.getName()).getLoans().stream().anyMatch(l->l.getId()==loan.getId())){
            return new ResponseEntity<>("you already have this loan",HttpStatus.FORBIDDEN);
        }

        Client clientFound = clientService.findByEmail(auth.getName());
        Loan loanFound = loanService.findById(loan.getId());
        Account accountFound = accountService.findByNumber(loan.getAccountNumber());
        ClientLoan newClientLoan = new ClientLoan(
                loan.getAmount()+((loanFound.getPercent()*loan.getAmount())/100),
                loan.getPayments(),
                clientFound,
                loanFound
        );
        Transaction newTransaction = new Transaction(
                TransactionType.CREDIT,
                loan.getAmount(),
                loanFound.getName()+" loan approved",
                accountFound.getBalance()+loan.getAmount()
        );

        accountFound.addTransaction(newTransaction);
        accountFound.addAmount(loan.getAmount());
        clientFound.addClientLoan(newClientLoan);
        loanFound.addClientLoan(newClientLoan);
        clientLoanService.save(newClientLoan);
        transactionService.save(newTransaction);

//       éxito: 201 created
        return new ResponseEntity<>("Succesfull Request",HttpStatus.CREATED);

    }
}
