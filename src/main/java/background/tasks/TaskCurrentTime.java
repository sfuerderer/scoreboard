/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package background.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.concurrent.Task;
import message.ErrorMsg;

/**
 * A class for displaying the current time.
 *
 * @author Sebastian Fürderer
 * @version 1.0.0
 * @date 25.06.2020
 *
 */
public class TaskCurrentTime extends Task<Void>{
	private final int I_TIME_TO_SLEEP_MS = 5000;
	private SimpleDateFormat dateFormatter;
	
	public TaskCurrentTime() {
		dateFormatter = new SimpleDateFormat("HH:mm");  
	}
	
	@Override public Void call() {
        while(true) {
        	Date date = new Date();
			updateMessage(dateFormatter.format(date));
			try {
				Thread.sleep(I_TIME_TO_SLEEP_MS);
			} catch (InterruptedException e) {
				 ErrorMsg.showErrorMessage(113,e.getMessage());
			}
        }
    }
}
