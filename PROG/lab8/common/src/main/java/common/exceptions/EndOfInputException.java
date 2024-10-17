package common.exceptions;

public class EndOfInputException extends RuntimeException {
    public EndOfInputException() {
        super("end of input");
    }
}
