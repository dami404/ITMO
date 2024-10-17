package commands;

import common.collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.InvalidDataException;

public class InfoCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public InfoCommand(WorkerManager cm) {
        super("info", CommandType.NORMAL);
        collectionManager = cm;//TODO
    }

    @Override
    public String execute() {
        return collectionManager.getInfo();
    }

}
