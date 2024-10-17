package ru.itmo.lab5.exceptions;
/**
 * thrown when collection is empty
 */
public class EmptyCollectionException extends CommandException{
    public EmptyCollectionException(){
        super("collection is empty");
    }
}
