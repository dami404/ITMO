package common.exceptions;

public class NoSuchIdException extends CollectionException {
    public NoSuchIdException(Integer id) {
        super("[NoSuchIdException] element [" + id + "] not found");
    }
    public NoSuchIdException(String id) {
        super("[NoSuchIdException] element [" + id + "] not found");
    }
}
