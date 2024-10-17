package common.exceptions;

public class AuthException extends ConnectionException {

    public AuthException() {
        super("[AuthException]", "no such user, or user login or password is incorrect");
    }
    public AuthException(String t, String s){
        super(t,s);
    }
}
