package fr.berbineau.transaction;

import static spark.Spark.get;

import java.math.BigDecimal;

import fr.berbineau.transaction.bank.Bank;
import fr.berbineau.transaction.data.Account;

/**
 * The basic app class
 * 
 * @author aberbineau
 *
 */
public class App {
    private static Bank bank = staticInitializer();

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello, world");

        get("/hello/:name", (req, res) -> {
            return "Hello, " + req.params(":name");
        });
    }

    public static Bank staticInitializer() {
        Bank bank = Bank.getInstance();
        for (long l = 1; l < 11; l++) {
            bank.addAccount(new Account(l, BigDecimal.valueOf(100)));
        }
        return bank;
    }
}
