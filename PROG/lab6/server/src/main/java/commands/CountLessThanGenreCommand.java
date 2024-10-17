package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.MusicBand;
import common.exceptions.*;

public class CountLessThanGenreCommand extends CommandImpl {
    private CollectionManager<MusicBand> collectionManager;
    public CountLessThanGenreCommand(CollectionManager<MusicBand> collectionManager) {
        super("count_less_than_genre", CommandType.NORMAL);
        this.collectionManager=collectionManager;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException {
        if(!hasStringArgument()) throw new MissedCommandArgumentException();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        String genre = getStringArg();
        Integer result =collectionManager.countLessThenGenre(genre);
        if(result==0) return "There are no genres that are *lower than yours\n"+"*-lower in alphabet";
        else return ""+result;
    }
}
