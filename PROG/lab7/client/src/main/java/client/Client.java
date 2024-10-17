package client;

import commands.ClientCommandManager;
import common.auth.User;
import common.connection.Request;
import common.connection.Response;
import common.connection.SenderReceiver;
import common.exceptions.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

import static common.io.OutputManager.printErr;

/**
 * client class
 */
public class Client extends Thread implements SenderReceiver {
    private SocketAddress address;
    private DatagramSocket socket;
    public final int MAX_TIME_OUT = 1000;
    public final int MAX_ATTEMPTS = 3;
    private User user = null;
    private boolean running;
    private ClientCommandManager commandManager;

    /**
     * initialize client
     *
     * @param addr Address
     * @param p port
     * @throws ConnectionException
     */
    private void init(String addr, int p) throws ConnectionException {
        connect(addr, p);
        running = true;
        commandManager = new ClientCommandManager(this);
        setName("client thread");
    }

    public Client(String addr, int p) throws ConnectionException {
        init(addr, p);
    }

    public void setUser(User usr){
        user = usr;
    }
    public User getUser(){
        return user;
    }
    /**
     * connects client to server
     *
     * @param addr Address
     * @param p port
     * @throws ConnectionException
     */
    public void connect(String addr, int p) throws ConnectionException {
        try {
            address = new InetSocketAddress(InetAddress.getByName(addr), p);
        } catch (UnknownHostException e) {
            throw new InvalidAddressException();
        } catch (IllegalArgumentException e) {
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
     *
     * @param request request
     * @throws ConnectionException
     */
    public void send(Request request) throws ConnectionException {
        try {
            request.setStatus(Request.Status.SENT_FROM_CLIENT);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objOutput = new ObjectOutputStream(byteArrayOutputStream);
            objOutput.writeObject(request);
            DatagramPacket requestPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), address);
            socket.send(requestPacket);
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new ConnectionException("something went wrong while sending request");
        }
    }

    /**
     * receive message from server
     *
     * @return response
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public Response receive() throws ConnectionException, InvalidDataException {

        ByteBuffer bytes = ByteBuffer.allocate(BUFFER_SIZE);
        DatagramPacket receivePacket = new DatagramPacket(bytes.array(), bytes.array().length);
        try {
            socket.receive(receivePacket);
        } catch (SocketTimeoutException e) {
            for (int attempts = MAX_ATTEMPTS; attempts > 0; attempts--) {
                printErr("server response timeout exceeded, trying to reconnect. " + attempts + " attempts left");
                try {
                    socket.receive(receivePacket);
                    break;
                } catch (IOException ignored) {

                }
            }

            throw new ConnectionTimeoutException();
        } catch (IOException e) {
            throw new ConnectionException("something went wrong while receiving response");
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes.array()));
            return (Response) objectInputStream.readObject();
        } catch (ClassNotFoundException | ClassCastException | IOException e) {
            throw new InvalidReceivedDataException();
        }
    }

    /**
     * runs client until interrupt
     */
    @Override
    public void run() {
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