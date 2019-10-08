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

    /**
     * Applies an outgoing transaction from this account by removing the amount
     * from the account balance
     * 
     * @param transaction
     *            the transaction to be applied
     * @throws BalanceException
     *             if the account balance is less than the transaction amount
     * @throws IllegalArgumentException
     *             if the transaction source account number does not match this
     *             account number
     */
    public void sendTransaction(Transaction transaction) throws BalanceException, IllegalArgumentException {
        if (!accountNumber.equals(transaction.getSourceAccountNumber())) {
            throw new IllegalArgumentException("The transaction is not from this account " + accountNumber.toString());
        }

        if (balance.compareTo(transaction.getAmount()) < 0) {
            throw new BalanceException("The amount on the account is insufficient");
        }

        BigDecimal newBalance = balance.subtract(transaction.getAmount());
        balance = newBalance;
    }

    /**
     * Applies an incoming transaction to this account by adding the amount to
     * the account balance
     * 
     * @param transaction
     *            the transaction to be applied
     * @throws IllegalArgumentException
     *             if the transaction destination account number does not match
     *             this account number
     */
    public void receiveTransaction(Transaction transaction) throws IllegalArgumentException {
        if (!accountNumber.equals(transaction.getDestinationAccountNumber())) {
            throw new IllegalArgumentException("The transaction is not to this account: " + accountNumber.toString());
        }
        BigDecimal newBalance = balance.add(transaction.getAmount());
        balance = newBalance;
    }
}
