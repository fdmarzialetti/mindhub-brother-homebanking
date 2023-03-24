package com.mindhub.homebanking.dtos;

public class PosnetServiceDTO {
    private String cardNumber;
    private int cardCvv;

    private Double operationAmount;
    private String operationDescription;

    public PosnetServiceDTO(String cardNumber, int cardCvv, Double operationAmount, String operationDescription) {
        this.cardNumber = cardNumber;
        this.cardCvv = cardCvv;
        this.operationAmount = operationAmount;
        this.operationDescription = operationDescription;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCardCvv() {
        return cardCvv;
    }

    public Double getOperationAmount() {
        return operationAmount;
    }

    public String getOperationDescription() {
        return operationDescription;
    }
}
