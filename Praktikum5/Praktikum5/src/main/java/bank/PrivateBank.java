package bank;

import bank.Exceptions.*;
import com.google.gson.*;
import javafx.fxml.FXML;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 *Eine private Bank repräsentieren
 */
public class PrivateBank implements Bank{
   /**
    *Gibt den Banknamen.
    */
   private String name;

   /**
    * Gibt den Speicherort der Konten bzw. Transaktionen.
    */
   private String directoryName;

   /**
    *Dieses Attribut gibt die Zinsen,die bei einer Einzahlung (Deposit) anfallen.
    */
   private double incomingInterest;
   /**
    *Dieses Attribut gibt die Zinsen,die bei einer Auszahlung (Withdrawal) anfallen.
    */
   private double outgoingInterest;
   /**
    * Gibt den Pfad der gespeicherten Konten.
    */
   private Path fullPath;

   /**
    *Dieses Attribut soll Konten mit
    * Transaktionen verknüpfen, so dass jedem gespeicherten Konto 0 bis n Transaktionen zugeordnet
    * werden können. Der Schlüssel steht für den Namen des Kontos.
    */

   private Map<String, List<Transaction>> accountsToTransactions= new HashMap<>();
   /**
    * Neue Bank erstellen
    *
    * @param name             Der Bankname.
    * @param directoryName    Den Speicherort der Konten bzw. Transaktionen.
    * @param incomingInterest Die anfallenden Zinsen bei einer Einzahlung.
    * @param outgoingInterest Die anfallenden Zinsen bei einer Auszahlung.
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
   /**
    *Gibt der Name zurück
    * @return ein String, der der Name darstellt
    */
   public String getName()
   {
      return name;
   }

   /**
    *Setzt den Name ein
    * @param name  Der Name
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    *Gibt die IncomingInterest zurück
    * @return ein double, der die Zinsen bei Einzahlung darstellt.
    */
   /**
    * Gibt den Speicherort der Konten bzw. Transaktionen ein.
    *
    * @return ein String, das den Ordnernamen repräsentiert.
    */
   public String getDirectoryName() {
      return directoryName;
   }

   /**
    * Gibt den Ordnernamen der gespeicherten Konten bzw. Transaktionen ein.
    *
    * @param directoryName Der Ordnername
    */
   public void setDirectoryName(String directoryName) {
      this.directoryName = directoryName;
   }
   public double getIncomingInterest()
   {
      return incomingInterest;
   }

   /**
    *Setzt die IncomingInterest ein
    * @param incomingInterest , IncomingInterest
    */
   public void setIncomingInterest(double incomingInterest)
   {
      this.incomingInterest = incomingInterest;
   }

   /**
    *Gibt die OutgoingInterest zurück
    * @return ein double,der die Zinsen bei Auszahlung zurück
    */
   public double getOutgoingInterest()
   {
      return outgoingInterest;
   }
   /**
    * Setzt die OutgoingsInterest ein
    * @param outgoingInterest , Auszahlungs Zinsen
    */
   public void setOutgoingInterest(double outgoingInterest)
   {
      this.outgoingInterest = outgoingInterest;
   }

   /**
    * Gibt den Pfad der gespeicherten Konten.
    *
    * @return
    */
   public Path getFullPath() {
      return fullPath;
   }

   /**
    * Gibt den Pfad der gespeicherten Konten.
    *
    * @param directoryName
    */
   public void setFullPath(String directoryName) {
      fullPath = Path.of("Banken/" + directoryName);
   }

   /**
    *
    * @param name der privatBank
    * @param incomingInterest der Zinsen bei EinZahlung
    * @param outgoingInterest der Zinsen bei AusZahlung
    */
   public PrivateBank(String name, double incomingInterest , double outgoingInterest)
   {
      setName(name);
      setIncomingInterest(incomingInterest);
      setOutgoingInterest(outgoingInterest);
   }

   /**
    *
    * @param privbank Objekt der Copie Konstrukter
    */
   public PrivateBank(PrivateBank privbank)
   {
      this (privbank.name,privbank.incomingInterest,privbank.outgoingInterest);
      privbank.accountsToTransactions = this.accountsToTransactions;
   }

   /**
    * Den Inhalt der Bankattribute auf der Konsole ausgeben
    *
    * @return ein String, der alle Informationen der Bankattribute beschreibt.
    */
   public String toString()
   {
      return ("Name :"+ getName()+ "\n" +
              "IncomingInterest :"+getIncomingInterest()+ "\n" +
              "OutgoingInterest :"+getOutgoingInterest() +"\n" +
               "Accounts  :" + accountsToTransactions);
   }

   /**
    * Prüft die Gleichheit beider Bankattribute.
    *
    * @param obj Die andere Bankattribute, die man prüfen möchte.
    * @return True, falls beide Bankattribute gleich sind. False hingegen.
    */
   public boolean equals(Object obj)
   {
      PrivateBank privbank = (PrivateBank) obj;
      return ( this.getName().equals(privbank.getName()) &&
               this.getIncomingInterest() == (privbank.getIncomingInterest()) &&
               this.getOutgoingInterest() == (privbank.getOutgoingInterest())) &&
               this.accountsToTransactions.equals(privbank.accountsToTransactions);
   }
   /**
    * Adds an account to the bank.
    *
    * @param account the account to be added
    * @throws AccountAlreadyExistsException if the account already exists
    */
   public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
      if(accountsToTransactions.containsKey(account))
      {
         throw new AccountAlreadyExistsException();
      }
      accountsToTransactions.put(account, new LinkedList<Transaction>());

