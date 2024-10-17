package main;


import java.io.PrintStream;

import client.Client;
import common.exceptions.ConnectionException;
import common.exceptions.InvalidPortException;
import common.exceptions.InvalidProgramArgumentsException;

import static common.io.OutputManager.*;




public class Main {
    //public static Logger logger = LogManager.getLogger("logger");
    //static final Logger logger = LogManager.getRootLogger();
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));


        //args = new String[]{"localhost","4445"};
        String addr  = "";
        int port = 0;
        try {
            if (args.length != 2) throw new InvalidProgramArgumentsException("no address passed by arguments");
            addr = args[0];
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                throw new InvalidPortException();
            }
            Client client = new Client(addr,port);
            client.start();
            print("HELL0, MY N4ME IS N0MMY. I'LL HELP Y0U, MY DE4R FRIEND, WITH CRE4TING Y0UR 0WN MUSIC B4NDS. 0KEEEY, LET'S G0...");
        }
        catch (InvalidProgramArgumentsException| ConnectionException e){
            print(e.getMessage());
        }

        //System.out.println(res.getMessage());
    }
}
