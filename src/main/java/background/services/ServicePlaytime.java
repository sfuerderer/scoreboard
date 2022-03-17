/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package background.services;

import background.tasks.TaskPlaytime;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServicePlaytime extends Service<Void> {
//    private final ControlFrame controlFrame;
//
//	public ServicePlaytime(ControlFrame controlFrame) {
//		this.controlFrame = controlFrame;
//	}

    @Override
    protected Task<Void> createTask() {
        return new TaskPlaytime();
    }
}