package commands;

import common.collection.WorkerManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.data.Worker;
import common.exceptions.*;

import java.util.Arrays;
import java.util.List;

import static common.utils.Parser.parseId;

public class RemoveByIdCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public RemoveByIdCommand(WorkerManager cm) {
        super("remove_by_id", CommandType.NORMAL, CollectionOperation.REMOVE);
        collectionManager = cm;
    }


    @Override
    public Response run() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id))
            throw new NoSuchIdException(id);

        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();

        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new PermissionException(owner);
        Worker worker = collectionManager.getByID(id);
        collectionManager.removeByID(id);
        return new AnswerMsg().info( "element #" + id + " removed").setCollection(List.of(worker));
    }

}
