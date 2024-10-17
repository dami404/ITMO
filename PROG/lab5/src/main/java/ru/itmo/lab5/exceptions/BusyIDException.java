package ru.itmo.lab5.exceptions;

/**
 * thrown  when user doesn't enter required command argument
 */
public class BusyIDException extends InvalidCommandArgumentException{
    public BusyIDException (){
        super("this id is already used.");
    }
}
