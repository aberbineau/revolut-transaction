# Revolut Homework Assignment
This is my homework assignment for the recruiting process by Revolut. The task was to produce a simple POC service to handle money transactions between accounts.

For simplicity's sake a static initializer has been used on app launch to populate the accounts known by the service. It is assumed that should this progress beyond a simple POC, a database would be used.


## Build
The project is built with maven. The `package` goal is used to build the executable jar.
The generated artifact is named *revolut-transaction-[version]-jar-with-dependencies.jar*


## Execution
The jar is executed with the java program with the `-jar` flag

Assuming the user is in the same directory as the archive, the command is

`java -jar revolut-transaction-[version]-jar-with-dependencies.jar`
The service listens on port 80. This can be changed by editing the `main` method in the App.java file and rebuilding the artifact.


## JSON
In the *json* folder, a *transaction.json* file is provided. This file provide the prototype for the data expected when posting a transaction to the service
## API
This is a description of the accessible URL's and the HTTP verb assigned
### GET /account
This method returns a list all the accounts known to the service
### GET /account/[id]
This method returns a single account from its id.

The parameter `id` is a positive integer.

If the service does not know the account, it returns an HTTP code 404 (not found)

If the `id` parameter is not an integer, it returns an HTTP code 400 (bad request)


### GET /account/[id]/history
This method returns a list containing the transaction history of an account from its id.

The parameter `id` is a positive integer.

If the service does not know the account, or if the account has no transaction history, it returns an empty list

If the `id` parameter is not an integer, it returns an HTTP code 400 (bad request)


### POST /transaction
This method is used to post a transaction.

It expects a json in the same format as in the file `/json/transaction.json` provided in the project directory

It returns a json with: 
 - The outcome of the transaction
	- `TRANSACTION_OK` if the transaction proceeded
	- `ERROR_SOURCE_ACCOUNT_BALANCE_INSUFFICIENT` if the balance of the source account was insufficient to cover the transaction
	- `ERROR_SOURCE_ACCOUNT_DOES_NOT_EXIST` if the source account does not exist
	- `ERROR_DESTINATION_ACCOUNT_DOES_NOT_EXIST` if the destination account does not exist
- The source account in its post-transaction state
- The destination account in its post-transaction state

