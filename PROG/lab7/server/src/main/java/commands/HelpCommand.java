package commands;

import common.commands.CommandImpl;
import common.commands.CommandManager;
import common.commands.CommandType;

public class HelpCommand extends CommandImpl {
    public HelpCommand() {
        super("help", CommandType.NORMAL);
    }

    @Override
    public String execute() {
        return (CommandManager.getHelp());
    }
}
