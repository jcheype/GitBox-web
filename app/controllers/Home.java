package controllers;

import models.Repository;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.With;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/31/11
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */

@With(Secure.class)
public class Home extends Controller {

    public static void index() {
        final User user = Security.currentUser();
        final List<Repository> repositories = Repository.filter("owner", user.username).asList();
        String domain = Http.Request.current().domain;
        render(user, repositories, domain);
    }
}
