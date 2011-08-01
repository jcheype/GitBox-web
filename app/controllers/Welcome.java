package controllers;

import models.Repository;
import models.User;
import play.mvc.Controller;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/31/11
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Welcome extends Controller {
    public final static Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9_]+$");

    public static void index() {
        if(Security.currentUser() != null)
            Home.index();
        render();
    }

    public static void register() {
        final User user = new User();
        user.username = params.get("username");
        if (!usernamePattern.matcher(user.username).matches()) {
            error(500, "username must be [A-Za-z0-9_]");
            return;
        }
        try {
            user.setPassword(params.get("password"));
        } catch (User.UserException e) {
            error(500, e.getMessage());
            return;
        }

        user.save();
        try {
            Repository.create(user.username, user.username);
        } catch (Repository.RepositoryException e) {
            error(500, e.getMessage());
        }
        session.put("username", user.username);
        Home.index();
    }
}
