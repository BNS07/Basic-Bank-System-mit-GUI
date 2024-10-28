package bank.Exceptions;

public class TransactionDoesNotExistException extends Exception {
    public TransactionDoesNotExistException ()
    {
        super("The Transaction does not exist");
    }
}
