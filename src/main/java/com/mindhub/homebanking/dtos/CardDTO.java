package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

public class CardDTO {
    private long id;
    private CardType type;
    private ColorType color;
    private String cardHolder;
    private String number;
    private int cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.type = card.getType();
        this.color = card.getColor();
        this.cardHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
    }

    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public ColorType getColor() {
        return color;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
}
