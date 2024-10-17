package commands;

import collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.InvalidDataException;

public class InfoCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public InfoCommand(WorkerManager cm) {
        super("info", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        return collectionManager.getInfo();
    }

}
