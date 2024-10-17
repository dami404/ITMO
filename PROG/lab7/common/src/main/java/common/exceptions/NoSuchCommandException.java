package common.exceptions;

/**
 * thrown when user enters invalid command
 */
public class NoSuchCommandException extends CommandException {
    public NoSuchCommandException() {
        super("wrong command");
    }
}