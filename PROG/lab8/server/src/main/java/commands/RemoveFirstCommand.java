package commands;

import common.collection.WorkerManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.data.Worker;
import common.exceptions.AuthException;
import common.exceptions.EmptyCollectionException;
import common.exceptions.InvalidDataException;
import common.exceptions.PermissionException;

import java.util.List;

public class RemoveFirstCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public RemoveFirstCommand(WorkerManager cm) {
        super("remove_first", CommandType.NORMAL, CollectionOperation.REMOVE);
        collectionManager = cm;
    }


    @Override
    public Response run() throws  AuthException {
        User user = getArgument().getUser();

        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        Worker worker = collectionManager.getCollection().iterator().next();
        int id = worker.getId();
        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();
        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new PermissionException(owner);
        collectionManager.removeFirst();
        return new AnswerMsg().info( "element #" + id + " successfully deleted").setCollection(List.of(worker));
    }

}
