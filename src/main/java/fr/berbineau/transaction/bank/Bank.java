package fr.berbineau.transaction.bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.berbineau.transaction.data.Account;
import fr.berbineau.transaction.data.Transaction;
import fr.berbineau.transaction.exceptions.BalanceException;

/**
 * A minimalistic class representing the collection of accounts
 * 
 * @author aberbineau
 *
 */
public class Bank {
    private static Bank        instance;
    private Map<Long, Account> accounts;
    private List<Transaction>  transactionsHistory;

    private Bank() {
        initialize();
    }

    public static Bank getInstance() {
        if (null == instance) {
            instance = new Bank();
        }
        return instance;
    }

    public Account getAccountById(Long id) {
        return accounts.get(id);
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Map<Long, Account> getAccounts() {
        return accounts;
    }

    /**
     * @return a shallow copy of the bank's transactions history
     */
    public List<Transaction> getTransactionsHistory() {
        return new ArrayList<>(transactionsHistory);
    }

    /**
     * Add a transaction to the history
     * 
     * @param transaction
     */
    private void addTransactionToHistory(Transaction transaction) {
        transactionsHistory.add(transaction);
    }

    /**
     * Check if a transaction can safely be executed. Since transactions are not
     * synchronized, the result is only indicative as a conflicting transaction
     * can be executed before this one
     * 
     * @param transaction
     *            the transaction that needs checking
     * @return the projected outcome of the transaction
     */
    public TransactionOutcome checkTransaction(Transaction transaction) {
        Account source = accounts.get(transaction.getSourceAccountNumber());
        Account destination = accounts.get(transaction.getDestinationAccountNumber());
        BigDecimal transactionAmount = transaction.getAmount();

        if (null == source) {
            return TransactionOutcome.ERROR_SOURCE_ACCOUNT_DOES_NOT_EXIST;
        }

        if (null == destination) {
            return TransactionOutcome.ERROR_DESTINATION_ACCOUNT_DOES_NOT_EXIST;
        }

        if (source.getBalance().compareTo(transactionAmount) < 0) {
            return TransactionOutcome.ERROR_SOURCE_ACCOUNT_BALANCE_INSUFFICIENT;
        }

        return TransactionOutcome.TRANSACTION_OK;
    }

    /**
     * Execute a transaction
     * 
     * @param transaction
     *            the transaction to be executed
     * @return the outcome of the transaction
     */
    public TransactionOutcome executeTransaction(Transaction transaction) {
        TransactionOutcome outcome = checkTransaction(transaction);

        if (TransactionOutcome.TRANSACTION_OK != outcome) {
            return outcome;
        }

        Account source = accounts.get(transaction.getSourceAccountNumber());
        Account destination = accounts.get(transaction.getDestinationAccountNumber());

        try {
            source.sendTransaction(transaction);
            destination.receiveTransaction(transaction);
            addTransactionToHistory(transaction);
        } catch (BalanceException be) {
            return TransactionOutcome.ERROR_SOURCE_ACCOUNT_BALANCE_INSUFFICIENT;
        }

        return TransactionOutcome.TRANSACTION_OK;
    }

    /**
     * Returns a list of an account's transactions history
     * 
     * @param account
     *            the account for which the history is wanted
     * @return the transaction history of this account
     */
    public List<Transaction> getTransactionsHistory(Account account) {
        return transactionsHistory.stream()
                .filter(t -> t.getSourceAccountNumber().equals(account.getAccountNumber())
                        || t.getDestinationAccountNumber().equals(account.getAccountNumber()))
                .collect(Collectors.toList());
    }

    private void initialize() {
        accounts = new HashMap<>();
        transactionsHistory = new ArrayList<>();
    }
}
