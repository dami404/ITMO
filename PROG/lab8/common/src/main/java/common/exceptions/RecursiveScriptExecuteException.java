package common.exceptions;

/**
 * thrown when script call loops
 */
public class RecursiveScriptExecuteException extends CommandException {
    public RecursiveScriptExecuteException() {
        super("[RecursiveScriptExecuteException] recursive script execute attempt");
    }
}
