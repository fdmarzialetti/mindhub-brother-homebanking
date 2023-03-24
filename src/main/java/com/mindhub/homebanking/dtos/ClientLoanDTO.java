package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class ClientLoanDTO {

    private long id;
    private long loanId;
    private String name;
    private Double amount;
    private Integer payments;

    public ClientLoanDTO(){}

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name=clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayment();
    }

    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }
}
