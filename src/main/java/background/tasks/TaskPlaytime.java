/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package background.tasks;

import football.match.Match;
import javafx.concurrent.Task;
import message.ErrorMsg;
import system.Main;

/**
 * A class for displaying the current playtime.
 *
 * @author Sebastian Fürderer
 * @version 1.0.0
 */
public class TaskPlaytime extends Task<Void> {
//    private final ControlFrame controlFrame;

//    public TaskPlaytime(ControlFrame controlFrame) {
//        this.controlFrame = controlFrame;
//    }

    @Override
    public Void call() {
        Match match = Main.getMatch();
        while (true) {
            if (match.isbPlaytimeRunning()) {
                long currentMilliseconds = System.currentTimeMillis();
                long diff = currentMilliseconds - match.getlStartTimeMillis();
                diff = diff / 1000; // diff in seconds

                if (diff >= match.getiHalftimeLength() * 60L) {
                    match.stop();
                    match.nextHalf();
//                    Platform.runLater(() -> controlFrame.changeState(new ControlFrameStateMatch()));
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
                ErrorMsg.showErrorMessage(113, e.getMessage());
            }
        }
    }
}
