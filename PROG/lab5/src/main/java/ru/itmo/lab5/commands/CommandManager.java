

package ru.itmo.lab5.commands;

import static ru.itmo.lab5.io.OutputManager.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

import ru.itmo.lab5.collection.CollectionManager;
import ru.itmo.lab5.data.*;
import ru.itmo.lab5.exceptions.*;
import ru.itmo.lab5.file.ReaderWriter;
import ru.itmo.lab5.io.*;

public class CommandManager implements Commandable{
private Map<String,Command> map;
private CollectionManager<MusicBand> collectionManager;

    
    private InputManager inputManager;
    private ReaderWriter fileManager;
    private boolean isRunning;
    private String currentScriptFileName;

    private static Stack<String> callStack = new Stack<>();

    public void clearStack(){
        callStack.clear();
    }

    public  CommandManager(CollectionManager<MusicBand> cManager, InputManager iManager, ReaderWriter fManager){
        isRunning = false;
        this.inputManager = iManager;
        this.collectionManager = cManager;
        this.fileManager = fManager;

        currentScriptFileName="";
        map=new HashMap<String,Command>();
        addCommand("info",(a)->print(collectionManager.getInfo()));
        addCommand("help",(a)->print(getHelp()));

        addCommand("show",(a) ->{ 
            if(collectionManager.getCollection().isEmpty()) print("Your Collection is empty");
            else print(collectionManager.serializeCollection());
        });

        addCommand("add",(a)->{
            collectionManager.add(inputManager.readMBand());
        });
        addCommand("update",(a)->{
            int id=0;
            if(a==null || a.equals("")){
                throw new MissedCommandArgumentException();
            }
            try{
                id=Integer.parseInt(a);
            }catch (NumberFormatException e){
                throw new InvalidCommandArgumentException("Id must be integer");
            }
            if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
            if(!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("No such id");

            collectionManager.updateByID(id, inputManager.readMBand());
        });

        addCommand("remove_by_id",(a)->{
            int id=0;
            if(a==null || a.equals("")) throw new MissedCommandArgumentException();
            try{
                id=Integer.parseInt(a);
            }catch(NumberFormatException e){
                throw new InvalidCommandArgumentException("Id must be integer");
            }
            if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
            if(!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("No such id");

            collectionManager.removeByID(id);
        });
        addCommand("insert_at",(a)->{
            if(a==null || a.equals("")) throw new MissedCommandArgumentException();
            
            if(callStack.contains(a)) {
                throw new BusyIDException ();

            }
            
            else {collectionManager.insertAtIndex(inputManager.readMBand(),a);}



            /**
             * int i=0;
            
            if(a==null || a.equals("")) throw new MissedCommandArgumentException();
            try{
                i=Integer.parseInt(a);
            }catch( NumberFormatException e){
                throw new InvalidCommandArgumentException("Id must be integer");
            }
            collectionManager.insertAtIndex(i);
             */
            
        });

        addCommand("clear",(a)->{
            collectionManager.clear();

        });
        addCommand("save",(a)->{
            if(!(a==null || a.equals(""))) fileManager.setPath(a);
            if(collectionManager.getCollection().isEmpty()) print("Collection is empty");
            if(!fileManager.write(collectionManager.serializeCollection())) throw new CommandException("Can not save collection");
        });

        addCommand("execute_script",(a)->{
            if(a==null || a.equals("")){
                 throw new MissedCommandArgumentException();
            }

            if(callStack.contains(currentScriptFileName)) throw new RecursiveScriptExecuteException();

            callStack.push(currentScriptFileName);
            CommandManager process= new CommandManager(collectionManager, inputManager, fileManager);
            process.fileMode(a);
            callStack.pop();
            //print("Successfully executed script "+ a);
        });
        addCommand("exit",(a)->isRunning=false);
        
        addCommand("remove_first",(a)->{
            
            if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
            collectionManager.removeFirst();
        });




        
        addCommand("remove_last",(a)->{
            
            if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
            collectionManager.removeLast();
        });



        addCommand("count_less_than_genre",(a)->{
            if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
            if(!MusicGenre.isPresent(a)) throw new InvalidEnumException();
            if(a==null || a.equals("")) throw new MissedCommandArgumentException();
            
            print(collectionManager.countLessThenGenre(a));
        });

        addCommand("filter_starts_with_name",(a)->{
            if(a==null || a.equals("")) throw new MissedCommandArgumentException();
            else{
                if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
                collectionManager.filterStartsWithName(a);
            }
        });

        addCommand("print_field_ascending_label",(a)->{
            if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
            print(collectionManager.printFieldAscendingLabel());
        });

    }

    public void addCommand(String key,Command c){
        map.put(key,c);
    }

    public void runCommand(String s,String arg){
        try{
            if(!hasCommand(s)) throw new NoSuchCommandException();
            map.get(s).run(arg);

        }catch(CommandException|InvalidDataException e){
            printErr(e.getMessage());
        }
    }

    public void runCommand(String s){
        runCommand(s,null);
    }

    public boolean hasCommand(String s){
        return map.containsKey(s);
    }

        public void consoleMode(){
            inputManager = new ConsoleInputManager();
            isRunning=true;
            while(isRunning){
                try{
                System.out.print("Enter a command(help to get the command list): ");
                CommandWrapper pair= inputManager.readCommand();
                runCommand(pair.getCommand(),pair.getArg());}
                catch(NoSuchElementException e){
                    inputManager=new ConsoleInputManager();
                }
            }
        }

        public void fileMode(String path){
            currentScriptFileName=path;
            isRunning=true;
            inputManager=new FileInputManager(path);
            isRunning=true;
            while(isRunning && inputManager.getScanner().hasNextLine()){
                CommandWrapper pair=  inputManager.readCommand(); 
                runCommand(pair.getCommand(), pair.getArg());  
            }
            
        }

        public static String getHelp(){
            return "\r\nhelp : show help for available commands\r\n\r\ninfo : Write to standard output information about the collection (type,\r\ninitialization date, number of elements, etc.)\r\n\r\nshow : print to standard output all elements of the collection in\r\nstring representation\r\n\r\nadd {element} : add a new element to the collection\r\n\r\nupdate id {element} : update the value of the collection element whose id\r\nequal to given\r\n\r\nremove_by_id id remove_by_id id : remove an element from the collection by its id\r\n\r\nclear : clear the collection\r\n\r\nsave (file_name - optional) : save the collection to a file\r\n\r\nexecute_script file_name : read and execute script from specified file.\r\nThe script contains commands in the same form in which they are entered\r\nuser is interactive.\r\n\r\nexit : exit the program (without saving to a file)\r\n\r\ninsert_at index {element} : add a new element to the specified position\r\n\r\nremove_first : remove the first element from the collection\r\n\r\nremove_last  : remove the last element from the collection \r\n\r\ncount_less_than_genre genre : print the number of elements whose genre field value is less than the specified one\r\n\r\nfilter_starts_with_name name : output elements, value of field name\r\nwhich starts with the given substring\r\n\r\nprint_field_ascending_label :  print the values of the label field of all elements in ascending order\r\n";
        }
}


 