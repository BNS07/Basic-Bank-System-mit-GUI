package bank.Exceptions;

public class AccountDoesNotExistException extends Exception {
    public AccountDoesNotExistException()
    {
        super("The account already exists ");
    }
}
