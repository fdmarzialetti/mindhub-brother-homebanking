package com.mindhub.homebanking.models;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class AccountTest {
//
//    Account account = new Account("VIN-00000001",1500.0, LocalDateTime.now(),AccountType.CHEKING);
//
//    @Test
//    public void canTransferTest(){
//        assertThat(account.canTransfer(2000.0),equalTo(false));
//    }
//
//    @Test
//    public void substractAmountTest(){
//        account.substractAmount(500.0);
//        assertThat(account.getBalance(),equalTo(1000.0));
//    }
//
//    @Test
//    public void addAmountTest(){
//        account.addAmount(1200.0);
//        assertThat(account.getBalance(),equalTo(2700.0));
//    }

}
