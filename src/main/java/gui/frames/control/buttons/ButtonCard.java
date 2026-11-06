/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import java.util.Optional;

import football.Cards;
import football.Team;
import football.match.Match;
import football.match.MatchMinute;
import football.match.Teams;
import football.person.Player;
import gui.dialogs.DialogCard;
import gui.frames.ControlFrame;
import gui.frames.panes.PaneCard;
import javafx.event.ActionEvent;
import javafx.util.Pair;
import language.Language;
import message.ErrorMsg;
import system.Main;

public class ButtonCard extends ControlButton {
	public ButtonCard(ControlFrame controlFrame) {
		super(Language.getText("karte"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == this) {
			// Taken now: choosing player and card can take a while
			String sMinute = MatchMinute.format(Main.getServicePlaytime().getMessage());
			Optional<Pair<Player, Cards>> result = new DialogCard(Language.getText("karte"), controlFrame.getSettings())
					.showAndWait();
			result.ifPresent(selected -> {
				Cards selectedCard = selected.getValue();
				Player selectedPlayer = selected.getKey();

				if (selectedCard != null && selectedPlayer != null) {
					Match match = Main.getMatch();
					Teams team = match.getlLineup().teamOf(selectedPlayer);
					Team selectedTeam = team == Teams.HOME ? match.gettTeamHome()
							: team == Teams.GUEST ? match.gettTeamGuest() : null;
					controlFrame.changeMainScoreboardPane(new PaneCard(controlFrame.getRootPaneMainScoreboard(),
							selectedPlayer, selectedTeam, selectedCard, sMinute));
				} else {
					ErrorMsg.showErrorMessage(209);
				}
			});
		}
	}
}
