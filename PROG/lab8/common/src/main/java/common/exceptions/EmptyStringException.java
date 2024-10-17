package common.exceptions;

/**
 * thrown when user input is empty
 */
public class EmptyStringException extends InvalidDataException {
    public EmptyStringException() {
        super("string cannot be empty");
    }
    public EmptyStringException(String s){
        super(s);
    }
}
