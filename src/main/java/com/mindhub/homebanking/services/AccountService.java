package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    void save(Account account);

    Boolean existsByNumber(String number);

    List<Account> findAll();

    Account findById(Long id);

    void deleteById(Long id);

    Account findByNumber(String number);
}
