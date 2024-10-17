package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;

import java.util.Date;

import collection.CollectionManager;
import collection.MusicBandCollectionManager;


import commands.ServerCommandManager;
import common.commands.*;
import common.connection.*;
import common.connection.Status;
import common.data.*;
import common.file.FileManager;
import common.file.ReaderWriter;

import exceptions.ServerOnlyCommandException;
import log.Log;
import common.exceptions.*;

/**
 * server class
 */
public class Server extends Thread implements SenderReceiver {

    private CollectionManager<MusicBand> collectionManager;
    private ReaderWriter fileManager;
    private ServerCommandManager commandManager;
    private int port;
    private InetSocketAddress clientAddress;
    private DatagramChannel channel;

    private volatile boolean running;

    private void init(int p, String path) throws ConnectionException{
        running=true;
        port = p;
        collectionManager = new MusicBandCollectionManager();
        fileManager = new FileManager(path);
        commandManager = new ServerCommandManager(this);
        try{
            collectionManager.deserializeCollection(fileManager.read());
        } catch (FileException e){
            Log.logger.error(e.getMessage());
        }
        host(port);
        setName("server thread");
        Log.logger.trace("starting server");
    }

    private void host(int p) throws ConnectionException{
        try{
            if(channel!=null && channel.isOpen()) channel.close();
            channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(port));
        }
        catch(AlreadyBoundException e){
            throw new PortAlreadyInUseException();
        }
        catch(IllegalArgumentException e){
            throw new InvalidPortException();
        }
        catch(IOException e){
            throw new ConnectionException("something went wrong during server initialization");
        }
    }

    public Server(int p, String path) throws ConnectionException{
        init(p,path);
    }

    /**
     * receives request from client
     * @return
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public Request receive() throws ConnectionException, InvalidDataException{
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            clientAddress = (InetSocketAddress) channel.receive(buf);
            Log.logger.trace("received request from " + clientAddress.toString());
        }catch (ClosedChannelException e){
            throw new ClosedConnectionException();
        } catch(IOException e){
            throw new ConnectionException("something went wrong during receiving request");
        }
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
            Request req  = (Request) objectInputStream.readObject();
            return req;
        } catch(ClassNotFoundException|ClassCastException|IOException e){
            throw new InvalidReceivedDataException();
        }

    }

    /**
     * sends response
     * @param response
     * @throws ConnectionException
     */
    public void send(Response response)throws ConnectionException{
        if (clientAddress == null) throw new InvalidAddressException("no client address found");
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            channel.send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()), clientAddress);
            Log.logger.trace("sent response to " + clientAddress.toString());
        } catch(IOException e){
            throw new ConnectionException("something went wrong during sending response");
        }
    }

    /**
     * runs server
     */
    public void run() {
        while (running) {
            AnswerMsg answerMsg = new AnswerMsg();
            try {
                try {
                    Request commandMsg = receive();
                    if (commandMsg.getMusicBand() != null) {
                        commandMsg.getMusicBand().setCreationDate(new Date());
                    }
                    if (commandManager.getCommand(commandMsg).getType() == CommandType.SERVER_ONLY) {
                        throw new ServerOnlyCommandException();
                    }
                    answerMsg = commandManager.runCommand(commandMsg);
                    if (answerMsg.getStatus() == Status.EXIT) {
                        close();
                    }
                } catch (CommandException e) {
                    answerMsg.error(e.getMessage());
                    Log.logger.error(e.getMessage());
                }
                send(answerMsg);
            } catch (ConnectionException | InvalidDataException e) {
                Log.logger.error(e.getMessage());
            }
        }
    }

    public void consoleMode(){
        commandManager.consoleMode();
    }

    /**
     * close server and connection
     */
    public void close(){
        try{
            running=false;
            channel.close();
        } catch (IOException e){
            Log.logger.error("cannot close channel");
        }
    }

    public Commandable getCommandManager(){
        return commandManager;
    }
    public ReaderWriter getFileManager(){
        return fileManager;
    }
    public CollectionManager<MusicBand> getCollectionManager(){
        return collectionManager;
    }

}