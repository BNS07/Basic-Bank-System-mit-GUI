package bank;



public abstract class Transaction implements CalculateBill{
    /**
     * Soll den Zeitpunkt einer Ein- oder Auszahlung bzw. einer Überweisung darstellen
     */
    protected String date;
    /**
     * Erlaubt eine zusätzliche Beschreibung des Vorgangs.
     */
    protected String description;
    /**
     * Soll die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung darstellen.
     */
    protected double amount;


    /**
     * Gibt den Betrag zurück.
     * @return ein double, der den Betrag darstellt.
     */
    public double getAmount()
    {
        return amount;
    }

    /**
     * Gibt das Datum zurück.
     * @return ein String, der das Datum darstellt.
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Setzt das Datum ein.
     * @param date Das Datum des Vorgangs.
     */
    public void setDate(String date)
    {
        this.date = date;
    }


    /**
     * Gibt die Beschreibung des Vorgangs zurück.
     * @return ein String, der die Beschreibung des Vorgangs darstellt.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     *Setzt die Beschreibung des Vorgangs ein.
     * @param description Die Beschreibung des Vorgangs.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     *Den Inhalt des Datums, des Betrags und der Beschreibung auf der Konsole ausgeben.
     * @return ein String, der alle Informationen des Vorgangs beschreibt.
     */
    public String toString() {
        return ("Date: " + getDate() + "\n"+
                "Description: " + getDescription() + "\n" +
                "Amount: " + getAmount() + "€");
    }

    /**
     *Prüft die Gleichheit beider Transaktionen.
     * @param obj andere Transaktion, die man prüfen möchte.
     * @return True, falls beide Transaktionen gleich sind. False hingegen.
     */
    @Override
    public boolean equals(Object obj)
    {
        Transaction trans = (Transaction) obj;
        if (    !trans.getDate().equals(this.getDate()) ||
                !trans.getDescription().equals(this.getDescription()) ||
                trans.getAmount() != this.getAmount()
        )
        {
            return false;
        }
        return true;
    }
}


