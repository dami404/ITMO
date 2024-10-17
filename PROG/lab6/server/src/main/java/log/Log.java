package log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * class with static methods for printing
 */
public class Log {
    public final static Logger logger = LogManager.getRootLogger();
}
