package commands;

import collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.EmptyCollectionException;

public class ShowCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public ShowCommand(WorkerManager cm) {
        super("show", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.sort();
        return collectionManager.serializeCollection();
    }

}
