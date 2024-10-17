package common.exceptions;

/**
 * base class for all file exceptions
 */
public class FileException extends Exception {
    public FileException(String msg) {
        super(msg);
    }
    public FileException(){
        super("[FileException] unable to read file");
    }
}
