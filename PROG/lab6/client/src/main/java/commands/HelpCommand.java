package commands;

import common.commands.*;
public class HelpCommand extends CommandImpl {
    public HelpCommand(){
        super("help", CommandType.NORMAL);
    }
    @Override 
    public String execute(){
        return CommandManager.getHelp();
    }
}
