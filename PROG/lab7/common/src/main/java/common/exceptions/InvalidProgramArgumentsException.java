package common.exceptions;

/**
 * thrown when arguments passed by command line are invalid
 */
public class InvalidProgramArgumentsException extends InvalidDataException {
    public InvalidProgramArgumentsException(String s) {
        super(s);
    }
}
