package ru.itmo.lab5.exceptions;
/**
 * base class for all exceptions caused by invalid input
 */
public class InvalidDataException extends Exception{
    public InvalidDataException(String message) {
        super(message);
    }
}