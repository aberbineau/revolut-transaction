package fr.berbineau.transaction.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.berbineau.transaction.data.Account;
import fr.berbineau.transaction.data.Transaction;

public class BankTest {
    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(100);
    static int                      count           = 0;

    private Bank bank;

    @BeforeEach
    public void beforeEach() {
        bank = Bank.getInstance();
        initializeBankForTesting();
    }

    private void initializeBankForTesting() {
        bank.initialize();
        for (long l = 1; l < 11; l++) {
            bank.addAccount(new Account(l, INITIAL_BALANCE));
        }
    }

    @Test
    public void checkTransactionIsOk() {
        // Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(10), 1L, 2L);

        // When
        TransactionOutcome outcome = bank.checkTransaction(transaction);

        // Then
        assertEquals(TransactionOutcome.TRANSACTION_OK, outcome);
    }

    @Test
    public void checkTransactionIsErrorBalance() {
        // Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(1000), 1L, 2L);

        // When
        TransactionOutcome outcome = bank.checkTransaction(transaction);

        // Then
        assertEquals(TransactionOutcome.ERROR_SOURCE_ACCOUNT_BALANCE_INSUFFICIENT, outcome);
    }

    @Test
    public void checkTransactionIsErrorSourceAccount() {
        // Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(1000), 11L, 2L);

        // When
        TransactionOutcome outcome = bank.checkTransaction(transaction);

        // Then
        assertEquals(TransactionOutcome.ERROR_SOURCE_ACCOUNT_DOES_NOT_EXIST, outcome);
    }

    @Test
    public void checkTransactionIsErrorDestinationAccount() {
        // Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(1000), 1L, 12L);

        // When
        TransactionOutcome outcome = bank.checkTransaction(transaction);

        // Then
        assertEquals(TransactionOutcome.ERROR_DESTINATION_ACCOUNT_DOES_NOT_EXIST, outcome);
    }

    @Test
    public void executeTransactionIsOk() {
        // Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(10), 1L, 2L);

        // When
        TransactionOutcome outcome = bank.executeTransaction(transaction);
        Account accountOne = bank.getAccountById(1L);
        Account accountTwo = bank.getAccountById(2L);
        List<Transaction> history = bank.getTransactionsHistory();

        // Then
        assertEquals(TransactionOutcome.TRANSACTION_OK, outcome);
        assertEquals(BigDecimal.valueOf(90), accountOne.getBalance());
        assertEquals(BigDecimal.valueOf(110), accountTwo.getBalance());
        assertEquals(1, history.size());
        assertTrue(history.contains(transaction));
    }

    @Test
    public void executeTransactionIsErrorBalance() {
        // Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(1000), 1L, 2L);

        // When
        TransactionOutcome outcome = bank.executeTransaction(transaction);

        // Then
        assertEquals(TransactionOutcome.ERROR_SOURCE_ACCOUNT_BALANCE_INSUFFICIENT, outcome);
    }

    @Test
    public void executeTransactionIsErrorSourceAccount() {
        // Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(10), 11L, 2L);

        // When
        TransactionOutcome outcome = bank.executeTransaction(transaction);

        // Then
        assertEquals(TransactionOutcome.ERROR_SOURCE_ACCOUNT_DOES_NOT_EXIST, outcome);
    }

    @Test
    public void executeTransactionIsErrorDestinationAccount() {
        // Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(10), 1L, 12L);

        // When
        TransactionOutcome outcome = bank.executeTransaction(transaction);

        // Then
        assertEquals(TransactionOutcome.ERROR_DESTINATION_ACCOUNT_DOES_NOT_EXIST, outcome);
    }

    @Test
    public void getTransactionsHistoryIsOk() {
        // Given
        Transaction transactionOne = new Transaction(BigDecimal.valueOf(10), 1L, 2L);
        Transaction transactionTwo = new Transaction(BigDecimal.valueOf(30), 3L, 1L);
        Transaction transactionThree = new Transaction(BigDecimal.valueOf(40), 4L, 5L);

        // When
        bank.executeTransaction(transactionOne);
        bank.executeTransaction(transactionTwo);
        bank.executeTransaction(transactionThree);

        // Then
        assertEquals(3, bank.getTransactionsHistory().size());
        assertEquals(2, bank.getTransactionsHistory(bank.getAccountById(1L)).size());
        assertEquals(1, bank.getTransactionsHistory(bank.getAccountById(2L)).size());
        assertEquals(1, bank.getTransactionsHistory(bank.getAccountById(3L)).size());
        assertEquals(1, bank.getTransactionsHistory(bank.getAccountById(4L)).size());
        assertEquals(1, bank.getTransactionsHistory(bank.getAccountById(5L)).size());
        assertEquals(0, bank.getTransactionsHistory(bank.getAccountById(6L)).size());
    }
}
