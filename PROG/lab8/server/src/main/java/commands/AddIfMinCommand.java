package commands;

import common.collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.CollectionOperation;

public class AddIfMinCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public AddIfMinCommand(WorkerManager cm) {
        super("add_if_min", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        collectionManager.addIfMin(getWorkerArg());
        return ("Added element: " + getWorkerArg().toString());
    }

}
