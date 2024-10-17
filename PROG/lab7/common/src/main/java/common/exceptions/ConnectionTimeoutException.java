package common.exceptions;

/**
 * thrown when waiting timout exceed
 */
public class ConnectionTimeoutException extends ConnectionException {
    public ConnectionTimeoutException() {
        super("response timed out");
    }
}
