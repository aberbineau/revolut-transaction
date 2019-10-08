package fr.berbineau.transaction;

import static spark.Spark.get;

/**
 * The basic app class
 * 
 * @author aberbineau
 *
 */
public class App {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello, world");

        get("/hello/:name", (req, res) -> {
            return "Hello, " + req.params(":name");
        });
    }
}
