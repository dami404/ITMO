package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.MusicBand;
import common.exceptions.EmptyCollectionException;
import common.exceptions.InvalidDataException;

public class RemoveLastCommand extends CommandImpl {
    private CollectionManager<MusicBand> collectionManager;
    public RemoveLastCommand(CollectionManager<MusicBand> cm){
        super("remove_last", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException {
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        Integer id = collectionManager.getCollection().get(collectionManager.getCollection().size()-1).getId();
        collectionManager.removeFirst();
        return "Element #" + Integer.toString(id) + " successfully deleted";
    }

}
