package commands;

import collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;

public class AddIfMinCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public AddIfMinCommand(WorkerManager cm) {
        super("add_if_min", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        collectionManager.addIfMin(getWorkerArg());
        return ("Added element: " + getWorkerArg().toString());
    }

}
