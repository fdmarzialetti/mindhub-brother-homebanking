package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImplementation implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    public List<Transaction> findAllByDateBetweenAndAccount(LocalDateTime startDate, LocalDateTime endDate, Account account){
        return transactionRepository.findAllByDateBetweenAndAccount(startDate, endDate, account);
    };

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
