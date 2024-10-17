package commands;

import collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;

public class AddIfMaxCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public AddIfMaxCommand(WorkerManager cm) {
        super("add_if_max", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        collectionManager.addIfMax(getWorkerArg());
        return ("Added element: " + getWorkerArg().toString());
    }
}
