package common.exceptions;

/**
 * thrown when program is interrupted
 */
public class ExitException extends CommandException {
    public ExitException() {
        super("shutting down...");
    }
}
