package bank;

import bank.Exceptions.*;

import java.util.*;

/**
 *PrivateBankAlt soll im Kontext von Bank verwendet werden.
 */
public class PrivateBankAlt implements Bank{

    /**
     *Dieses Attribut soll den Namen der privaten Bank repräsentieren.
     */

    private String name;

    /**
     *Dieses Attribut gibt die Zinsen,die bei einer Einzahlung (Deposit) anfallen.
     */

    private double incomingInterest;

    /**
     *Dieses Attribut gibt die Zinsen,die bei einer Auszahlung (Withdrawal) anfallen.
     */

    private double outgoingInterest;

    /**
     * Dieses Attribut soll Konten mit
     * Transaktionen verknüpfen, so dass jedem gespeicherten Konto 0 bis n Transaktionen zugeordnet
     * werden können. Der Schlüssel steht für den Namen des Kontos.
     */

    private Map<String, List<Transaction>> accountsToTransactions= new HashMap<>();

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
     *
     * @param name der privatBank
     * @param incomingInterest der Zinsen bei EinZahlung
     * @param outgoingInterest der Zinsen bei AusZahlung
     */
    public PrivateBankAlt(String name, double incomingInterest , double outgoingInterest)
    {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     *
     * @param privbank Objekt der Copie Konstrukter
     */
    public PrivateBankAlt(PrivateBankAlt privbank)
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
                "OutgoingInterest :"+getOutgoingInterest() +"\n") ;
    }

    /**
     * Prüft die Gleichheit beider Bankattribute.
     *
     * @param obj Die andere Bankattribute, die man prüfen möchte.
     * @return True, falls beide Bankattribute gleich sind. False hingegen.
     */
    public boolean equals(Object obj)
    {
        PrivateBankAlt privbank = (PrivateBankAlt) obj;
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
    public void createAccount(String account) throws AccountAlreadyExistsException
    {
        if(accountsToTransactions.containsKey(account))
        {
            throw new AccountAlreadyExistsException();
        }
        accountsToTransactions.put(account, new LinkedList<Transaction>());
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
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException
    {
        if(accountsToTransactions.containsKey(account))
        {
            throw new AccountAlreadyExistsException();
        }
        if(accountsToTransactions.containsValue(transactions))
        {
            throw new TransactionAlreadyExistException();
        }
        if(!(outgoingInterest >= 0 && outgoingInterest <= 1) || !(incomingInterest >= 0 && incomingInterest <= 1))
        {
            throw new TransactionAttributeException();
        }
        accountsToTransactions.put(account,new LinkedList<Transaction> (transactions));
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
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException
    {
        if (!accountsToTransactions.containsKey(account))
        {
            throw new AccountDoesNotExistException();
        }
        if(accountsToTransactions.containsValue(transaction))
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
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException
    {
        if (!accountsToTransactions.containsKey(account))
        {
            throw new AccountDoesNotExistException();
        }
        if (!accountsToTransactions.get(account).contains(transaction))
        {
            throw new TransactionDoesNotExistException();
        }
        accountsToTransactions.get(account).remove(transaction);
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

      for(int i = 0; i<accountsToTransactions.get(account).size();i++) {
         Transaction transaction = accountsToTransactions.get(account).get(i);
         if (transaction instanceof Transfer)
         {
            if (account == (((Transfer) transaction).getSender()))
            {
               balance -= transaction.calculate();//outgoing
            }
            else
            {
               balance += transaction.calculate();//ingoing
            }

         }
         else if ( transaction instanceof  Payment)
         {
            balance += transaction.calculate();
         }
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
}