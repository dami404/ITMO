package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;

import commands.ClientCommandManager;
import common.connection.Request;
import common.connection.Response;
import common.connection.SenderReceiver;
import common.exceptions.*;

import static common.io.OutputManager.printErr;

/**
 * client class
 */
public class Client extends  Thread implements SenderReceiver {
    private SocketAddress address;
    private DatagramSocket socket;
    public final int MAX_TIME_OUT = 1000;
    public final int MAX_ATTEMPTS = 3;

    private boolean running;
    private ClientCommandManager commandManager;

    /**
     * initialize client
     * @param addr
     * @param p
     * @throws ConnectionException
     */
    private void init(String addr, int p) throws ConnectionException {
        connect(addr,p);
        running = true;
        commandManager = new ClientCommandManager(this);
        setName("client thread");
    }

    public Client(String addr, int p) throws ConnectionException{
        init(addr,p);
    }

    /**
     * connects client to server
     * @param addr
     * @param p
     * @throws ConnectionException
     */
    public void connect(String addr, int p) throws ConnectionException{
        try{
            address = new InetSocketAddress(InetAddress.getByName(addr),p);
        }
        catch(UnknownHostException e){
            throw new InvalidAddressException();
        }
        catch(IllegalArgumentException e){
            throw new InvalidPortException();
        }
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(MAX_TIME_OUT);
        } catch (IOException e) {
            throw new ConnectionException("cannot open socket");
        }
    }

    /**
     * sends request to server
     * @param request
     * @throws ConnectionException
     */
    public void send(Request request) throws ConnectionException{
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objOutput = new ObjectOutputStream(byteArrayOutputStream);
            objOutput.writeObject(request);
            DatagramPacket requestPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), address);
            socket.send(requestPacket);
            byteArrayOutputStream.close();
        }
        catch (IOException e){
            throw new ConnectionException("something went wrong while sending request");
        }
    }

    /**
     * receive message from server
     * @return
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public Response receive()throws ConnectionException, InvalidDataException{

        ByteBuffer bytes = ByteBuffer.allocate(BUFFER_SIZE);
        DatagramPacket receivePacket = new DatagramPacket(bytes.array(), bytes.array().length);
        try{
            socket.receive(receivePacket);
        }
        catch (SocketTimeoutException e){
            int attempts = MAX_ATTEMPTS;
            boolean success = false;
            for(;attempts>0;attempts--) {
                printErr("server response timeout exceeded, trying to reconnect. " + Integer.toString(attempts)+ " attempts left");
                try{
                    socket.receive(receivePacket);
                    success = true;
                    break;
                }
                catch (IOException error){

                }
            }

            throw new ConnectionTimeoutException();
        }

        catch(IOException e){
            throw new ConnectionException("something went wrong while receiving response");
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes.array()));
            return (Response) objectInputStream.readObject();
        } catch (ClassNotFoundException|ClassCastException|IOException e) {
            throw new InvalidReceivedDataException();
        }
    }

    /**
     * runs client until interrupt
     */
    @Override
    public void run(){
        commandManager.consoleMode();
        close();
    }

    /**
     * close client
     */
    public void close() {
        running = false;
        commandManager.close();
        socket.close();
    }
}