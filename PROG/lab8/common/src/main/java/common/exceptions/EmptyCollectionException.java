package common.exceptions;

/**
 * thrown when collection is empty
 */
public class EmptyCollectionException extends CollectionException {
    public EmptyCollectionException() {
        super("[EmptyCollectionException] collection is empty");
    }
}
