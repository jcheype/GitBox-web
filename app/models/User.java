package models;

import java.util.HashMap;
import java.util.Map;

import play.data.validation.Password;
import play.data.validation.Required;
import play.libs.Codec;
import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;

/**
 * Created by IntelliJ IDEA. User: mush Date: 7/31/11 Time: 12:13 AM
 */

@Entity
public class User extends Model {

	@Required
	@Indexed(unique = true)
	public String username;

	@Required
	@Password
	private String password;

	public final Map<String, Key> sshkeys = new HashMap<String, Key>();

	public boolean checkPassword(String password) {
		return (this.password.equals(Codec.hexSHA1(password)));
	}

	public void setPassword(String password) throws UserException {
		if (username == null) {
			throw new UserException("set username first");
		}
		if (password == null) {
			throw new UserException("password cannot be null");
		}
		try {
			this.password = Codec.hexSHA1(password);
		} catch (Exception e) {
			throw new UserException("cannot set password", e);
		}
	}

	public static class UserException extends Exception {
		public UserException(String s) {
			super(s); // To change body of overridden methods use File |
						// Settings | File Templates.
		}

		public UserException(String s, Throwable throwable) {
			super(s, throwable); // To change body of overridden methods use
									// File | Settings | File Templates.
		}
	}
}
