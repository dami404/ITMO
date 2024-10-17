package commands;

import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.ExitException;

public class ExitCommand extends CommandImpl {
    public ExitCommand() {
        super("exit", CommandType.SPECIAL);
    }

    @Override
    public String execute() {
        throw new ExitException();
    }
}
