package fr.berbineau.transaction;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.Gson;

import fr.berbineau.transaction.bank.Bank;
import fr.berbineau.transaction.bank.TransactionOutcome;
import fr.berbineau.transaction.data.Account;
import fr.berbineau.transaction.data.Transaction;
import fr.berbineau.transaction.dto.PostTransactionResponse;
import fr.berbineau.transaction.exceptions.AccountException;

/**
 * The basic app class
 * 
 * @author aberbineau
 *
 */
public class App {
    private static Bank bank = staticInitializer();
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(80);

        get("/account/:id", (request, response) -> {
            try {
                String json = getAccountById(request.params(":id"));
                response.type("application/json");
                return json;
            } catch (NumberFormatException nfe) {
                response.status(400);
            } catch (AccountException ae) {
                response.status(404);
            }
            return ("Invalid ID");
        });

        get("/account/:id/history", (request, response) -> {
            try {
                String json = getAccountHistory(request.params(":id"));
                response.type("application/json");
                return json;
            } catch (NumberFormatException nfe) {
                response.status(400);
            }
            return ("Invalid ID");
        });

        get("/account", (request, response) -> {
            response.type("application/json");
            return getAccounts();
        });

        post("/transaction", (request, response) -> {
            response.type("application/json");
            Transaction transaction = gson.fromJson(request.body(), Transaction.class);
            return postTransaction(transaction);
        });
    }

    private static String getAccountById(String strId) throws AccountException, NumberFormatException {
        Long longId = Long.parseLong(strId);
        Account account = bank.getAccountById(longId);
        if (null == account) {
            throw new AccountException();
        }
        return gson.toJson(account);
    }

    private static String getAccountHistory(String strId) throws NumberFormatException {
        Long longId = Long.parseLong(strId);
        List<Transaction> history = bank.getTransactionsHistory(longId);
        if (history.isEmpty()) {
        }
        return gson.toJson(history);
    }

    private static String getAccounts() {
        return gson.toJson(bank.getAccounts().values());
    }

    private static String postTransaction(Transaction transaction) {
        Account sourceAccount = bank.getAccountById(transaction.getSourceAccountNumber());
        Account destinationAccount = bank.getAccountById(transaction.getDestinationAccountNumber());
        TransactionOutcome outcome = bank.executeTransaction(transaction);
        PostTransactionResponse json = new PostTransactionResponse(outcome, sourceAccount, destinationAccount);
        return gson.toJson(json);
    }

    /**
     * This static initializer initializes ten accounts in the bank
     * 
     * Outside of a POC setting the account holding should be done via a data
     * base (on disk for persistance or in-memory for a more advanced POC)
     * 
     * @return
     */
    public static Bank staticInitializer() {
        Bank bank = Bank.getInstance();
        for (long l = 1; l < 11; l++) {
            bank.addAccount(new Account(l, BigDecimal.valueOf(100)));
        }
        return bank;
    }
}
