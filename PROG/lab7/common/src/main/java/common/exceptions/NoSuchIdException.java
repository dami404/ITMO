package common.exceptions;

public class NoSuchIdException extends CollectionException {
    public NoSuchIdException(Integer id) {
        super("element #" + id + " not found");
    }
}
