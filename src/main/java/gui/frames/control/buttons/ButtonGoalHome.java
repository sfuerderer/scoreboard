/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import football.match.MatchMinute;
import football.match.Teams;
import football.person.Player;
import gui.frames.ControlFrame;
import gui.frames.panes.PaneGoal;
import gui.frames.panes.PaneVideo;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceDialog;
import language.Language;
import system.Main;
import system.Medias;

import java.util.Optional;

public class ButtonGoalHome extends ControlButton {
    public ButtonGoalHome(ControlFrame controlFrame) {
        super(Language.getText("tor_heim"), controlFrame);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this) {
            // Taken now: choosing the scorer can take a while
            String sClock = Main.getServicePlaytime().getMessage();
            String sMinute = MatchMinute.format(sClock);
            Main.getMatch().addGoal(Teams.HOME, MatchMinute.of(sClock));
            if (Main.getMatch().getlLineup().getlHomePlayersOnField().size() > 0) {
                controlFrame.changeMainScoreboardPane(new PaneVideo(controlFrame.getRootPaneMainScoreboard(),
                        Medias.loadMediaFromResource(controlFrame.getSettings().sGoalAnimation.getFilename(), "mp4", "videos"), false));
                ChoiceDialog<Player> dialog = new ChoiceDialog<Player>(
                        Main.getMatch().getlLineup().getlHomePlayersOnField().get(0),
                        Main.getMatch().getlLineup().getlHomePlayersOnField());

                dialog.setTitle(Language.getText("tor_heim"));
                dialog.setHeaderText(Language.getText("waehle_torschuetze"));
                dialog.setContentText(Language.getText("spieler"));

                Optional<Player> result = dialog.showAndWait();

                result.ifPresent(player -> {
                    Main.getMatch().setScorerOfLastGoal(Teams.HOME, player);
                    controlFrame.changeMainScoreboardPane(
                            new PaneGoal(controlFrame.getRootPaneMainScoreboard(), player, sMinute));
                });
            }
        }
    }
}
