package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		String date = "2010-05-05";
		LocalDate localDate = LocalDate.parse(date);
		System.out.println(localDate);
		System.out.println("Running program!");
	}

	@Bean
	public CommandLineRunner initData(
			ClientRepository clientRepository,
			AccountRepository accountRepository,
			TransactionRepository transactionRepository,
			LoanRepository loanRepository,
			ClientLoanRepository clientLoanRepository,
			CardRepository cardRepository
			) {
		return (args) -> {
//			 save a couple of clients
//			Client client1 = new Client("Melba", "Lorenzo", "melba@mindhub.com", passwordEncoder.encode("melba"));
//			Client client2 = new Client("Fernando", "Marzialetti", "fdmarzialetti@gmail.com", passwordEncoder.encode("12345"));
//			Client admin = new Client ("admin","admin","admin@admin.com", passwordEncoder.encode("admin"));
//			Account vin001 = new Account("VIN001", 250000.0, LocalDateTime.now(),AccountType.CHEKING);
//			Account vin002 = new Account("VIN002", 37500.0, LocalDateTime.now().plusDays(1),AccountType.CHEKING);
//			Account vin003 = new Account("NIV001", 3500.0, LocalDateTime.now().plusDays(2),AccountType.CHEKING);
//			Account vin004 = new Account("NIV002", 2000.0, LocalDateTime.now().minusDays(2),AccountType.CHEKING);
//			Account vin005 = new Account("NIV004", 800.0, LocalDateTime.now(),AccountType.CHEKING);
//			Loan loan1 = new Loan("Hipotecary",500000.0, Arrays.asList(6,12,24,36,48,60),20.0);
//			Loan loan2 = new Loan("Personal",100000.0,Arrays.asList(6,12,24),30.0);
//			Loan loan3 = new Loan("Automoive",300000.0,Arrays.asList(6,12,24,36),15.0);
//			ClientLoan clientLoan1 = new ClientLoan(400000.0,60,client1,loan1);
//			ClientLoan clientLoan2 = new ClientLoan(50000.0,12,client1,loan2);
//			ClientLoan clientLoan3 = new ClientLoan(100000.0,24,client2,loan2);
//			ClientLoan clientLoan4 = new ClientLoan(200000.0,36,client2,loan3);
//			Card card1 = new Card(CardType.DEBIT,ColorType.GOLD,"MelbaLorenzo","1111-1111-1111-1111",111,LocalDate.now().plusYears(5),LocalDate.now());
//			Card card2 = new Card(CardType.CREDIT,ColorType.TITANIUM,"MelbaLorenzo","2222-2222-2222-2222",222,LocalDate.now().plusYears(5),LocalDate.now());
//			Card card3 = new Card(CardType.DEBIT,ColorType.SILVER,"FernandoMarzialetti","3333-3333-3333-3333",333,LocalDate.now().plusYears(5),LocalDate.now());
//			loanRepository.save(loan1);
//			loanRepository.save(loan2);
//			loanRepository.save(loan3);
//			generateRandomTransactions(vin002,50);
//			generateRandomTransactions(vin001,50);
//			client1.addAccount(vin001);
//			client1.addAccount(vin002);
//			client2.addAccount(vin003);
//			client2.addAccount(vin004);
//			client2.addAccount(vin005);
//			client1.addCard(card1);
//			client1.addCard(card2);
//			client1.addCard(card3);
//			clientRepository.save(client1);
//			clientRepository.save(client2);
//			clientRepository.save(admin);
//			cardRepository.save(card1);
//			cardRepository.save(card2);
//			cardRepository.save(card3);
//			saveAccountsAndTransactions(client1, accountRepository, transactionRepository);
//			saveAccountsAndTransactions(client2, accountRepository, transactionRepository);
//			clientLoanRepository.save(clientLoan1);
//			clientLoanRepository.save(clientLoan2);
//			clientLoanRepository.save(clientLoan3);
//			clientLoanRepository.save(clientLoan4);
		};
	}
	public static void generateRandomTransactions(Account account, int cant){
		Random rand = new Random();
		TransactionType type;
		Double amount;
		for (int i=0;i<cant;i++){
			String description = "Description "+Integer.toString(i);
			amount = new Double(rand.nextInt(1000));
			if(rand.nextBoolean()){
				type=TransactionType.DEBIT;
				amount=new Double(rand.nextInt(500))*-1;
			}else{
				type= TransactionType.CREDIT;
			}
			Transaction trans = new Transaction(type,amount,description,0.0);
			trans.setDate(LocalDateTime.now().plusMonths(rand.nextInt(9)).plusDays(rand.nextInt(10)));
			account.addTransaction(trans);
		}
	}
	public static void saveAccountsAndTransactions(Client client, AccountRepository accountRepository, TransactionRepository transactionRepository){
		for (Account account: client.getAccounts()){
			accountRepository.save(account);
			for(Transaction transaction: account.getTransactions()){
				transactionRepository.save(transaction);
			}
		}
	}
}
