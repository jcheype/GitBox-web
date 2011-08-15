package controllers;

import models.Key;
import models.Repository;
import models.StatusJson;
import models.User;
import play.mvc.Controller;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 8/15/11
 * Time: 12:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class Api extends Controller {

    public static void listRepositories(String apikey) {
        final User user = User.find("byApikey", apikey).first();

        final List<Repository> repositories = Repository.find("byOwner", user.username).asList();
        renderJSON(new StatusJson(200, "LIST KEYS", repositories));
    }

    public static void addRepository(String name, String apikey) {
        try {
            final User user = User.find("byApikey", apikey).first();
            Repository.create(name, user.username);

            renderJSON(new StatusJson(200, "CREATED"));
        } catch (Repository.RepositoryException e) {
            renderJSON(new StatusJson(500, e.getMessage()));
        }
    }

    public static void showRepository(String name, String apikey) {
        final User user = User.find("byApikey", apikey).first();
        final Repository repository = Repository.find().filter("name =", name).filter("owner =", user.username).first();
        renderJSON(new StatusJson(200, "REPOSITORY", repository));
    }


    public static void listSshKeys(String apikey) {
        final User user = User.find("byApikey", apikey).first();
        renderJSON(new StatusJson(200, "LIST KEYS", user.sshkeys));
    }

    public static void addSshKey(String name, String key, String apikey) {
        final User user = User.find("byApikey", apikey).first();
        try {
            user.addKey(name, key);
            user.save();
            Key.authorizedKeysGenerator.now();
            renderJSON(new StatusJson(200, "ADDED"));
        } catch (Key.SshKeyException e) {
            renderJSON(new StatusJson(500, e.getMessage()));
        }
    }

    public static void showSshKey(String uuid, String apikey) {
        final User user = User.find("byApikey", apikey).first();
        final Key key = user.sshkeys.get(uuid);
        renderJSON(new StatusJson(200, "KEY", key));
    }

    public static void deleteSshKey(String uuid, String apikey) {
        final User user = User.find("byApikey", apikey).first();
        user.sshkeys.remove(uuid);
        user.save();
        Key.authorizedKeysGenerator.now();
        renderJSON(new StatusJson(200, "DELETED"));
    }
}
