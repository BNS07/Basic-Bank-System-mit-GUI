package bank;

import bank.Exceptions.*;

/**
 * Transfer soll im Kontext von Überweisungen verwendet werden.
 */
public class Transfer extends Transaction implements CalculateBill {
    /**
     * Gibt an, welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen hat.
     */
    private String sender;
    /**
     * Gibt an, welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen bekommen hat.
     */
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
            throw new TransactionAttributeException();
        }
    }

    /**
     * Gibt den Wert der Absender zurück.
     *
     * @return ein String, der den Absender darstellt.
     */
    public String getSender()
    {
        return sender;
    }

    /**
     * Setzt den Wert der Absender ein.
     *
     * @param sender Der Absender.
     */
    public void setSender(String sender)
    {
        this.sender = sender;
    }

    /**
     * Gibt den Wert der Empfänger zurück.
     *
     * @return ein String, der den Empfänger darstellt.
     */

    public String getRecipient()
    {
        return recipient;
    }

    /**
     * Setzt den Wert der Empfänger ein.
     *
     * @param recipient Der Empfänger.
     */
    public void setRecipient(String recipient)
    {
        this.recipient = recipient;
    }

    /**
     * Erzeugt eine neue Überweisung.
     *
     * @param date Das Datum des Vorgangs.
     * @param amount Der Betrag des Vorgangs.
     * @param description Die Beschreibung des Vorgangs.
     */
    public Transfer(String date, double amount, String description) throws TransactionAttributeException {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    /**
     * Erzeugt eine neue Überweisung mit allen Informationen.
     *
     * @param date Das Datum des Vorgangs.
     * @param amount Der Betrag des Vorgangs.
     * @param description Die Beschreibung des Vorgangs.
     * @param sender Der Absender.
     * @param recipient Der Empfänger.
     */
    public Transfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        this(date, amount, description);
        setSender(sender);
        setRecipient(recipient);
    }

    /**
     *Kopiert eine andere Überweisung.
     * @param trans Die andere Überweisung.
     */
    public Transfer(Transfer trans) throws TransactionAttributeException {
        this(trans.date,trans.amount,trans.description,trans.sender,trans.recipient);

    }

    /**
     * Gibt den Betrag ohne eingehenden bzw. ausgehenden Zinsen zurück.
     *
     * @return ein double, der den neuen Betrag darstellt.
     */
    @Override
    public double calculate()
    {
        return getAmount();
    }

    /**
     * Den Inhalt der Überweisung auf der Konsole ausgeben
     *
     * @return ein String, der alle Informationen der Überweisung beschreibt.
     */
    public String toString()
    {
        return ("\nTransfer"+ "\n" +
                super.toString() + "\n" +
                "Sender: " + getSender() + "\n" +
                "Recipient: " + getRecipient() + "\n");
    }

    /**
     * Prüft die Gleichheit beider Überweisungen.
     *
     * @param obj Die andere Überweisung, die man prüfen möchte.
     * @return True, falls beide Überweisungen gleich sind. False hingegen.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if(obj.getClass() != this.getClass())
        {
            return false;
        }
        Transfer trans = (Transfer) obj;
        if (    !super.equals(trans)  ||
                !trans.getSender().equals(this.getSender()) ||
                !trans.getRecipient().equals(this.getRecipient()))
        {
            return false;
        }
        return true;
    }
}

