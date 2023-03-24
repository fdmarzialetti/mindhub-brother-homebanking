package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
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
public class AccountRepositoryTest {
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    @Test
//    public void existAccounts() {
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts, is(not(empty())));
//    }
//
//    @Test
//    public void existVIN001Account() {
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));
//    }

}
