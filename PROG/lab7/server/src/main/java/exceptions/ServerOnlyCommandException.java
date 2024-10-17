package exceptions;

import common.exceptions.CommandException;

/**
 * thrown if command is only for server
 */
public class ServerOnlyCommandException extends CommandException {
    public ServerOnlyCommandException() {
        super("this command is only for server");
    }
}
