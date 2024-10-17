package ru.itmo.lab5.io;
import ru.itmo.lab5.exceptions.*;
@FunctionalInterface
/**
 *user input callback
 */
public interface Askable<T>{
    T ask() throws InvalidDataException;
}
