package ru.itmo.lab5.exceptions;

import java.io.IOException;
/**
 * base class for all file exceptions
 */
public class FileException  extends IOException{
    public FileException(String msg){
        super(msg);
    }
}
