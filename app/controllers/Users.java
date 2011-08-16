package controllers;

import models.User;
import play.mvc.Controller;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 8/15/11
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class Users extends Controller {
    public static void index() { render();}

    public static void changePassword(String password) throws User.UserException {
        final User user = Security.currentUser();
        user.setPassword(password);
        user.save();
        flash("info", "new password saved");
        Home.index();
    }
}
