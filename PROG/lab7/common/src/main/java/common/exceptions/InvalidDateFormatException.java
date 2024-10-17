package common.exceptions;

/**
 * thrown when date format is invalid
 */
public class InvalidDateFormatException extends InvalidDataException {
    public InvalidDateFormatException() {
        super("date format must be yyyy-MM-dd");
    }
}
