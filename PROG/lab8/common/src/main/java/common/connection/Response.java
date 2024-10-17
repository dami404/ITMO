package common.connection;

import common.data.Worker;

import java.io.Serializable;
import java.util.Collection;

public interface Response extends Serializable {
    String getMessage();

    Status getStatus();

    /**
     * Status enum:
     * ERROR: Not fatal error that user can fix
     * FINE: All right
     * EXIT: Need to stop program
     */
    enum Status {
        ERROR,
        FINE,
        EXIT,
        AUTH_SUCCESS,
        BROADCAST,
        COLLECTION
    }


    CollectionOperation getCollectionOperation();

    public Collection<Worker> getCollection();
}
