package fr.berbineau.transaction.dto;

import fr.berbineau.transaction.bank.TransactionOutcome;
import fr.berbineau.transaction.data.Account;

/**
 * A class representing the result of the transaction
 * 
 * the sourceAccount and destinationAccount are expected to hold the state of
 * the respective accounts after the transactions but the proper behaviour is
 * delegated to the developper
 * 
 * @author aberbineau
 *
 */
public class PostTransactionResponse {

    private TransactionOutcome outcome;
    private Account            sourceAccount;
    private Account            destinationAccount;

    public PostTransactionResponse(TransactionOutcome outcome, Account sourceAccount, Account destinationAccount) {
        this.outcome = outcome;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }

    public TransactionOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(TransactionOutcome outcome) {
        this.outcome = outcome;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

}
