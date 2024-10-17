package common.exceptions;

public class InvalidUserException extends InvalidDataException {
    public InvalidUserException() {
        super("user login or password is incorrect");
    }
}
