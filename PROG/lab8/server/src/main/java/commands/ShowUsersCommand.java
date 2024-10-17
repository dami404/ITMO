package commands;

import auth.UserManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.AuthException;

public class ShowUsersCommand extends CommandImpl {
    private final UserManager userManager;

    public ShowUsersCommand(UserManager userManager) {
        super("show_users", CommandType.SERVER_ONLY);
        this.userManager = userManager;
    }

    @Override
    public String execute() throws AuthException {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        userManager.getUsers().forEach(user -> builder.append(user).append("\n"));

        return builder.toString();
    }
}
