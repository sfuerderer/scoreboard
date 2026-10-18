/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package background.services;

import background.tasks.TaskPlaytime;
import football.match.Match;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServicePlaytime extends Service<Void> {
    private final Match match;
    private final Runnable onHalftimeEnded;

    public ServicePlaytime(Match match, Runnable onHalftimeEnded) {
        this.match = match;
        this.onHalftimeEnded = onHalftimeEnded;
    }

    @Override
    protected Task<Void> createTask() {
        return new TaskPlaytime(match, onHalftimeEnded);
    }
}
