package ru.itmo.lab5.commands;

public interface Commandable {
    /**
     * adds command
     * @param key command name
     * @param cmd command callback
     */
    public void addCommand(String key, Command cmd);
    
    /**
     * executes command with argument
     * @param key command name
     * @param arg
     */
    public void runCommand(String key, String arg);

    /**
     * executes command without argument
     * @param key
     */
    public void runCommand(String key);

    public boolean hasCommand(String s);

    /**
     * runs in command interpreter in console
     */
    public void consoleMode();

    /**
     * executes script from file
     * @param path
     */
    public void fileMode(String path);
}
