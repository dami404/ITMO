package commands;

import common.collection.WorkerManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.exceptions.EmptyCollectionException;
import common.exceptions.InvalidDataException;
import database.WorkerDatabaseManager;

public class ClearCommand extends CommandImpl {
    private final WorkerDatabaseManager collectionManager;

    public ClearCommand(WorkerManager cm) {
        super("clear", CommandType.NORMAL, CollectionOperation.REMOVE);
        collectionManager = (WorkerDatabaseManager) cm;
    }

    @Override
    public Response run() {
        AnswerMsg answerMsg = new AnswerMsg();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        User user = getArgument().getUser();
        answerMsg.setCollection(collectionManager.clear(user));
        answerMsg.info( "collection cleared");
        return answerMsg;
    }

}
