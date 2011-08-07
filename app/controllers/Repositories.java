package controllers;

import models.Repository;
import models.User;
import org.eclipse.jgit.api.Git;
import play.Logger;
import play.Play;
import play.mvc.Controller;

import javax.persistence.Access;
import java.io.File;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/30/11
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Repositories extends Controller {

    public static void add(String name) {
        try {
            Repository.create(name, Security.connected());
        } catch (Repository.RepositoryException e) {
            error(500, e.getMessage());
        }

        Application.index();
    }

    public static void access(String name) {
        try {
            final Repository repository = Repository.find("byName", name).first();
            render(repository);
        } catch (IndexOutOfBoundsException e) {
            error(501, "user doesn't exist");
        }
    }

    public static void accessDelete(String name, String type, String username) {
        final Repository repository = Repository.find("byName", name).first();
        if (!repository.owner.equals(Security.connected())) {
            error(500, "your not the owner");
        }

        if ("read".equals(type)) {
            repository.readUsers.remove(username);
        } else if ("write".equals(type)) {
            repository.writeUsers.remove(username);
        } else error(500, "must be read or write");

        repository.save();
        access(name);
    }

    public static void accessAdd(String name) {
        String username = params.get("username");
        String type = params.get("type");

        if (!Application.usernamePattern.matcher(username).matches()) {
            error(500, "username must be [A-Za-z0-9_]");
            return;
        }
        if(User.find("byUsername", username).count()  != 1){
            error(500, "cannot find user");
        }


        final Repository repository = Repository.find("byName", name).first();
        if (!repository.owner.equals(Security.connected())) {
            error(500, "your not the owner");
        }

        if ("read".equals(type)) {
            repository.readUsers.add(username);
        } else if ("write".equals(type)) {
            repository.writeUsers.add(username);
        } else error(500, "must be read or write");

        repository.save();
        access(name);
    }
}
