package common.exceptions;

/**
 * thrown when address is invalid
 */
public class InvalidAddressException extends ConnectionException {
    public InvalidAddressException() {
        super("invalid address");
    }

    public InvalidAddressException(String s) {
        super(s);
    }
}
