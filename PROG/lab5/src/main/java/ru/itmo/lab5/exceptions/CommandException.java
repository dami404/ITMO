package ru.itmo.lab5.exceptions;
/**
 * base class for all exceptions caused by executing commands
 */
public class CommandException extends RuntimeException{
    public CommandException(String message) {
        super(message);
    }
}
