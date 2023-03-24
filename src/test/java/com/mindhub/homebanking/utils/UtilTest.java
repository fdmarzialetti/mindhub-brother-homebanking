package com.mindhub.homebanking.utils;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static com.mindhub.homebanking.utils.Util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class UtilTest {

//    @Autowired
//    private CardService cardService;
//
//    @Autowired
//    private AccountService accountService;
//    @Test
//    public void generateCardCvvTest() {
//        assertThat(generateCardCvv(),allOf(
//                greaterThan(0),
//                lessThan(1000)));
//    }
//
//    @Test
//    public void generateAccountNumberTest(){
//        assertThat(generateAccountNumber(accountService).length(),equalTo(12));
//        assertThat(generateCardNumber(cardService),startsWith("VIN-"));
//    }
//
//    @Test
//    public void generateCardNumberTest(){
//        assertThat(generateCardNumber(cardService).length(),equalTo(19));
//        assertThat(generateCardNumber(cardService),isA(String.class));
//    }

}