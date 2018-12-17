package helpers;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {

    private Logger logger;
    private String classname;

    public LogHelper(String classname) {
        logger = Logger.getLogger(this.classname = classname);
    }

    public void printLogInfo(String message, String method) {
        logger.logp(Level.INFO, classname, method, message);
    }

    public void printLogWarning(String message, String method) {
        logger.logp(Level.WARNING, classname, method, message);
    }
}
