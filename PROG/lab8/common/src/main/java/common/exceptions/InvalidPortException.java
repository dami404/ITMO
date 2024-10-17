package common.exceptions;

/**
 * thrown when port is invalid
 */
public class InvalidPortException extends ConnectionException {
    public InvalidPortException() {
        super("invalid port");
    }
}
