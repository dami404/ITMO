package common.commands;

import common.connection.Request;
import common.connection.Response;
import common.exceptions.FileException;

public interface Commandable {

    /**
     * adds command
     *
     * @param cmd
     */
    void addCommand(Command cmd);

    /**
     * runs command
     *
     * @param req
     * @return response
     */
    Response runCommand(Request req);

    Command getCommand(String key);

    default Command getCommand(Request req) {
        return getCommand(req.getCommandName());
    }

    boolean hasCommand(String s);

    default boolean hasCommand(Request req) {
        return hasCommand(req.getCommandName());
    }

    /**
     * runs in command interpreter in console
     */
    void consoleMode();

    /**
     * executes script from file
     *
     * @param path
     */
    void fileMode(String path) throws FileException;
}
