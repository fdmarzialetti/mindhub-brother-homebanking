package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LoanServiceImplementation implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public Boolean existsByName(String name) {
        return loanRepository.existsByName(name);
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public Boolean existsById(long id) {
        return loanRepository.existsById(id);
    }

    @Override
    public Loan findById(long id) {
        return loanRepository.findById(id);
    }
}
