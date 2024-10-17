package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;
import static common.utils.Parser.*;
public class RemoveByIdCommand extends CommandImpl{
    private CollectionManager<MusicBand> collectionManager;
    public RemoveByIdCommand(CollectionManager<MusicBand> cm){
        super("remove_by_id",CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException {
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if(!hasStringArgument()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if(!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("no such id");

        boolean success = collectionManager.removeByID(id);
        if (success) return "element #" + Integer.toString(id) + " removed";
        else throw new CommandException("cannot remove");
    }
    
}
