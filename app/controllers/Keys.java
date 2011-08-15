package controllers;

import models.Key;
import models.User;
import play.mvc.Controller;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/31/11
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Keys extends Controller {

    public static void delete(String uuid) {
        final User user = Security.currentUser();
        user.sshkeys.remove(uuid);
        user.save();
        Key.authorizedKeysGenerator.now();
        Home.index();
    }

    public static void add() {
        final User user = Security.currentUser();
        try {
            user.addKey(params.get("keyName"), params.get("key"));
            user.save();
            Key.authorizedKeysGenerator.now();
            Home.index();
        } catch (Key.SshKeyException e) {
            error(500, e.getMessage());
        }
    }
}
