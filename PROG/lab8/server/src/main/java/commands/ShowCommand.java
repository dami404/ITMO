package commands;

import common.collection.WorkerManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.exceptions.EmptyCollectionException;

public class ShowCommand extends CommandImpl {
    private final WorkerManager collectionManager;

    public ShowCommand(WorkerManager cm) {
        super("show", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public Response run() {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.sort();
        return new AnswerMsg().info(collectionManager.serializeCollection()).setCollection(collectionManager.getCollection()).setStatus(Response.Status.COLLECTION);
    }

}
