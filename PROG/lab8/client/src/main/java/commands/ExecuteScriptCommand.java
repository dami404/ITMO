package commands;

import common.commands.*;
import common.exceptions.*;

public class ExecuteScriptCommand extends CommandImpl {
    ClientCommandManager commandManager;

    public ExecuteScriptCommand(ClientCommandManager cm) {
        super("execute_script", CommandType.NORMAL);
        commandManager = cm;
    }

    @Override
    public String execute() throws FileException,InvalidDataException,ConnectionException{
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        if (commandManager.getStack().contains(getStringArg())) throw new RecursiveScriptExecuteException();
        commandManager.getStack().add(getStringArg());
        ClientCommandManager process = new ClientCommandManager(commandManager.getClient());
        process.fileMode(getStringArg());
        commandManager.getStack().pop();
        return "script successfully executed";
    }
}

