package common.exceptions;

/**
 * thrown when connection is closed
 */
public class ClosedConnectionException extends ConnectionException {
    public ClosedConnectionException() {
        super("server channel closed");
    }
}
