package common.exceptions;

public class DatabaseException extends CollectionException {
    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException() {
        super("something went wrong with database");
    }
}
