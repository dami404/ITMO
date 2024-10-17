package common.exceptions;

/**
 * thrown when file doesnt exist
 */
public class FileNotExistsException extends FileException {
    public FileNotExistsException() {
        super("cannot find file");
    }
}
