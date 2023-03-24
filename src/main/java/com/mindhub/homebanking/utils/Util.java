package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class Util {

    static public Integer generateCardCvv(){
        String candidateChars="1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }
        return Integer.valueOf(sb.toString());
    }

    static public String generateAccountNumber(AccountService accountService){
        String candidateChars="1234567890";
        String number="";
        int length=8;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        do{
            for (int i = 0; i < length; i++) {
                sb.append(candidateChars.charAt(random.nextInt(candidateChars
                        .length())));
            }
            number = "VIN-"+sb.toString();
        }while(accountService.existsByNumber(number));
        return number;
    }

    static public String generateCardNumber(CardService cardService){
        String candidateChars="1234567890";
        StringBuilder sb = new StringBuilder();
        String number;
        Random random = new Random();
        do {
        for (int i = 0; i < 16; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
            }
        for(int j = 4; j<=14;j+=5){
            sb.insert(j,"-");
            }
        number = sb.toString();
        }while(cardService.existsByNumber(number));
        return number;
    }

}
