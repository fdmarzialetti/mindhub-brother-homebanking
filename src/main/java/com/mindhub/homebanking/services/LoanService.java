package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Loan;

import java.util.Collection;
import java.util.List;

public interface LoanService {
    List<Loan> findAll();

    Boolean existsByName(String name);

    void save(Loan loan);

    Boolean existsById(long id);

    Loan findById(long id);
}
