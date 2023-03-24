package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {

    void save(Card card);

    Boolean existsByNumber(String number);

    Card findByNumber(String number);
    void deleteById(Long id);

}
