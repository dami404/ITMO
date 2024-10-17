package commands;

import collection.WorkerManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.AuthException;
import common.exceptions.EmptyCollectionException;
import common.exceptions.InvalidDataException;

public class RemoveFirstCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public RemoveFirstCommand(WorkerManager cm) {
        super("remove_first", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();

        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        int id = collectionManager.getCollection().iterator().next().getId();
        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();
        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new AuthException("you dont have permission, element was created by " + owner);
        collectionManager.removeFirst();
        return "element #" + id + " successfully deleted";
    }

}
