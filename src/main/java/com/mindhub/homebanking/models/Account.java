package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private Double balance;

    private AccountType type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account",fetch=FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();
    protected Account (){};
    public Account(String number, Double balance, LocalDateTime creationDate, AccountType type){
        this.number=number;
        this.balance=balance;
        this.creationDate=creationDate;
        this.type=type;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }


    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        this.transactions.add(transaction);
    }

    public Boolean canTransfer(Double amount){
        return balance>=amount;
    }

    public void addAmount(Double amount){
        this.balance+=amount;
    }

    public void substractAmount(Double amount){
        this.balance-=amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                ", owner=" + client +
                '}';
    }

}
