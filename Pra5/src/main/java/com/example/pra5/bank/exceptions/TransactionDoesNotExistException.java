package com.example.pra5.bank.exceptions;

public class TransactionDoesNotExistException extends Exception{
    public TransactionDoesNotExistException(String ausgabe){super(ausgabe);}
}
