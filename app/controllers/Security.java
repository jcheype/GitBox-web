package controllers;

import models.User;
import play.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/31/11
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Security extends Secure.Security {

	static boolean authenticate(String username, String password) {
        final User user = User.find("byUsername", username).first();
        if(user == null){
            return false;
        }

        boolean b = false;
        try {
            b = user.checkPassword(password);
        } catch (Exception e) {
            Logger.error(e,"error while check password");
        }
        return b;
    }

    static void onDisconnected(){
        session.remove("username");
    }

    public static User currentUser(){
        return User.find("byUsername", connected()).first();
    }
}
