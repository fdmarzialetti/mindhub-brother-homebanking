package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    void save(Transaction transaction);

    List<Transaction> findAllByDateBetweenAndAccount(LocalDateTime startDate, LocalDateTime endDate, Account account);

}
