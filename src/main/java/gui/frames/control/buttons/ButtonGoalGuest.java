/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import java.util.Optional;

import football.match.Teams;
import football.person.Player;
import gui.frames.ControlFrame;
import gui.frames.panes.PanePlayer;
import gui.frames.panes.PanePersonIcons;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceDialog;
import language.Language;
import system.Main;

public class ButtonGoalGuest extends ControlButton{
	public ButtonGoalGuest(ControlFrame controlFrame) {
		super(Language.getText("tor_gast"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == this) {
			Main.getMatch().addGoal(Teams.GUEST);
			if (Main.getMatch().getlLineup().getlGuestPlayersOnField().size() > 0) {
				ChoiceDialog<Player> dialog = new ChoiceDialog<Player>(
						Main.getMatch().getlLineup().getlGuestPlayersOnField().get(0),
						Main.getMatch().getlLineup().getlGuestPlayersOnField());

				dialog.setTitle(Language.getText("tor_gast"));
				dialog.setHeaderText(Language.getText("waehle_torschuetze"));
				dialog.setContentText(Language.getText("spieler"));

				Optional<Player> result = dialog.showAndWait();

				result.ifPresent(player -> {
					controlFrame.changeMainScoreboardPane(
							new PanePlayer(controlFrame.getRootPaneMainScoreboard(), PanePersonIcons.BALL, player));
				});
			}
		}
	}
}
