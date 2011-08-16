package controllers;

import models.User;
import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Util;



public class BaseController extends Controller {
	
	@Before(unless={"Application.register","Application.createUser","Secure.login", "Secure.authenticate"})
	public static void check(){
		User user = Security.currentUser();
		if(user == null){
			Logger.info("user == null...Redirecting to login page");
			try {
				Secure.login();
			} catch (Throwable e) {
				error(500, e.getMessage());
			}
		}else{
			Logger.info("user: %s action: %s", user.username, request.action);
		}
	}
	
	@Before
	public static void setup(){
		String gituser = Play.configuration.getProperty("gitbox.user");
		String giturl =  Play.configuration.getProperty("gitbox.url");
		renderArgs.put("gituser", gituser);
		renderArgs.put("giturl", giturl);
	}

}
