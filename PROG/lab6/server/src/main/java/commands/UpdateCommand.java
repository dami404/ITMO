package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;
import static common.utils.Parser.*;
public class UpdateCommand extends CommandImpl{
    private CollectionManager<MusicBand> collectionManager;
    public UpdateCommand(CollectionManager<MusicBand> cm){
        super("update",CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException{
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if(!hasStringArgument()||!hasMusicBandArgument()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if(!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("no such id");

        boolean success = collectionManager.updateByID(id,getMusicBandArgument());
        if (success) return "element #" + Integer.toString(id) + " updated";
        else throw new CommandException("cannot update");
    }
    
}
