/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.control.buttons;

import java.util.Optional;

import football.Cards;
import football.person.Player;
import gui.dialogs.DialogCard;
import gui.frames.ControlFrame;
import gui.frames.panes.PanePlayer;
import gui.frames.panes.PanePersonIcons;
import javafx.event.ActionEvent;
import javafx.util.Pair;
import language.Language;
import message.ErrorMsg;

public class ButtonCard extends ControlButton {
	public ButtonCard(ControlFrame controlFrame) {
		super(Language.getText("karte"), controlFrame);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == this) {
			Optional<Pair<Player, Cards>> result = new DialogCard(Language.getText("karte"), controlFrame.getSettings())
					.showAndWait();
			result.ifPresent(selected -> {
				Cards selectedCard = selected.getValue();
				Player selectedPlayer = selected.getKey();

				if (selectedCard != null && selectedPlayer != null) {
					PanePersonIcons icon = PanePersonIcons.NONE;
					switch(selectedCard) {
					case YELLOW:
						icon = PanePersonIcons.YELLOW_CARD;
						break;
					case RED:
						icon = PanePersonIcons.RED_CARD;
						break;
					case YELLOW_RED:
						icon = PanePersonIcons.YELLOW_RED_CARD;
						break;
					}
					controlFrame.changeMainScoreboardPane(new PanePlayer(controlFrame.getRootPaneMainScoreboard(), icon, selectedPlayer));
				} else {
					ErrorMsg.showErrorMessage(209);
				}
			});
		}
	}
}
