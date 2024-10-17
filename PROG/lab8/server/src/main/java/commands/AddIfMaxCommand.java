package commands;

import common.collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.CollectionOperation;

public class AddIfMaxCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public AddIfMaxCommand(WorkerManager cm) {
        super("add_if_max", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        collectionManager.addIfMax(getWorkerArg());
        return ("Added element: " + getWorkerArg().toString());
    }
}
