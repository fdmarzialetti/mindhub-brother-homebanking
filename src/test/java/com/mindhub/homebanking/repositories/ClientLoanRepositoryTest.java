package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.ClientLoan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class ClientLoanRepositoryTest {
//
//    @Autowired
//    ClientLoanRepository clientLoanRepository;
//
//    @Test
//    public void existClientLoans(){
//
//        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
//        assertThat(clientLoans,is(not(empty())));
//    }
//    @Test
//    public void existPayments12ClientLoan(){
//        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
//        assertThat(clientLoans, hasItem(hasProperty("payment", is(12))));
//    }
}
