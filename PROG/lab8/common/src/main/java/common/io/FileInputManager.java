package common.io;

import common.exceptions.FileException;
import common.file.FileManager;

import java.io.File;
import java.util.Scanner;

/**
 * Operates input
 */
public class FileInputManager extends InputManagerImpl {
    public FileInputManager(String path) throws FileException {
        super(new Scanner(new FileManager(path).read()));
        getScanner().useDelimiter("\n");
    }

    public FileInputManager(File file) throws FileException {
        super(new Scanner(new FileManager(file).read()));
        getScanner().useDelimiter("\n");
    }
}
