package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import play.data.validation.Required;
import play.modules.morphia.Model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA. User: mush Date: 7/31/11 Time: 12:13 AM
 */

@Entity
public class User extends Model {

    @Required
    @Indexed(unique = true)
    public String username;

    @Required
    private byte[] passwordHashed;

    public final Map<String, Key> sshkeys = new HashMap<String, Key>();

    @Indexed(unique = true)
    public String apikey;


    public static byte[] makeHash(String username, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(username.getBytes("UTF-8"));
        digest.update(password.getBytes("UTF-8"));
        return digest.digest();
    }

    public boolean checkPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return Arrays.equals(makeHash(username, password), passwordHashed);
    }

    public void setPassword(String password) throws UserException {
        if (username == null) {
            throw new UserException("set username first");
        }
        if (password == null) {
            throw new UserException("password cannot be null");
        }
        if(password.isEmpty()){
            throw new UserException("Password length must be > 3");
        }

        try {
            this.passwordHashed = makeHash(username, password);
            apikey = UUID.randomUUID().toString();
        } catch (Exception e) {
            throw new UserException("cannot set password", e);
        }
    }

    public void addKey(String name, String keyString) throws Key.SshKeyException {
        Key key = new Key();
        key.name = name;
        key.sshkey = Key.extractKey(keyString);
        sshkeys.put(UUID.randomUUID().toString(), key);
    }

    public static class UserException extends Exception {
        public UserException(String s) {
            super(s);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public UserException(String s, Throwable throwable) {
            super(s, throwable);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

}
