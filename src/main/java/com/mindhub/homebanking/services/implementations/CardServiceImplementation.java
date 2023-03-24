package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplementation implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Boolean existsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }


}
