package models;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Indexed;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/31/11
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */

@Embedded
public class Key {
    public String name;
    public String sshkey;
}