      Path path = Path.of(this.getFullPath() + "/" + account + ".json");
      if (Files.notExists(path)) {
         writeAccount(account);
      }
   }
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
   public void createAccount(String account, List<Transaction> transactions)
           throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException {
      if(accountsToTransactions.containsKey(account))
      {
         throw new AccountAlreadyExistsException();
      }
      if(accountsToTransactions.get(account) == transactions)
      {
         throw new TransactionAlreadyExistException();
      }
      if(!(outgoingInterest >= 0 && outgoingInterest <= 1) || !(incomingInterest >= 0 && incomingInterest <= 1))
      {
         throw new TransactionAttributeException();
      }
      accountsToTransactions.put(account,new LinkedList<Transaction> (transactions));

      Path path = Path.of(this.getFullPath() + "/" + account + ".json");
      if (Files.notExists(path)) {
         writeAccount(account);
      }
   }

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
      if (!accountsToTransactions.containsKey(account))
      {
         throw new AccountDoesNotExistException();
      }
      if(accountsToTransactions.get(account).contains(transaction))
      {
         throw new TransactionAlreadyExistException();
      }
      if(!(outgoingInterest >= 0 && outgoingInterest <= 1) || !(incomingInterest >= 0 && incomingInterest <= 1))
      {
         throw new TransactionAttributeException();
      }
      if(transaction instanceof Payment)
      {
         ((Payment) transaction).setIncomingInterest(this.incomingInterest);
         ((Payment) transaction).setOutgoingInterest(this.outgoingInterest);
      }
      accountsToTransactions.get(account).add(transaction);

      writeAccount(account);
   }
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
      if (!accountsToTransactions.containsKey(account))
      {
         throw new AccountDoesNotExistException();
      }
      if (!accountsToTransactions.get(account).contains(transaction))
      {
         throw new TransactionDoesNotExistException();
      }
      accountsToTransactions.get(account).remove(transaction);

      writeAccount(account);
   }
   /**
    * Checks whether the specified transaction for a given account exists.
    *
    * @param account     the account from which the transaction is checked
    * @param transaction the transaction to search/look for
    */
   @Override
   public boolean containsTransaction(String account, Transaction transaction)
   {
      if(accountsToTransactions.get(account).contains(transaction))
      {
         return true;
      }
      else {
         return false;
            }
   }
   /**
    * Calculates and returns the current account balance.
    *
    * @param account the selected account
    * @return the current account balance
    */
   @Override
   public double getAccountBalance(String account) {
      double balance = 0;

      for(int i=0; i<accountsToTransactions.get(account).size();i++)
      {
         balance += accountsToTransactions.get(account).get(i).calculate();
      }
      return balance;

   }
   /**
    * Returns a list of transactions for an account.
    *
    * @param account the selected account
    * @return the list of all transactions for the specified account
    */
   @Override
   public List<Transaction> getTransactions(String account)
   {
      return accountsToTransactions.get(account);
   }
   /**
    * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
    * (or empty).
    *
    * @param account the selected account
    * @param asc     selects if the transaction list is sorted in ascending or descending order
    * @return the sorted list of all transactions for the specified account
    */
   @Override
   public List<Transaction> getTransactionsSorted(String account, boolean asc)
   {
      List<Transaction> transactions = new LinkedList<>(accountsToTransactions.get(account));
      if (asc)
      {
         Collections.sort(transactions, Comparator.comparing(CalculateBill::calculate));
      }
      else
      {
         Collections.sort(transactions, Comparator.comparing(CalculateBill::calculate).reversed());
      }
      return transactions;
   }
   /**
    * Returns a list of either positive or negative transactions (-> calculated amounts).
    *
    * @param account  the selected account
    * @param positive selects if positive or negative transactions are listed
    * @return the list of all transactions by type
    */
   @Override
   public List<Transaction> getTransactionsByType(String account, boolean positive)
   {
      List<Transaction> transactions = new LinkedList<>();
      if(positive)
      {
         for(int i =0; i < accountsToTransactions.get(account).size(); i++)
         {
            if(accountsToTransactions.get(account).get(i).calculate() > 0)
            {
               transactions.add(accountsToTransactions.get(account).get(i));
            }

         }
      }
      else
      {
         for(int i =0; i < accountsToTransactions.get(account).size(); i++)
         {
            if(accountsToTransactions.get(account).get(i).calculate() < 0)
            {
               transactions.add(accountsToTransactions.get(account).get(i));
            }

         }
      }
      return transactions;
   }

   /**
    * Read all existing accounts from data system and make them available in PrivateBank object
    *
    * @throws IOException Signals that an I/O exception of some sort has occurred
    */
   private void readAccounts() throws IOException {
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
   }

   /**
    * Persists the specified account in the file system (serialize then save into JSON)
    *
    * @param account the account to be written
    * @throws IOException Signals that an I/O exception of some sort has occurred
    */
   private void writeAccount(String account) throws IOException {
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
   }

   public void deleteAccount(String account) throws  AccountDoesNotExistException, IOException{
      if(!accountsToTransactions.containsKey(account)){
         throw new AccountDoesNotExistException();}
      else{
         accountsToTransactions.remove(account);
         Path path= Path.of(this.getFullPath()+"/"+account+".json");
         Files.deleteIfExists(path);
      }
   }


   public List<String> getAllAccounts() {
      Set<String> set= accountsToTransactions.keySet();
      return new ArrayList<>(set);
   }
}


























