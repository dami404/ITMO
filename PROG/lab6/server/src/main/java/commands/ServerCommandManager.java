package commands;

import common.data.MusicBand;
import common.file.ReaderWriter;
import server.*;
import log.*;
import common.commands.*;
import common.connection.*;
import common.exceptions.*;

import collection.CollectionManager;

public class ServerCommandManager extends CommandManager{
    private Server server;
    private CollectionManager<MusicBand> collectionManager;
    private ReaderWriter fileManager;

    public ServerCommandManager(Server server) {
        this.server = server;
        collectionManager=server.getCollectionManager();
        fileManager=server.getFileManager();
        addCommand(new AddCommand(collectionManager));
        addCommand(new ClearCommand(collectionManager));
        addCommand(new CountLessThanGenreCommand(collectionManager));
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new ExitCommand());
        addCommand(new FilterStartsWithNameCommand(collectionManager));
        addCommand(new HelpCommand());
        addCommand(new InfoCommand(collectionManager));
        addCommand(new PrintFieldAscendingLabelCommand(collectionManager));
        addCommand(new RemoveByIdCommand(collectionManager));
        addCommand(new RemoveFirstCommand(collectionManager));
        addCommand(new RemoveLastCommand(collectionManager));
        addCommand(new SaveCommand(collectionManager,fileManager));
        addCommand(new ShowCommand(collectionManager));
        addCommand(new UpdateCommand(collectionManager));
    }
    public Server getServer(){
        return server;
    }
    @Override
    public AnswerMsg runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        try {
            Command cmd = getCommand(msg.getCommandName());
            cmd.setArgument(msg);
            res = (AnswerMsg) cmd.run();
        } catch (CommandException e){
            res.error(e.getMessage());
        }
        switch (res.getStatus()){
            case EXIT:
                Log.logger.fatal(res.getMessage());
                server.close();
                break;
            case ERROR:
                Log.logger.error(res.getMessage());
                break;
            default:
                Log.logger.info(res.getMessage());
                break;
        }
        return res;
    }
}
