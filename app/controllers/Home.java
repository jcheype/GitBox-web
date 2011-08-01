package controllers;

import jobs.AuthorizedKeysGenerator;
import models.Repository;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        render(user, repositories);
    }
}
