package commands;

import collection.WorkerManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.EmptyCollectionException;
import common.exceptions.InvalidDataException;
import database.WorkerDatabaseManager;

public class ClearCommand extends CommandImpl {
    private final WorkerDatabaseManager collectionManager;

    public ClearCommand(WorkerManager cm) {
        super("clear", CommandType.NORMAL);
        collectionManager = (WorkerDatabaseManager) cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        User user = getArgument().getUser();
        collectionManager.clear(user);
        return "collection cleared";
    }

}
