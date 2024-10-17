package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;
public class ClearCommand extends CommandImpl{
    private CollectionManager<MusicBand> collectionManager;
    public ClearCommand(CollectionManager<MusicBand> cm){
        super("clear",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.clear();
        return "collection cleared";
    }

}
