package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import org.bson.types.ObjectId;
import org.eclipse.jgit.api.Git;
import play.Play;
import play.data.validation.Required;
import play.modules.morphia.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/30/11
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Repository extends Model {
    private final static String BASE_DIR = Play.configuration.getProperty("gitbox.repo", System.getProperty("user.home") + "/repo");
    public final static Pattern repositoryPattern = Pattern.compile("^[A-Za-z0-9_]+$");

    @Required
    @Indexed(unique = true)
    public String name;

    @Required
    @Indexed
    public String owner;

    @Indexed
    public final List<String> writeUsers = new ArrayList<String>();

    @Indexed
    public final List<String> readUsers = new ArrayList<String>();

    public static Repository create(String name, String owner) throws RepositoryException {
        if (!repositoryPattern.matcher(name).matches()) {
            throw new RepositoryException("repository name not allowed");
        }
        if (User.find("byUsername", owner).count() != 1) {
            throw new RepositoryException("user doesn't exist");
        }

        File repoDir = new File(BASE_DIR, name + ".git");
        if (repoDir.exists()) {
            throw new RepositoryException("repository exists");
        }
        Git.init().setBare(true).setDirectory(repoDir).call();

        final Repository repository = new Repository();
        repository.name = name;
        repository.owner = owner;
        repository.writeUsers.add(owner);
        repository.save();
        return repository;
    }

    public static class RepositoryException extends Exception {
        public RepositoryException() {
            super();    //To change body of overridden methods use File | Settings | File Templates.
        }

        public RepositoryException(String s) {
            super(s);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public RepositoryException(String s, Throwable throwable) {
            super(s, throwable);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public RepositoryException(Throwable throwable) {
            super(throwable);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

}
