package com.example.pra5.bank;

import com.example.pra5.bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PrivateBank implements Bank {
    /**************************** Variables **********************************/
    /**
     * Dieses Attribut soll den Namen der privaten Bank repräsentieren.
     * */
    private String name;


    /**
     * Gibt den Speicherort der Konten bzw. Transaktionen.
     */
    private String directoryName;


    private Path fullPath;


    /**
     * Dieses Attribut gibt die Zinsen bzw. den Zinssatz (positiver Wert in
     * Prozent, 0 bis 1) an, die bei einer Einzahlung (Deposit) anfallen. Dieses Attribut soll identisch zu dem
     * gleichnamigen Attribut der Klasse Payment sein.
     * */
    private double incomingInterest;

    /**
     * Dieses Attribut gibt die Zinsen bzw. den Zinssatz (positiver Wert in
     * Prozent, 0 bis 1) an, die bei einer Auszahlung (Withdrawal) anfallen. Dieses Attribut soll identisch zu
     * dem gleichnamigen Attribut der Klasse Payment sein.
     * */

    private double outgoingInterest;

    /**
     * Dieses  Attribut  soll  Konten  mit
     * Transaktionen verknüpfen, so dass jedem gespeicherten Konto 0 bis n Transaktionen zugeordnet
     * werden können. Der Schlüssel steht für den Namen des Kontos.
     * Beispiel: „Konto 1“ -> [Transaktion 1, Transaktion2]
     * „Konto Adam“ -> [Transaktion 3]
     * „Konto Eva“ -> []
     * */
    protected Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    /********************* Setter and Getter for the Variables *************************/

    /** @param Name Setter and Getter*/
    public void setName(String Name) {this.name = Name;}
    public String getName() {return name;}



    /** @param IncomingInterest Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1;) */
    public void setIncomingInterest(double IncomingInterest) {
        if(IncomingInterest >= 0 && IncomingInterest <= 1) {
            this.incomingInterest = IncomingInterest;
        }
        else {
            System.out.println("Bitte geben Sie den Wert des IncomingInterest zwischen 0 und 1 an.");
        }
    }
    public double getIncomingInterest() {
        return incomingInterest;
    }


    /** @param OutgoingInterest  Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1;) */
    public void setOutgoingInterest(double OutgoingInterest) {
        if(OutgoingInterest >= 0 && OutgoingInterest <= 1) {
            this.outgoingInterest = OutgoingInterest;
        }
        else {
            System.out.println("Bitte geben Sie den Wert des OutgoingInterest zwischen 0 und 1 an.");
        }
    }
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /********************* Seeter and Getter für DirectoryName amd fullPath **********************************/
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
    public String getDirectoryName() {
        return directoryName;
    }


    public Path getFullPath() {
        return fullPath;
    }

    /**
     * Gibt den Pfad der gespeicherten Konten.
     *
     * @param directoryName
     */
    public void setFullPath(String directoryName) {
        fullPath = Path.of("MyJsonFiles/" + directoryName);
    }
    /*******************************************************/

    /********************************** Constructor ***********************************/

    /**
     *
     * @param Name der privatBank
     * @param IncomingInterest der Zinsen bei EinZahlung
     * @param OutgoingInterest der Zinsen bei AusZahlung
     */
    public PrivateBank(String Name, double IncomingInterest, double OutgoingInterest) throws IOException{
        setName(Name);
        setIncomingInterest(IncomingInterest);
        setOutgoingInterest(OutgoingInterest);
    }

    /**
     * Constructor with four attributes
     */
    public PrivateBank(String name, String directoryName, double incomingInterest, double outgoingInterest) throws IOException {
        setName(name);
        setDirectoryName(directoryName);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);

        setFullPath(directoryName);

        if (Files.notExists(getFullPath())) {
            Files.createDirectories(getFullPath());
        } else {
            readAccounts();
            System.out.println("Alle vorhandenen Konten in " + name + " wurden gelesen\n");
        }
    }


    /********************************** Copy-Constructor ***********************************/
    public PrivateBank (PrivateBank privateBank) throws IOException{
        this (privateBank.name, privateBank.incomingInterest, privateBank.outgoingInterest);
        privateBank.accountsToTransactions = this.accountsToTransactions;
        setDirectoryName("C:\\Users\\AStA\\Desktop\\Pra5\\MyJsonFiles\\");
        try{
            Files.createDirectories(Path.of(getDirectoryName() + this.getName()));
            System.out.println("Bank is created!");

        } catch (IOException e) {

            System.err.println("Failed to create Bank!");
        }
    }



    /********************************** toString and eQual Methods ***********************************/
    @Override
    /**
     * Den Inhalt der Bankattribute auf der Konsole ausgeben
     *
     * @return ein String, der alle Informationen der Bankattribute beschreibt.
     */
    public String toString() {
        return "Name :"+ getName()+ "\n" +
                "IncomingInterest :"+getIncomingInterest()+ "\n" +
                "OutgoingInterest :"+getOutgoingInterest() +"\n" +
                "Accounts  :" + accountsToTransactions;
    }

    @Override
    /**
     * Prüft die Gleichheit beider Bankattribute.
     *
     * @param o Die andere Bankattribute, die man prüfen möchte.
     * @return True, falls beide Bankattribute gleich sind. False hingegen.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivateBank privatebank)) return false;
        return Double.compare(privatebank.incomingInterest, incomingInterest) == 0 &&
                Double.compare(privatebank.outgoingInterest, outgoingInterest) == 0 &&
                name.equals(privatebank.name) &&
                accountsToTransactions.equals(privatebank.accountsToTransactions);
    }

    @Override
    /********************************** create Account ***********************************/
    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    public void  createAccount(String account) throws AccountAlreadyExistsException, IOException{
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account Already exists!");
        }
        directoryName = "C:\\Users\\AStA\\Desktop\\Pra5\\MyJsonFiles\\";
        accountsToTransactions.put(account, List.of()); //create immutable lists
        Path path = Path.of(directoryName + this.getName() + "\\" + account + ".json");
        if (Files.notExists(path)) {
            writeAccount(account);
        }

    }

    /************************************* Create a new Account with Transactions **********************************************/
    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account Already exists!");
        }

        if (accountsToTransactions.containsValue(transactions)) {
            throw new TransactionAlreadyExistException("One or more Transactions already exists!");
        }
        if(!(outgoingInterest >= 0 && outgoingInterest <= 1) || !(incomingInterest >= 0 && incomingInterest <= 1))
        {
            throw new TransactionAttributeException("Transactions attribute are false");
        }
        accountsToTransactions.put(account, transactions);
        directoryName = "C:\\Users\\AStA\\Desktop\\Pra5\\MyJsonFiles\\";
        Path path = Path.of(directoryName + this.getName() + "\\" + account + ".json");
        if (Files.notExists(path)) {
            writeAccount(account);
        }
    }

    /************************************* Add Transaction to an exciting Account  **********************************************/

    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {

        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Account does not exists!");
        }

        if(accountsToTransactions.get(account).contains(transaction)){
            throw new TransactionAlreadyExistException("Transaction already exists!");
        }

        if(!(outgoingInterest >= 0 && outgoingInterest <= 1) || !(incomingInterest >= 0 && incomingInterest <= 1))
        {
            throw new TransactionAttributeException("Transactions attribute are false");
        }

        if(transaction instanceof Payment payment){
            payment.setIncomingInterest(this.getIncomingInterest());
            payment.setOutgoingInterest(this.getOutgoingInterest());
        }

        List<Transaction> myTransactions = new ArrayList<>(accountsToTransactions.get(account));
        myTransactions.add(transaction);
        accountsToTransactions.put(account, myTransactions);

    }

    /************************************* Remove Transaction **********************************************/
    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Account does not exists!");
        }
        if(!accountsToTransactions.get(account).contains(transaction)){
            throw new TransactionDoesNotExistException("Transaction does not exists!");
        }
        List<Transaction> myTransactions = new ArrayList<>(accountsToTransactions.get(account));
        myTransactions.remove(transaction);

        accountsToTransactions.put(account, myTransactions);

    }

    /************************************* containsTransaction **********************************************/
    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        return accountsToTransactions.get(account).contains(transaction);
    }

    /************************************* AccountBalance **********************************************/
    @Override
    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    public double getAccountBalance(String account) {
        double balance = 0;

        for(int i=0; i<accountsToTransactions.get(account).size();i++)
        {
            balance += accountsToTransactions.get(account).get(i).calculate();
        }
        return balance;
    }


    /************************************* Get Transaction **********************************************/
    @Override
    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     */
    public List<Transaction> getTransactions(String account) {
        return accountsToTransactions.get(account);
    }

    /************************************* Get Sorted Transaction ASC **********************************************/
    @Override
    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     */
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        List<Transaction> sortedTransactionsList = new ArrayList<>(accountsToTransactions.get(account));
        if(asc) {
            sortedTransactionsList.sort(Comparator.comparingDouble(Transaction::calculate));
            System.out.print("------------------------- Sorting transactions of account <" + account + "> by calculated amounts in ASCENDING order:  ------------------------- \n"  );
        }
        else {
            sortedTransactionsList.sort(Comparator.comparingDouble(Transaction::calculate).reversed());
            System.out.print("------------------------- Sorting transactions of account <" + account + "> by calculated amounts in DESCENDING order: ------------------------- \n" );
        }
        return sortedTransactionsList;
    }


    /************************************* Get Transaction Sorted by Type POSITIVE  **********************************************/
    @Override
    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> transactionsListByType = new ArrayList<>();
        if (positive)
            System.out.println("------------------------- List of POSITIVE transactions of account <" + account + "> : -------------------------");
        else
            System.out.println("------------------------- List of NEGATIVE transactions of account <" + account + "> : -------------------------");
        for (Transaction transaction : accountsToTransactions.get(account)) {
            if (positive && transaction.calculate() >= 0)
                transactionsListByType.add(transaction);
            else if (!positive && transaction.calculate() < 0)
                transactionsListByType.add(transaction);
        }
        return transactionsListByType;
    }

    /************************************* ReadAccount and WriteAccount **********************************************/
    /**
     * Read all existing accounts from data system and make them available in PrivateBank object
     *
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void readAccounts() throws IOException {
        // Get accounts folder
        File folder = new File(getFullPath().toString());

        // Get all accounts files
        File[] listOfFiles = Objects.requireNonNull(folder.listFiles());

        for (File file : listOfFiles) {
            // Parse Json File
            FileReader fileReader = new FileReader(getFullPath() + "/" + file.getName());
            JsonArray jsonArrayTransactions = JsonParser.parseReader(fileReader).getAsJsonArray();

            // Deserialize Transaction
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(List.class, new CustomDeSerializer())
                    .create();

            // Convert Json File to List of Transaction Objects using Gson
            List<Transaction> transactionsList = gson.fromJson(jsonArrayTransactions, List.class);

            // Create the account
            try {
                String accountName = file.getName().replace(".json", "");
                createAccount(accountName, transactionsList);
            } catch (AccountAlreadyExistsException | TransactionAlreadyExistException | TransactionAttributeException e) {
                System.out.println(e);
            }

            // Close the file
            fileReader.close();
        }
        System.out.println("---------------------- Alle vorhandenen Konten in MyJsonFiles wurden erfolgreich gelesen ---------------------------------\n ");
    }

    /**
     * Persists the specified account in the file system (serialize then save into JSON)
     *
     * @param account the account to be written
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void writeAccount(String account) throws IOException {
        List<Transaction> transactionsList = new LinkedList<>(accountsToTransactions.get(account));

        // Serialize transactions list of the account
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(transactionsList.getClass(), new CustomDeSerializer())
                .setPrettyPrinting()
                .create();

        // Convert Transactions objects to Json String using Gson
        String prettyJsonTransactions = gson.toJson(transactionsList);

        // Persist transactions list in Json file
        FileWriter file = new FileWriter(getFullPath() + "/" + account + ".json");
        file.write(prettyJsonTransactions);
        file.close();

        System.out.println("---------------------- Der Konto wurde erfolgreich erstellt ---------------------------------\n ");
    }

    /**
     * deleting an excisting Account
     *
     * @param account the account to be written
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("This Account does not exists!!");
        }
        accountsToTransactions.remove(account);
        Path path = Path.of(this.getFullPath()+"/"+account+".json");
        Files.deleteIfExists(path);
    }




    @Override
    public List<String> getAllAccounts() {
        Set<String> set= accountsToTransactions.keySet();
        return new ArrayList<>(set);
    }
}

