package bank.Exceptions;

public class TransactionAttributeException extends Exception {
    public TransactionAttributeException ()
    {
        super ("Transactions attribute are false");
    }
}
