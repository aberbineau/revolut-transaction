package fr.berbineau.transaction.data;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.berbineau.transaction.exceptions.BalanceException;

/**
 * A minimalistic class representing an account holding money
 * 
 * @author aberbineau
 *
 */
public class Account implements Serializable {
    private static final long serialVersionUID = -7230363099602687444L;
    private Long              accountNumber;
    private BigDecimal        balance;

    public Account(Long accountNumber, BigDecimal balance) {
        super();
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void sendTransaction(Transaction transaction) throws BalanceException {
        if (!accountNumber.equals(transaction.getSourceAccountNumber())) {
            throw new IllegalArgumentException("The transaction is not from this account " + accountNumber.toString());
        }

        if (balance.compareTo(transaction.getAmount()) < 0) {
            throw new BalanceException("The amount on the account is insufficient");
        }

        BigDecimal newBalance = balance.subtract(transaction.getAmount());
        balance = newBalance;
    }

    public void receiveTransaction(Transaction transaction) {
        if (!accountNumber.equals(transaction.getDestinationAccountNumber())) {
            throw new IllegalArgumentException("The transaction is not to this account: " + accountNumber.toString());
        }
        BigDecimal newBalance = balance.add(transaction.getAmount());
        balance = newBalance;
    }
}
