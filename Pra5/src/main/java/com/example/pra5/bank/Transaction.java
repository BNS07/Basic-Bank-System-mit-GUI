package com.example.pra5.bank;


public abstract class Transaction implements CalculateBill{
    protected String date;
    protected double amount;
    protected String description;

    /** @param Date das Datum der Überweisung */
    public void setDate(String Date) {this.date = Date;}
    public String getDate() {return date;}


    public double getAmount() {return amount;}


    /** @param Description die Beschreibung der Überweisung */
    public void setDescription(String Description) {this.description = Description;}
    public String getDescription() {return description;}

    /** Konstruktor */
    public Transaction(){};

    /**
     *
     * Print all the Data of the Transaction Object
     * @return string
     *
     * */
    @Override
    public String toString() {
        return ("Date: " + getDate() + "\n"+
                "Description: " + getDescription() + "\n" +
                "Amount: " + getAmount() + "€");
    }

    /**
     *
     * Check if two Transactions are equal
     * @return Boolean
     *
     **/
    @Override
    public boolean equals(Object o) {
        //Objektreferanzen
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return (this.getAmount() == that.getAmount()) && (this.getDate() == that.getDate()) &&  (this.getDescription() == that.getDescription());
    }

}
