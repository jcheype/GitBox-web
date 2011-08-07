package controllers;

import java.util.List;
import java.util.regex.Pattern;

import models.Repository;
import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Application extends Controller {

    public final static Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9_]+$");

    @Check("user")
    public static void index() {
        final User user = Security.currentUser();
        final List<Repository> repositories = Repository.filter("owner", user.username).asList();
        render(user, repositories);
    }

    public static void register() {
    	checkAuthenticity();
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
        index();
    }

}