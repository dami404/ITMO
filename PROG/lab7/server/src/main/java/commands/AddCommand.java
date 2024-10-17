package commands;

import collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.CommandException;
import common.exceptions.InvalidDataException;

public class AddCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public AddCommand(WorkerManager cm) {
        super("add", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException {
        collectionManager.add(getWorkerArg());
        return "Added element: " + getWorkerArg().toString();
    }
}
