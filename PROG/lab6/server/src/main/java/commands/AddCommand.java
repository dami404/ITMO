package commands;

import common.exceptions.*;
import collection.CollectionManager;
import common.commands.*;
import common.data.*;
public class AddCommand extends CommandImpl{
    private CollectionManager<MusicBand> collectionManager;
    public AddCommand(CollectionManager<MusicBand> cm){
        super("add",CommandType.NORMAL);
        collectionManager = cm;
    }
    public CollectionManager<MusicBand> getManager(){
        return collectionManager;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException {
        getManager().add(getMusicBandArgument());
        return "Added element: " + getMusicBandArgument().toString();
    }
}
