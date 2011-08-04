package controllers;

import jobs.AuthorizedKeysGenerator;
import models.Key;
import models.User;
import play.mvc.Controller;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/31/11
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Keys extends Controller {
    private static AuthorizedKeysGenerator authorizedKeysGenerator = new AuthorizedKeysGenerator();

    public static void delete(String uuid) {
        final User user = Security.currentUser();
        user.sshkeys.remove(uuid);
        user.save();
        authorizedKeysGenerator.now();
        Home.index();
    }

    public static void add() {
        Key key = new Key();
        key.name = params.get("keyName");

        String keyValue = params.get("key");
        try {
            key.sshkey = Key.extractKey(keyValue);
        } catch (Key.SshKeyException e) {
            error(500, e.getMessage());
        }

        final User user = Security.currentUser();

        user.sshkeys.put(UUID.randomUUID().toString(), key);
        user.save();
        authorizedKeysGenerator.now();
        Home.index();
    }
}
