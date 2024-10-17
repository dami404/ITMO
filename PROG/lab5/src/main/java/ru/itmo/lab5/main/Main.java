package ru.itmo.lab5.main;

import ru.itmo.lab5.data.*;
import ru.itmo.lab5.file.FileManager;
import ru.itmo.lab5.io.*;

import java.io.PrintStream;
import ru.itmo.lab5.collection.CollectionManager;
import ru.itmo.lab5.collection.MusicBandCollectionManager;

import static ru.itmo.lab5.io.OutputManager.*;

import ru.itmo.lab5.commands.*;
public class Main {
    
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        print("Hello,my name is Nommy. I'll help you with creating your own music band");
        print("\n");
        FileManager fileManager = new FileManager();
        CollectionManager<MusicBand> collectionManager = new MusicBandCollectionManager();
        if (args.length!=0){
            fileManager.setPath(args[0]);
            collectionManager.deserializeCollection(fileManager.read());
        } else{
            print("no file passed by argument. you can load file using load command");
        }
        
        InputManager consoleManager = new ConsoleInputManager();
        CommandManager commandManager = new CommandManager(collectionManager,consoleManager,fileManager);
        commandManager.consoleMode();    
    }
}

