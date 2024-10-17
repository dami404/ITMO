package commands;

import common.collection.WorkerManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.exceptions.*;

import java.util.Arrays;
import java.util.List;

import static common.utils.Parser.parseId;

public class UpdateCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public UpdateCommand(WorkerManager cm) {
        super("update", CommandType.NORMAL, CollectionOperation.UPDATE);
        collectionManager = cm;
    }


    @Override
    public Response run() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (!hasStringArg() || !hasWorkerArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id)) throw new NoSuchIdException(id);

        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();

        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new PermissionException(owner);

        collectionManager.updateByID(id, getWorkerArg());
        return new AnswerMsg().info( "element #" + id + " updated").setCollection(List.of(getWorkerArg())).setCollectionOperation(CollectionOperation.UPDATE);
    }

}
