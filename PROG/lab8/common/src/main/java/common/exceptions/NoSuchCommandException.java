package common.exceptions;

/**
 * thrown when user enters invalid command
 */
public class NoSuchCommandException extends CommandException {
    public NoSuchCommandException(String cmd) {
        super("[NoSuchCommandException] wrong command: ["+cmd+"]");
    }
}