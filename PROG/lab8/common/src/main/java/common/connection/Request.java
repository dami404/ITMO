package common.connection;

import common.auth.User;
import common.data.Worker;

import java.io.Serializable;
import java.net.InetSocketAddress;

public interface Request extends Serializable {
    String getStringArg();

    Worker getWorker();

    String getCommandName();

    User getUser();

    Request setUser(User usr);

    Status getStatus();

    Request setStatus(Status s);

    InetSocketAddress getBroadcastAddress();
    Request setBroadcastAddress(InetSocketAddress address);
    enum Status {
        DEFAULT,
        HELLO,
        CONNECTION_TEST,
        EXIT,
        SENT_FROM_CLIENT,
        RECEIVED_BY_SERVER
    }


}
