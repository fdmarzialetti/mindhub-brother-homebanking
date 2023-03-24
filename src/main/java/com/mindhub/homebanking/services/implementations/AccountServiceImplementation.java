package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImplementation implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Boolean existsByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }
}
