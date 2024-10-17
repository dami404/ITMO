package ru.itmo.lab5.commands;

/**
 * command wrapper class for command parsing
 */
public class CommandWrapper {
    private final String argument;
    private final String command;
    public CommandWrapper(String cmd, String arg){
        argument = arg;
        command = cmd;
    }
    public CommandWrapper(String cmd){
        argument = null;
        command = cmd;
    }

    public String getCommand(){
        return command;
    }

    public String getArg(){
        return argument;
    }
}

