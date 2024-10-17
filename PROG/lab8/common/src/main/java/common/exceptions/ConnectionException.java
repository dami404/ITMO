package common.exceptions;

/**
 * base class for all connection exceptions caused by connection problems
 */
public class ConnectionException extends Exception {
    public ConnectionException(String s) {
        super("[ConnectionException] " + s);
    }
    public ConnectionException(){
        super("[ConnectionException] something wrong with connection");
    }
    public ConnectionException(String t, String msg){
        super(t+" "+msg);
    }
}
