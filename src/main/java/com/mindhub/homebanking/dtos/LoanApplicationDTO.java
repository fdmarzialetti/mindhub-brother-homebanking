package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private Double amount;
    private Integer payments;

    private String accountNumber;

    public LoanApplicationDTO(long id, Double amount, Integer payments, String accountNumber) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.accountNumber = accountNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
