package common.exceptions;

/**
 * base class for exceptions caused by invalid data
 */
public class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}