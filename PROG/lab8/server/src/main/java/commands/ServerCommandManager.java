package commands;

import auth.UserManager;
import common.collection.WorkerManager;
import common.auth.User;
import common.commands.Command;
import common.commands.CommandManager;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.Request;
import common.connection.Response;
import common.data.Worker;
import common.exceptions.AuthException;
import common.exceptions.CommandException;
import common.exceptions.ConnectionException;
import log.Log;
import server.Server;

public class ServerCommandManager extends CommandManager {
    private final Server server;

    private final UserManager userManager;

    public ServerCommandManager(Server serv) {
        server = serv;
        WorkerManager collectionManager = server.getCollectionManager();
        userManager = server.getUserManager();
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new InfoCommand(collectionManager));
        addCommand(new AddCommand(collectionManager));
        addCommand(new AddIfMinCommand(collectionManager));
        addCommand(new AddIfMaxCommand(collectionManager));
        addCommand(new UpdateCommand(collectionManager));
        addCommand(new RemoveByIdCommand(collectionManager));
        addCommand(new ClearCommand(collectionManager));
        addCommand(new RemoveFirstCommand(collectionManager));
        addCommand(new ShowCommand(collectionManager));
        addCommand(new FilterStartsWithNameCommand(collectionManager));
        addCommand(new GroupCountingByEndDateCommand(collectionManager));
        addCommand(new PrintUniqueSalaryCommand(collectionManager));

        addCommand(new LoginCommand(userManager));
        addCommand(new RegisterCommand(userManager));
        addCommand(new ShowUsersCommand(userManager));
    }

    public Server getServer() {
        return server;
    }

    @Override
    public Response runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        User user = msg.getUser();
        String cmdName = msg.getCommandName();
        boolean isGeneratedByServer = (msg.getStatus() != Request.Status.RECEIVED_BY_SERVER);
        try {
            Command cmd = getCommand(msg);
            //allow to execute a special command without authorization
            if (cmd.getType() != CommandType.AUTH && cmd.getType() != CommandType.SPECIAL) {
                if (isGeneratedByServer) {
                    user = server.getHostUser();
                    msg.setUser(user);
                }
                if (user == null) throw new AuthException();
                if (!userManager.isValid(user)) throw new AuthException();

                //link user to worker
                Worker worker = msg.getWorker();
                if (worker != null) worker.setUser(user);
            }

            //executing command
            res = (AnswerMsg) super.runCommand(msg);
        } catch (ConnectionException | CommandException e) {
            res.error(e.getMessage());
        }
        String message = "";

        //format user and current command
        if (user != null) message += "[" + user.getLogin() + "] ";
        if (cmdName != null) message += "[" + cmdName + "] ";

        //format multiline output
        if (res.getMessage().contains("\n")) message += "\n";
        switch (res.getStatus()) {
            case EXIT:
                Log.logger.fatal(message + "shutting down...");
                server.close();
                break;
            case ERROR:
                Log.logger.error(message + res.getMessage());
                break;
            case AUTH_SUCCESS: //check if auth command was invoked by server terminal
                if (isGeneratedByServer) server.setHostUser(user);
                Log.logger.info(message + res.getMessage());
                break;
            default:
                Log.logger.info(message + res.getMessage());
                break;
        }
        return res;
    }
}
