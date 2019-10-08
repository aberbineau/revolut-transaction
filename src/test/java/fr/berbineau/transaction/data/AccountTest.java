package fr.berbineau.transaction.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import fr.berbineau.transaction.exceptions.BalanceException;

public class AccountTest {

    @Test
    public void sendTransactionIsOk() throws BalanceException {
        // Given
        Account account = new Account(1L, BigDecimal.valueOf(15.0));
        Transaction transaction = new Transaction(BigDecimal.valueOf(7.8), 1L, 2L);

        // When
        account.sendTransaction(transaction);

        // Then
        assertEquals(BigDecimal.valueOf(7.2), account.getBalance());
    }

    @Test
    public void sendTransactionShouldThrowExceptionWhenAccountNumbersDoNotMatch() throws BalanceException {
        // Given
        Account account = new Account(1L, BigDecimal.valueOf(15.0));
        Transaction transaction = new Transaction(BigDecimal.valueOf(7.8), 3L, 2L);

        // Then
        assertThrows(IllegalArgumentException.class, () -> account.sendTransaction(transaction));
    }

    @Test
    public void sendTransactionShouldThrowExceptionWhenAccountIsOverdrafted() throws BalanceException {
        // Given
        Account account = new Account(1L, BigDecimal.valueOf(15.0));
        Transaction transaction = new Transaction(BigDecimal.valueOf(16.0), 1L, 2L);

        // Then
        assertThrows(BalanceException.class, () -> account.sendTransaction(transaction));
    }

    @Test
    public void receiveTransactionIsOk() {
        // Given
        Account account = new Account(2L, BigDecimal.valueOf(15.0));
        Transaction transaction = new Transaction(BigDecimal.valueOf(7.8), 1L, 2L);

        // When
        account.receiveTransaction(transaction);

        // Then
        assertEquals(BigDecimal.valueOf(22.8), account.getBalance());
    }

    @Test
    public void receiveTransactionShouldThrowExceptionWhenAccountNumbersDoNotMatch() {
        // Given
        Account account = new Account(1L, BigDecimal.valueOf(15.0));
        Transaction transaction = new Transaction(BigDecimal.valueOf(7.8), 3L, 2L);

        // Then
        assertThrows(IllegalArgumentException.class, () -> account.receiveTransaction(transaction));
    }

}
