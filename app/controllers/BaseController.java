package controllers;

import models.User;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;



public class BaseController extends Controller {
	
	@Before(unless={"login","register","logout"})
	public static void setup(){
		User user = Security.currentUser();
		Logger.info("user: %s action: %s", user.username, request.action);
	}

}
