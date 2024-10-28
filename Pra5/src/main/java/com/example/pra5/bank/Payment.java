package com.example.pra5.bank;

import com.example.pra5.bank.exceptions.*;

public class Payment extends Transaction implements CalculateBill {

    private double incomingInterest;
    private double outgoingInterest;


    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    /** @param IncomingInterest Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1;) */
    public void setIncomingInterest(double IncomingInterest) throws TransactionAttributeException {
        if(IncomingInterest >= 0 && IncomingInterest <= 1) {
            this.incomingInterest = IncomingInterest;
        }
        else {
            throw new TransactionAttributeException("Bitte geben Sie den Wert des IncomingInterest zwischen 0 und 1 an.");
        }
    }
    public double getIncomingInterest() {
        return incomingInterest;
    }


    /** @param OutgoingInterest  Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1;) */
    public void setOutgoingInterest(double OutgoingInterest) throws TransactionAttributeException{
        if(OutgoingInterest >= 0 && OutgoingInterest <= 1) {
            this.outgoingInterest = OutgoingInterest;
        }
        else {
            throw new TransactionAttributeException("Bitte geben Sie den Wert des OutgoingInterest zwischen 0 und 1 an.");
        }
    }
    public double getOutgoingInterest() {
        return outgoingInterest;
    }


    /**  Konstruktor für die Attribute date, amount und description. */
    public Payment(String Date, double Amount, String Description){
        this.setDate(Date);
        this.setAmount(Amount);
        this.setDescription(Description);


    }

    /** Konstruktor für alle Attribute */
    public Payment(String Date, double Amount, String Description, double Incominginterest, double OutgoingInterest) throws TransactionAttributeException{
        this(Date, Amount, Description);
        setIncomingInterest(Incominginterest);
        setOutgoingInterest(OutgoingInterest);
    }

    /** Copy-Constructor für alle Attribute. */
    public Payment(Payment payment) throws TransactionAttributeException{
        /********************************************/
        this(payment.date,payment.amount,payment.description,payment.incomingInterest,payment.outgoingInterest);
    }

    /**
     *
     * Print all the Data of the Payment Object
     * @return string
     *
     * */
    @Override
    public String toString() {
        return("\nPayment"+ "\n" +
                super.toString()+ "\n" +
                "IncomingInterest: " + getIncomingInterest() + "%\n" +
                "OutgoingInterest: " + getOutgoingInterest()+ "%\n" +
                "CalculatedBill: " + calculate() + "€\n");
    }


    /**
     *
     * Check if two Payments are equal
     * @return boolean
     *
     **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Payment payment = (Payment) o;
        return Double.compare(payment.getIncomingInterest(), getIncomingInterest()) == 0 && Double.compare(payment.getOutgoingInterest(), getOutgoingInterest()) == 0;
    }

    /**
     *
     * Sollte der Betrag positiv sein und somit eine Einzahlung repräsentieren, soll der Wert des incomingInterest-
     * Attributes prozentual von der Einzahlung abgezogen und das Ergebnis zurückgegeben werden.
     * @return double
     *
     **/
    @Override
    public double calculate() {
        if(getAmount()>0){
            return getAmount() - (getAmount() * getIncomingInterest());
        }
        else{
            return  getAmount() + (getAmount() * getOutgoingInterest());
        }
    }
}
