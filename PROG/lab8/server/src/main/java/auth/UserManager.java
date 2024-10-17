package auth;

import common.auth.User;
import common.exceptions.DatabaseException;

import java.util.List;

public interface UserManager{
    public void add(User user) throws DatabaseException;

    public boolean isValid(User user);

    public boolean isPresent(String username);

    public List<User> getUsers();
}