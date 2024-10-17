package common.exceptions;

/**
 * base class for all connection exceptions caused by connection problems
 */
public class ConnectionException extends Exception {
    public ConnectionException(String s) {
        super(s);
    }
}
