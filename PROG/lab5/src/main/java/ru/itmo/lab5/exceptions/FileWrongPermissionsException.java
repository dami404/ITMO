package ru.itmo.lab5.exceptions;
/**
 * thrown when not enough permission to access the file
 */
public class FileWrongPermissionsException extends FileException{
    public FileWrongPermissionsException(String s){
        super(s);
    }
}