package common.commands;

import common.connection.*;
import common.exceptions.*;
import common.io.ConsoleInputManager;
import common.io.FileInputManager;
import common.io.InputManager;

import java.io.Closeable;
import java.util.*;

import static common.io.ConsoleOutputter.print;

public abstract class CommandManager implements Commandable, Closeable {
    private final Map<String, Command> map;
    protected InputManager inputManager;
    protected boolean isRunning;
    protected String currentScriptFileName;
    private static final Stack<String> callStack = new Stack<>();

    public void clearStack() {
        callStack.clear();
    }

    public Stack<String> getStack() {
        return callStack;
    }

    public String getCurrentScriptFileName() {
        return currentScriptFileName;
    }
    public void setCurrentScriptFileName(String cn){
        currentScriptFileName = cn;
    }
    public CommandManager() {
        isRunning = false;
        currentScriptFileName = "";
        map = new HashMap<String, Command>();
    }

    public void addCommand(Command c) {
        map.put(c.getName(), c);
    }

    public void addCommand(String key, Command c) {
        map.put(key, c);
    }

    public Command getCommand(String s) {
        if (!hasCommand(s)) throw new NoSuchCommandException(s);
        return map.get(s);
    }

    public boolean hasCommand(String s) {
        return map.containsKey(s);
    }

    public void consoleMode() {
        inputManager = new ConsoleInputManager();
        isRunning = true;
        while (isRunning) {
            Response answerMsg = new AnswerMsg();

            print("enter command (help to get command list): ");
            try {
                CommandMsg commandMsg = inputManager.readCommand();
                answerMsg = runCommand(commandMsg);
            } catch (InvalidDataException ignored){

            }
            catch (NoSuchElementException e) {
                close();
                print("user input closed");
                break;
            }

            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
            }
        }
    }

    public Response fileMode(String path) throws FileException, InvalidDataException, ConnectionException {
        currentScriptFileName = path;
        inputManager = new FileInputManager(path);
        isRunning = true;
        Response answerMsg = new AnswerMsg();
        while (isRunning && inputManager.hasNextLine()) {
            CommandMsg commandMsg = inputManager.readCommand();
            answerMsg = runCommand(commandMsg);
            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
                break;
            }
            if(answerMsg.getStatus()== Response.Status.ERROR){
                break;
            }
        }
        return answerMsg;
    }

    public Response runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        try {
            res = (AnswerMsg) runCommandUnsafe(msg);

        } catch (ExitException e) {
            res.setStatus(Response.Status.EXIT);
        } catch (CommandException | InvalidDataException | ConnectionException | FileException | CollectionException e) {
            res.error(e.getMessage());
        }
        return res;
    }
    public Response runCommandUnsafe(Request msg) throws CommandException, InvalidDataException, ConnectionException, FileException, CollectionException {
        AnswerMsg res = new AnswerMsg();

            Command cmd = getCommand(msg);
            cmd.setArgument(msg);
            res = (AnswerMsg) cmd.run();
            res.setCollectionOperation(cmd.getOperation());

        return res;
    }

    public static String getHelp() {
        return "\r\nhelp : show help for available commands\r\n\r\ninfo : Write to standard output information about the collection (type,\r\ninitialization date, number of elements, etc.)\r\n\r\nshow : print to standard output all elements of the collection in\r\nstring representation\r\n\r\nadd {element} : add a new element to the collection\r\n\r\nupdate id {element} : update the value of the collection element whose id\r\nequal to given\r\n\r\nremove_by_id id : remove an element from the collection by its id\r\n\r\nclear : clear the collection\r\n\r\nexecute_script file_name : read and execute script from specified file.\r\nThe script contains commands in the same form in which they are entered\r\nuser is interactive.\r\n\r\nexit : exit the program\r\n\r\nremove_first : remove the first element from the collection\r\n\r\nadd_if_max {element} : add a new element to the collection if its\r\nvalue is greater than the value of the largest element of this collection\r\n\r\nadd_if_min {element} : add a new element to the collection if it\r\nthe value is less than the smallest element of this collection\r\n\r\ngroup_counting_by_end_date : group the elements of the collection by\r\nthe value of the endDate field, display the number of elements in each group\r\n\r\nfilter_starts_with_name name : output elements, value of field name\r\nwhich starts with the given substring\r\n\r\nprint_unique_salary : print the unique values of the salary field of all\r\nitems in the collection\r\n\r\nregister {user} : register a new user\r\n\r\nlogin {user} : login user\r\n\r\nshow_users : show all registered users\r\n";
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void close() {
        setRunning(false);
    }
}
