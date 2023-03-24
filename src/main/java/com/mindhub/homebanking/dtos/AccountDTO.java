package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;
    private String number;
    private LocalDateTime creationDate;
    private Double balance;
    private List<TransactionDTO> transactions;

    private AccountType type;
    public AccountDTO(Account account){
        this.id= account.getId();
        this.number= account.getNumber();
        this.creationDate= account.getCreationDate();
        this.balance= account.getBalance();
        this.transactions=account.getTransactions()
               .stream()
                .map(transaction->new TransactionDTO(transaction))
                .sorted((t1,t2)->t2.getDate().compareTo(t1.getDate()))
                .collect(Collectors.toList());
        this.type=account.getType();
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }


    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public AccountType getType() {
        return type;
    }
}
