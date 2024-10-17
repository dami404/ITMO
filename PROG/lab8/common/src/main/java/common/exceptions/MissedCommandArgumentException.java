package common.exceptions;

/**
 * thrown  when user doesn't enter required command argument
 */
public class MissedCommandArgumentException extends InvalidCommandArgumentException {
    public MissedCommandArgumentException() {
        super("[MissedCommandArgumentException] missed command argument");
    }
}
