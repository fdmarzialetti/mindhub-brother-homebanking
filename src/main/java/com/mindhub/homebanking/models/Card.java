package com.mindhub.homebanking.models;

import com.mindhub.homebanking.repositories.ClientRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    private long id;
    private CardType type;
    private ColorType color;
    private String cardHolder;
    private String number;
    private int cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Card(){};

    public Card(CardType type, ColorType color, String cardHolder, String number, int cvv, LocalDate thruDate, LocalDate fromDate) {
        this.type = type;
        this.color = color;
        this.cardHolder = cardHolder;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
    }

    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public ColorType getColor() {
        return color;
    }

    public void setColor(ColorType color) {
        this.color = color;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Client getClient(){
        return client;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public Boolean equalNumber(String number){
        return this.number==number;
    }

    public boolean isExpired() {
        if (LocalDate.now().isAfter(thruDate)||thruDate.equals(LocalDate.now())){
            return true;
        }
        return false;
    }
}
