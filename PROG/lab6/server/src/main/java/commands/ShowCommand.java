package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;
public class ShowCommand extends CommandImpl{
    private CollectionManager<MusicBand> collectionManager;
    public ShowCommand(CollectionManager<MusicBand> cm){
        super("show",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute(){
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.sort();
        return collectionManager.serializeCollection();
    }

}
