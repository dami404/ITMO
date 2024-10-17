package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;

public class InfoCommand extends CommandImpl{
    private CollectionManager<MusicBand> collectionManager;
    public InfoCommand(CollectionManager<MusicBand> cm){
        super("info",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        return collectionManager.getInfo();
    }

}
