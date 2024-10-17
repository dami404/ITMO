package common.commands;

import common.connection.*;
import common.data.Worker;
import common.exceptions.*;

import java.util.HashSet;

/**
 * basic command implementation
 */
public abstract class CommandImpl implements Command {
    private final CommandType type;
    private final String name;
    private Request arg;
    private CollectionOperation operation;
    public CommandImpl(String n, CommandType t, CollectionOperation op) {
        name = n;
        type = t;
        operation = op;
    }

    public  CommandImpl(String n, CommandType t){
        name = n;
        type = t;
        operation = CollectionOperation.NONE;
    }


    public CommandType getType() {
        return type;
    }

    public CollectionOperation getOperation(){
        return operation;
    }
    public String getName() {
        return name;
    }

    /**
     * custom execute command
     *
     * @return String
     * @throws InvalidDataException
     * @throws CommandException
     * @throws FileException
     * @throws ConnectionException
     */
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException, CollectionException {
        return "";
    }

    /**
     * wraps execute into response
     *
     * @return response
     */
    @Override
    public Response run() throws InvalidDataException, CommandException, FileException, ConnectionException, CollectionException {
        AnswerMsg res = new AnswerMsg();
        res.info(execute());
        return res;
    }

    public Request getArgument() {
        return arg;
    }

    public void setArgument(Request req) {
        arg = req;
    }

    public boolean hasStringArg() {
        return arg != null && arg.getStringArg() != null && !arg.getStringArg().equals("");
    }

    public boolean hasWorkerArg() {
        return arg != null && arg.getWorker() != null;
    }

    public boolean hasUserArg() {
        return arg != null && arg.getUser() != null && arg.getUser().getLogin() != null;
    }

    public String getStringArg() {
        return getArgument().getStringArg();
    }

    public Worker getWorkerArg() {
        return getArgument().getWorker();
    }
}
