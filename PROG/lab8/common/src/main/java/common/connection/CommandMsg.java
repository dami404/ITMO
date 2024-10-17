package common.connection;

import common.auth.User;
import common.data.Worker;

import java.net.InetSocketAddress;

/**
 * Message witch include command and arguments
 */
public class CommandMsg implements Request {
    private final String commandName;
    private String commandStringArgument;
    private Worker worker;
    private User user;
    private Request.Status status;
    private InetSocketAddress address;
    public CommandMsg(String commandNm, String commandSA, Worker w) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        worker = w;
        user = null;
        status = Status.DEFAULT;
    }
    public  CommandMsg(){
        commandName = null;
        commandStringArgument=null;
        worker = null;
        status = Status.DEFAULT;
    }
    public  CommandMsg(String s){
        commandName = s;
        commandStringArgument=null;
        worker = null;
        status = Status.DEFAULT;
    }

    public CommandMsg(String commandNm, String commandSA, Worker w, User usr) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        worker = w;
        user = usr;
        status = Status.DEFAULT;
    }

    public CommandMsg setStatus(Status s) {
        status = s;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public CommandMsg setUser(User usr) {
        user = usr;
        return this;
    }

    public CommandMsg setWorker(Worker w){
        worker = w;
        return this;
    }

    public CommandMsg setArgument(String s){
        commandStringArgument = s;
        return this;
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


    public InetSocketAddress getBroadcastAddress() {
        return address;
    }
    public CommandMsg setBroadcastAddress(InetSocketAddress addr){
        address=addr;
        return this;
    }
}