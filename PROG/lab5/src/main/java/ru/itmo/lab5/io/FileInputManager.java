package ru.itmo.lab5.io;
import java.util.Scanner;

import ru.itmo.lab5.file.FileManager;
/**
 * Operates input
 */
public class FileInputManager extends InputManagerImpl{
    public FileInputManager(String path){
        super(new Scanner(new FileManager(path).read()));
        getScanner().useDelimiter("\n");
    }
}
