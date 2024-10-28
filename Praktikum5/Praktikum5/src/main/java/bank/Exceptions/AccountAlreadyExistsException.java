package bank.Exceptions;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException ()
    {
        super("The account already exists");
    }
}
