package common.commands;

import common.connection.Request;
import common.connection.Response;
import common.exceptions.*;


/**
 * Command callback interface
 */

public interface Command {
    Response run() throws InvalidDataException, CommandException, FileException, ConnectionException, CollectionException;

    String getName();

    CommandType getType();

    void setArgument(Request a);
}