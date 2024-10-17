package commands;

import auth.UserManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.Response;
import common.exceptions.DatabaseException;

public class RegisterCommand extends CommandImpl {
    private final UserManager userManager;

    public RegisterCommand(UserManager manager) {
        super("register", CommandType.AUTH);
        userManager = manager;
    }

    @Override
    public Response run() throws DatabaseException {

        User user = getArgument().getUser();
        if (user != null && user.getLogin() != null && user.getPassword() != null) {
            if (userManager.isPresent(user.getLogin())) {
                throw new DatabaseException("user " + user.getLogin() + " already exist");
            }
            userManager.add(user);
            return new AnswerMsg().info("user " + user.getLogin() + " successfully registered").setStatus(Response.Status.AUTH_SUCCESS);
        }
        throw new DatabaseException("something wrong with user");

    }
}
