package common.utils;

import common.exceptions.InvalidNumberException;

public class Parser {
    public static int parseId(String s) throws InvalidNumberException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new InvalidNumberException();
        }
    }
}
