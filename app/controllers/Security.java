package controllers;

import models.User;
import org.bson.types.ObjectId;
import play.Logger;
import play.mvc.Scope;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
        
        session.put("userId", user.getId());
        return b;
    }

    public static void onDisconnected(){
        redirect("/");
    }

    public static void login(){

    }

    public static User currentUser(){
        return User.find("byUsername", connected()).first();
    }
}