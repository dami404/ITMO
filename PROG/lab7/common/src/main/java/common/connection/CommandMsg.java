package common.connection;

import common.auth.User;
import common.data.Worker;

/**
 * Message witch include command and arguments
 */
public class CommandMsg implements Request {
    private final String commandName;
    private final String commandStringArgument;
    private final Worker worker;
    private User user;
    private Request.Status status;

    public CommandMsg(String commandNm, String commandSA, Worker w) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        worker = w;
        user = null;
        status = Status.DEFAULT;
    }

    public CommandMsg(String commandNm, String commandSA, Worker w, User usr) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        worker = w;
        user = usr;
        status = Status.DEFAULT;
    }

    public void setStatus(Status s) {
        status = s;
    }

    public Status getStatus() {
        return status;
    }

    public void setUser(User usr) {
        user = usr;
    }


    /**
     * @return Command name.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Command string argument.
     */
    public String getStringArg() {
        return commandStringArgument;
    }

    /**
     * @return Command object argument.
     */
    public Worker getWorker() {
        return worker;
    }

    public User getUser() {
        return user;
    }
}