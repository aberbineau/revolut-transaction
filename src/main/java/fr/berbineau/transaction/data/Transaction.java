package fr.berbineau.transaction.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A minimalistic class representing a money transfer between two accounts
 * 
 * @author aberbineau
 *
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = -4662439656697474572L;
    private BigDecimal        amount;
    private Long              sourceAccountNumber;
    private Long              destinationAccountNumber;

    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Instantiate a transaction
     * 
     * @param amount
     *            the amount of the transaction
     * @param sourceAccountNumber
     *            the account number from which the money is to be drafted
     * @param destinationAccountNumber
     *            the account number to which the money is to be added
     * @throws IllegalArgumentException
     *             if the transaction amount is zero or less
     */
    public Transaction(BigDecimal amount, Long sourceAccountNumber, Long destinationAccountNumber)
            throws IllegalArgumentException {
        super();
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("The amount of a transaction cannot be negative or zero");
        }
        this.amount = amount;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(Long sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public Long getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(Long destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }
}
