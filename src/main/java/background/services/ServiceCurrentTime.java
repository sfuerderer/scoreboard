/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package background.services;

import background.tasks.TaskCurrentTime;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServiceCurrentTime extends Service<Void> {
    @Override
    protected Task<Void> createTask() {
        TaskCurrentTime taskCurrentTime = new TaskCurrentTime();
        return taskCurrentTime;
    }
}