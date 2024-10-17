package common.exceptions;

/**
 * thrown when received data is invalid
 */
public class InvalidReceivedDataException extends InvalidDataException {
    public InvalidReceivedDataException() {
        super("received data is damaged");
    }
}
