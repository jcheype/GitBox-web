package controllers;

import java.util.List;
import java.util.regex.Pattern;

import models.Repository;
import models.User;
import play.data.validation.Required;

public class Application extends BaseController {

	public final static Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9_]+$");

	public static void index() {
		final User user = Security.currentUser();
		final List<Repository> repositories = Repository.filter("owner", user.username).asList();
		render(user, repositories);
	}

	public static void register() {
		render();
	}

	public static void createUser(@Required String username, @Required String password) {
		checkAuthenticity();
		final User user = new User();
		user.username = username;
		if (!usernamePattern.matcher(user.username).matches()) {
			error(500, "username must be [A-Za-z0-9_]");
		}

		try {
			user.setPassword(password);
		} catch (User.UserException e) {
			error(500, e.getMessage());
		}
		try {
			user.save();
		} catch (Exception e) {
			error(500, e.getMessage());
		}
		try {
			Repository.create(user.username, user.username);
		} catch (Repository.RepositoryException e) {
			error(500, e.getMessage());
		}
		session.put("username", user.username);
		index();
	}

}