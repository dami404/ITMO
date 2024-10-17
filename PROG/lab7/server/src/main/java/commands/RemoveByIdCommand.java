package commands;

import collection.WorkerManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.*;


import static common.utils.Parser.parseId;

public class RemoveByIdCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public RemoveByIdCommand(WorkerManager cm) {
        super("remove_by_id", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id))
            throw new InvalidCommandArgumentException("no such id #" + id);

        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();

        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new AuthException("you dont have permission, element was created by " + owner);
        collectionManager.removeByID(id);
        return "element #" + id + " removed";
    }

}
