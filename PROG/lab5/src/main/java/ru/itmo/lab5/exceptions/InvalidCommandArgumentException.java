package ru.itmo.lab5.exceptions;

/**
 * thrown when command argument is invalid
 */
public class InvalidCommandArgumentException extends CommandException{
    public InvalidCommandArgumentException(String s) {
        super(s);
    }
}
