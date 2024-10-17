package database;

import auth.UserManager;
import common.auth.User;
import common.exceptions.DatabaseException;
import log.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * A manager of user database.
 */
public class UserDatabaseManager implements UserManager {

    private final DatabaseHandler databaseHandler;


    public UserDatabaseManager(DatabaseHandler handler) throws DatabaseException {
        databaseHandler = handler;
        create();
    }

    private void create() throws DatabaseException {
        //language=SQL
        String createTableSQL = "CREATE TABLE IF NOT EXISTS USERS" +
                "(login TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL);";

        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        try (PreparedStatement statement = databaseHandler.getPreparedStatement(createTableSQL)) {
            statement.execute();
            databaseHandler.commit();
        } catch (SQLException e) {
            databaseHandler.rollback();
            throw new DatabaseException("cannot create user database");
        } finally {
            databaseHandler.setNormalMode();
        }
    }


    public void add(User user) throws DatabaseException {
        String sql = "INSERT INTO USERS (login, password) VALUES (?, ?)";

        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        try (PreparedStatement preparedStatement = databaseHandler.getPreparedStatement(sql)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.execute();
            databaseHandler.commit();
        } catch (SQLException e) {
            databaseHandler.rollback();
            throw new DatabaseException("something went wrong during adding new user");
        } finally {
            databaseHandler.setNormalMode();
        }
    }


    public boolean isValid(User user) {
        try {
            String password = user.getPassword();
            ResultSet rs = databaseHandler.getStatement().executeQuery("SELECT * FROM USERS WHERE login = '" + user.getLogin() + "'");
            while (rs.next())
                if (password.equals(rs.getString(2)))
                    return true;
            return false;
        } catch (SQLException e) {
            Log.logger.error("Can't get user from database.");
            return false;
        }
    }


    public boolean isPresent(String username) {
        try {
            ResultSet rs = databaseHandler.getStatement().executeQuery("SELECT * FROM USERS WHERE login = '" + username + "'");
            return rs.next();
        } catch (SQLException e) {
            Log.logger.error("Can't get user from database.");
            return false;
        }
    }

    public List<User> getUsers() {
        List<User> users = new LinkedList<>();
        try (PreparedStatement statement = databaseHandler.getPreparedStatement("SELECT * FROM USERS")) {
            ResultSet resultSet = statement.executeQuery();
            try {
                while (resultSet.next()) {
                    User user = new User(resultSet.getString("login"));
                    user.setPassword(resultSet.getString("password"));
                    users.add(user);
                }
            } catch (SQLException ignored) {

            }

        } catch (SQLException | DatabaseException e) {
            if (users.isEmpty()) throw new DatabaseException("no registered users found");
        }
        return users;
    }

}