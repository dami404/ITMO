package common.exceptions;

/**
 * thrown when input doesnt match enum
 */
public class InvalidEnumException extends InvalidDataException {
    public InvalidEnumException() {
        super("wrong constant");
    }
    public InvalidEnumException(String s){
        super(s);
    }
}
