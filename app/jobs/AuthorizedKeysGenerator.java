package jobs;

import com.google.code.morphia.Datastore;
import models.Key;
import models.User;
import play.Logger;
import play.jobs.Job;
import play.modules.morphia.MorphiaPlugin;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: 7/31/11
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthorizedKeysGenerator extends Job {
    private final static File sshFolder = new File(System.getProperty("user.home"), ".ssh");
    private static final String HEADER = "# gitbox";

    private boolean checkAuthorizedKeys(){
        File authFile = new File(sshFolder, "authorized_keys");
        if(!authFile.exists()){
            return true;
        }
        BufferedReader reader = null;
        boolean result = false;
        try {
            reader = new BufferedReader(new FileReader(authFile));
            result = reader.readLine().startsWith(HEADER);
        } catch (Exception e) {
            Logger.error(e,"error while checkAuthorizedKeys");
        } finally {
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }
        return result;
    }

    @Override
    synchronized public void doJob() throws Exception {
        if(!checkAuthorizedKeys()){
            Logger.warn("cannot update authorized_keys, it will be overwritten");
            return;
        }

        File tempFile = File.createTempFile("gitbox", null);
        BufferedWriter bufferedWriter = null;
        boolean success = false;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
            bufferedWriter.append(HEADER+"\n");

            Datastore ds = MorphiaPlugin.ds();
            for (Iterator<User> it = ds.find(User.class).iterator(); it.hasNext(); ) {
                User user = it.next();
                String command = "command=\"/home/git/gitaccess.rb " + user.username + "\",no-port-forwarding,no-X11-forwarding,no-agent-forwarding,no-pty";
                for (Map.Entry<String, Key> entry : user.sshkeys.entrySet()) {
                    bufferedWriter.append(command);
                    bufferedWriter.append(' ');
                    bufferedWriter.append(entry.getValue().sshkey);
                    bufferedWriter.append(' ');
                    bufferedWriter.append(entry.getKey());
                    bufferedWriter.append('\n');
                }
            }
            success=true;
        }
        finally {
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
        }
        if(success)
            tempFile.renameTo(new File(sshFolder, "authorized_keys"));
    }
}
