package bank;

import bank.Exceptions.*;

/**
 *Payment soll Ein- und Auszahlungen repräsentieren.
 */
public class Payment extends Transaction implements CalculateBill {
    /**
     * Gibt die Zinsen an, die bei einer Einzahlung („Deposit“) anfallen.
     */
    private double incomingInterest;
    /**
     * Gibt die Zinsen an, die bei einer Auszahlung („Withdrawal“) anfallen.
     */
    private double outgoingInterest;

    /**
     *Setzt den Wert des Betrags ein.
     * @param amount Der Betrag des Vorgangs.
     */
    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    /**
     * Gibt den Wert der anfallenden Zinsen bei einer Einzahlung zurück.
     *
     * @return ein double, der die anfallenden Zinsen bei einer Einzahlung darstellt.
     */
    public double getIncomingInterest()
    {
        return incomingInterest;
    }

    /**
     * Setzt den Wert der anfallenden Zinsen bei einer Einzahlung ein.
     *
     * @param incomingInterest Die anfallenden Zinsen bei einer Einzahlung.
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException
    {
        if (incomingInterest >= 0 && incomingInterest <= 1) {
            this.incomingInterest = incomingInterest;
        }
        else{
            throw new TransactionAttributeException ();
        }
    }

    /**
     * Gibt den Wert der anfallenden Zinsen bei einer Auszahlung zurück.
     *
     * @return ein double, der die anfallenden Zinsen bei einer Auszahlung darstellt.
     */
    public double getOutgoingInterest()
    {
        return outgoingInterest;
    }

    /**
     * Setzt den Wert der anfallenden Zinsen bei einer Auszahlung ein.
     *
     * @param outgoingInterest Die anfallenden Zinsen bei einer Auszahlung.
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException
    {
        if (outgoingInterest >= 0 && outgoingInterest <= 1) {
            this.outgoingInterest = outgoingInterest;
        }
        else
        {
            throw new TransactionAttributeException();
        }
    }

    /**
     * Erzeugt eine neue Ein- oder Auszahlung.
     *
     * @param date Das Datum des Vorgangs.
     * @param amount Der Betrag des Vorgangs.
     * @param description Die Beschreibung des Vorgangs.
     */
    public Payment(String date, double amount, String description){
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    /**
     * Erzeugt eine neue Ein- oder Auszahlung mit allen Informationen der Zinsen.
     *
     * @param date Das Datum des Vorgangs.
     * @param amount Der Betrag des Vorgangs.
     * @param description Die Beschreibung des Vorgangs.
     * @param incomingInterest Die anfallenden Zinsen bei einer Einzahlung.
     * @param outgoingInterest Die anfallenden Zinsen bei einer Auszahlung.
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest) throws TransactionAttributeException {
        this(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     * Bei einer Einzahlung muss der Wert des incomingInterest-Attributes prozentual von der Einzahlung abgezogen
     * oder bei einer Auszahlung muss der Wert des outgoingInterest-Attributes prozentual zu der Auszahlung „hinzuaddiert“.
     *
     * @return Gibt den neuen Betrag nach abziehen oder addieren der Zinsen zurück.
     */
    @Override
    public double calculate() {
        if(getAmount() > 0) {
            return (getAmount() - getAmount() * getIncomingInterest());
        }
        else if (getAmount() < 0)
        {
            return (getAmount() + getAmount() * getOutgoingInterest());
        }
        else
        {
            return getAmount();
        }
    }

    /**
     *Kopiert eine andere Ein- oder Auszahlung.
     * @param pay Die andere Ein- oder Auszahlung.
     */
    public Payment(Payment pay) throws TransactionAttributeException {
        this(pay.date,pay.amount,pay.description,pay.incomingInterest,pay.outgoingInterest);
    }

    /**
     * Den Inhalt der Ein- oder Auszahlung auf der Konsole ausgeben.
     *
     * @return ein String, der alle Informationen der Ein- oder Auszahlung beschreibt.
     */
    public String toString()
    {
        return("\nPayment"+ "\n" +
                super.toString()+ "\n" +
                "IncomingInterest: " + getIncomingInterest() + "%\n" +
                "OutgoingInterest: " + getOutgoingInterest()+ "%\n" +
                "CalculatedBill: " + calculate() + "€\n");
    }

    /**
     * Prüft die Gleichheit beider Ein- oder Auszahlungen.
     *
     * @param obj Die andere Ein- oder Auszahlung, die man prüfen möchte.
     * @return True, falls beide Ein- oder Auszahlungen gleich sind. False hingegen.
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
        Payment pay = (Payment) obj;
        if (    !super.equals(pay)  ||
                this.getIncomingInterest() != pay.getIncomingInterest() ||
                this.getOutgoingInterest() != pay.getOutgoingInterest())
        {
            return false;
        }
        return true;
    }
}
