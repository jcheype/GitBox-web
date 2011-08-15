package models;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 8/15/11
 * Time: 12:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class StatusJson {
    public final int code;
    public final String message;
    public final Object object;

    public StatusJson(int code, String message) {
        this.code = code;
        this.message = message;
        this.object = null;
    }

    public StatusJson(int code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }
}
