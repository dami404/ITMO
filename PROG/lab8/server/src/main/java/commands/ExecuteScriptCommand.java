package commands;

import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.*;

public class ExecuteScriptCommand extends CommandImpl {
    ServerCommandManager commandManager;

    public ExecuteScriptCommand(ServerCommandManager cm) {
        super("execute_script", CommandType.SPECIAL);
        commandManager = cm;
    }

    @Override
    public String execute() throws FileException, InvalidDataException, ConnectionException {
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        if (commandManager.getStack().contains(getStringArg())) throw new RecursiveScriptExecuteException();
        commandManager.getStack().add(getStringArg());
        ServerCommandManager process = new ServerCommandManager(commandManager.getServer());
        process.fileMode(getStringArg());

        commandManager.getStack().pop();
        return "script successfully executed";
    }
}
