package common.auth;

import common.crypto.SHA384Generator;
import common.exceptions.EncryptionException;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 8347617547303456361L;

    private final String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        try {
            this.password = SHA384Generator.hash(password);
        } catch (EncryptionException e) {
            this.password = null;
        }
    }

    public User(String username) {
        this.username = username;
    }

    public String getLogin() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return getLogin();
    }
}