package common.exceptions;

/**
 * thrown when file path is empty
 */
public class EmptyPathException extends FileException {
    public EmptyPathException() {
        super("path is empty");
    }
}
