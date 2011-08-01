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
    static Pattern keyPattern = Pattern.compile("(ssh-rsa +[A-Za-z0-9+/=]+).*");

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
        final Matcher matcher = keyPattern.matcher(keyValue);
        if (!matcher.matches())
            error(500, "key is not valid");
        key.sshkey = matcher.group(1);

        final User user = Security.currentUser();

        user.sshkeys.put(UUID.randomUUID().toString(), key);
        user.save();
        authorizedKeysGenerator.now();
        Home.index();
    }
}
