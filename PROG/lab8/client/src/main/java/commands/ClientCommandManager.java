package commands;

import client.Client;

import static common.io.ConsoleOutputter.*;

import common.exceptions.*;
import commands.*;
import common.commands.*;
import common.connection.*;
import common.io.FileInputManager;

import java.io.File;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * command manager for client
 */
public class ClientCommandManager extends CommandManager {
    private final Client client;
    public ClientCommandManager(Client c) {
        client = c;
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
        addCommand(new PrintUniqueSalaryCommand(client.getWorkerManager()));
        addCommand(new GroupCountingByEndDateCommand(client));
        addCommand(new FilterStartsWithNameCommand(client.getWorkerManager()));
    }

    public Client getClient() {
        return client;
    }

    @Override

    public AnswerMsg runCommandUnsafe(Request msg) throws FileException, InvalidDataException, ConnectionException {
        AnswerMsg res = new AnswerMsg();
        if (hasCommand(msg)) {
            res = (AnswerMsg) super.runCommandUnsafe(msg);
            if(res.getStatus() == Response.Status.EXIT){
                res.info("shutting down...");
            }
        } else {
            //lock.lock();
                if(client.getUser()!=null && msg.getUser()==null) msg.setUser(client.getUser());
                else client.setAttemptUser(msg.getUser());
                try {
                    client.send(msg);
                    try {
                        res = (AnswerMsg) client.receive();
                    } catch (InvalidDataException e) {
                        throw new ConnectionException();
                    }
                }catch (ConnectionException e){
                    res.error(e.getMessage());
                }

            switch (res.getStatus()){
                case FINE:
                    client.getOutputManager().info(res.getMessage());
                    break;
                case ERROR:
                    client.getOutputManager().error(res.getMessage());
                    break;
                case AUTH_SUCCESS:
                    client.setUser(client.getAttemptUser());
                    client.setAuthSuccess(true);
                    break;
            }
            if(res.getStatus()== Response.Status.COLLECTION&&res.getCollectionOperation()!=CollectionOperation.NONE&&res.getCollection()!=null) {
                client.getWorkerManager().clear();
                client.getWorkerManager().applyChanges(res);
            }
            else if (res.getCollectionOperation()!=CollectionOperation.NONE&&res.getCollection()!=null){
                client.getWorkerManager().applyChanges(res);
            }
        }
        print(res.getMessage());
        return res;
    }
    @Override
    public AnswerMsg fileMode(String path) throws FileException, InvalidDataException, ConnectionException {
        currentScriptFileName=path;
        inputManager = new FileInputManager(path);
        isRunning = true;
        AnswerMsg answerMsg = new AnswerMsg();
        while (isRunning && inputManager.hasNextLine()) {
            CommandMsg commandMsg = inputManager.readCommand();
            answerMsg = (AnswerMsg) runCommandUnsafe(commandMsg);
            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
                break;
            } else if(answerMsg.getStatus()== Response.Status.ERROR){
                break;
            }
        }
        return answerMsg;
    }

    public Response runFile(File file) throws FileException, InvalidDataException, ConnectionException {
        currentScriptFileName=file.getName();
        inputManager = new FileInputManager(file);
        getStack().add(currentScriptFileName);
        isRunning = true;
        Response answerMsg = new AnswerMsg();
        while (isRunning && inputManager.hasNextLine()) {
            CommandMsg commandMsg = inputManager.readCommand();
            answerMsg = (AnswerMsg)runCommandUnsafe(commandMsg);
            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
                break;
            } else if(answerMsg.getStatus()== Response.Status.ERROR){
                break;
            }
        }
        return answerMsg;
    }
}
