package commands;

import auth.UserManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.Response;
import common.exceptions.AuthException;

public class LoginCommand extends CommandImpl {
    private final UserManager userManager;

    public LoginCommand(UserManager manager) {
        super("login", CommandType.AUTH);
        userManager = manager;
    }

    @Override
    public Response run() throws AuthException {

        User user = getArgument().getUser();
        if (user != null && user.getLogin() != null && user.getPassword() != null) {
            if (userManager.isValid(user)) {
                return new AnswerMsg().info("login successful").setStatus(Response.Status.AUTH_SUCCESS);
            }
        }
        throw new AuthException();

    }
}
