package commands;

import common.commands.*;
import common.exceptions.ExitException;

public class ExitCommand extends CommandImpl {
    public ExitCommand() {
        super("exit", CommandType.NORMAL);
    }

    @Override
    public String execute() {
        throw new ExitException();
    }
}
