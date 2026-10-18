/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package background.tasks;

import football.match.Match;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * A class for displaying the current playtime.
 *
 * @author Sebastian Fürderer
 * @version 1.0.0
 */
public class TaskPlaytime extends Task<Void> {
    private final Match match;
    private final Runnable onHalftimeEnded;

    /**
     * @param match           the match whose playtime is displayed
     * @param onHalftimeEnded called on the JavaFX thread when a halftime ended, may be null
     */
    public TaskPlaytime(Match match, Runnable onHalftimeEnded) {
        this.match = match;
        this.onHalftimeEnded = onHalftimeEnded;
    }

    @Override
    public Void call() {
        while (!isCancelled()) {
            if (match.isbPlaytimeRunning()) {
                long currentMilliseconds = System.currentTimeMillis();
                long diff = currentMilliseconds - match.getlStartTimeMillis();
                diff = diff / 1000; // diff in seconds

                if (diff >= match.getiHalftimeLength() * 60L) {
                    match.stop();
                    match.nextHalf();
                    if (onHalftimeEnded != null)
                        Platform.runLater(onHalftimeEnded);
                } else {
                    int minute = (int) (diff / 60
                            + (match.getTimePassedToHalftime()));
                    int sekunden = (int) (diff % 60);
                    updateMessage(String.format("%02d:%02d", minute, sekunden));
                }
            } else {
                updateMessage(String.format("%02d:00",
                        (match.getTimePassedToHalftime())));
            }
            try {
                Thread.sleep(99);
            } catch (InterruptedException e) {
                // cancel() interrupts the sleep; the loop condition ends the task
                if (isCancelled())
                    break;
            }
        }
        return null;
    }
}
