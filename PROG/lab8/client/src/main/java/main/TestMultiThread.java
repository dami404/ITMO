package main;

import client.Client;
import common.connection.CommandMsg;
import common.exceptions.ConnectionException;

import java.util.LinkedList;
import java.util.List;

public class TestMultiThread {
    public static void main(String[] args)throws ConnectionException {
        List<Client> clients = new LinkedList<>();

        clients.add(new Client("localhost", 4445));
        clients.add(new Client("localhost", 4445));
        clients.add(new Client("localhost", 4445));
        clients.add(new Client("localhost", 4445));
        clients.add(new Client("localhost", 4445));

        for(Client c: clients){
            c.send(new CommandMsg("ABOBA", null, null));
        }

        new Client("localhost", 4445).start();

    }
}
