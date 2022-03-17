package system;

public class Logger {
	private static Logger logger;

	private Logger() {

	}

	public static Logger getLoad() {
		if (logger == null)
			logger = new Logger();
		return logger;
	}
	
	public void logMessage(String Message, LogTypes logType) {
		
	}
}
