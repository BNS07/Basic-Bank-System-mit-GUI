package bank.Exceptions;

public class TransactionAlreadyExistException extends Exception {
    public TransactionAlreadyExistException ()
    {
        super("The Transaction already exists");
    }
}
