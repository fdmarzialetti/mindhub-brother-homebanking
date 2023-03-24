package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class TransactionDTO {
    private long id;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;
    private Account account;

    private Double currentBalance;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.account = transaction.getAccount();
        this.currentBalance= transaction.getCurrentBalance();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }
    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }
}
