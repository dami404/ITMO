package commands;

import collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.MusicBand;
import common.exceptions.*;

import java.util.List;

public class PrintFieldAscendingLabelCommand extends CommandImpl {
    private CollectionManager<MusicBand> collectionManager;
    public PrintFieldAscendingLabelCommand(CollectionManager<MusicBand> collectionManager) {
        super("print_field_ascending_label", CommandType.NORMAL );
        this.collectionManager=collectionManager;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        List<String> list=collectionManager.printFieldAscendingLabel();
        return list.stream().reduce("", (a,b)->a + b + "\n");

    }
}
