package common.io;

import common.exceptions.InvalidDataException;

@FunctionalInterface
/**
 *user input callback
 */
public interface Askable<T> {
    T ask() throws InvalidDataException;
}