package ru.itmo.lab5.commands;

import ru.itmo.lab5.exceptions.CommandException;
import ru.itmo.lab5.exceptions.InvalidDataException;
@FunctionalInterface
/**
 * Command callback interface
 */
public interface Command {
    public void run(String arg) throws CommandException, InvalidDataException;
}

