package models;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Indexed;
import jobs.AuthorizedKeysGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/31/11
 * Time: 7:34 PM
 */

@Embedded
public class Key {
    public String name;
    public String sshkey;

    public final static Pattern keyPattern = Pattern.compile("^(ssh-[a-z]+ +[A-Za-z0-9+/=]+).*$", Pattern.DOTALL | Pattern.MULTILINE);
    public final static AuthorizedKeysGenerator authorizedKeysGenerator = new AuthorizedKeysGenerator();

    public static String extractKey(String key) throws SshKeyException {
        final Matcher matcher = keyPattern.matcher(key);
        if(!matcher.matches()){
            throw new SshKeyException("key is not valid");
        }
        return matcher.group(1);
    }

    public static class SshKeyException extends Exception {
        public SshKeyException(String s) {
            super(s);
        }
    }
}
