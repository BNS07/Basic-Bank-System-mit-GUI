package com.example.pra5.bank;

import com.example.pra5.bank.exceptions.*;

import java.util.Objects;

/**
 * diese klasse wurde in kontext für Überweisungen benutzt
 */
public class Transfer extends Transaction implements CalculateBill {

    private String sender;
    private String recipient;


    /**
     *
     * @param amount  Dieses Attribut soll die Geldmenge einer Ein- oder Auszahlung bzw. einer
     * Überweisung darstellen
     */
    public void setAmount(double amount) throws TransactionAttributeException {
        if(amount>0)
        {
            this.amount = amount;
        }
        else if (amount < 0)
        {
            throw new TransactionAttributeException("The Amount has to be Positive");
        }
    }


    /**
     * @param Sender  Dieses Attribut gibt an, welcher Akteur
     * die Geldmenge, die in amount angege-ben wurde, überwiesen hat.
     */
    public void setSender(String Sender) { this.sender = Sender; }
    public String getSender() { return sender; }


    /**
     * @param Recipient Dieses Attribut gibt an, welcher Akteur die Geldmenge,
     *  die in amount ange-geben wurde, überwiesen bekommen hat.
     */
    public void setRecipient(String Recipient) { this.recipient = Recipient; }
    public String getRecipient() { return recipient; }


    public Transfer(){};
    /** Konstruktor für die Attribute date, amount und description. */
    public Transfer(String Date, double Amount, String Description) throws TransactionAttributeException{
        setDate(Date);
        setAmount(Amount);
        setDescription(Description);
    }

    /** Konstruktor für alle Attribute */
    public Transfer(String Date, double Amount, String Description, String Sender, String Recipient) throws TransactionAttributeException{
        this(Date, Amount, Description);
        setSender(Sender);
        setRecipient(Recipient);
    }


    /**
     *
     * Copy-Constructor für alle Attribute.
     *
     **/
    public Transfer(Transfer transfer) throws TransactionAttributeException {
        this(transfer.date, transfer.amount, transfer.description, transfer.sender, transfer.recipient);
    }

    /**
     *
     * Print all the Data of the Transfer Object
     * @return string
     *
     * */
    @Override
    public String toString() {
        return ("\nTransfer"+ "\n" +
                super.toString() + "\n" +
                "Sender: " + getSender() + "\n" +
                "Recipient: " + getRecipient() + "\n");

    }

    /**
     *
     * Check if two Transfers are equal
     * @return boolean
     *
     **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(getSender(), transfer.getSender()) && Objects.equals(getRecipient(), transfer.getRecipient());
    }


    /**
     *
     * die Methode calculate() soll den Betrag unverändert zurückgeben.
     * @return double
     *
     **/
    @Override
    public double calculate() {
        return getAmount();
    }
}
