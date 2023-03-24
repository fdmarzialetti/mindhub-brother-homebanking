package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.util.ArrayList;
import java.util.List;

public class LoanDTO {
    private long id;
    private String name;
    private Double maxAmount;
    private List<Integer> payments;

    private Double percent;

    public LoanDTO(Loan loan){
        id=loan.getId();
        name=loan.getName();
        maxAmount=loan.getMaxAmount();
        payments=loan.getPayments();
        percent=loan.getPercent();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Double getPercent() {
        return percent;
    }
}
