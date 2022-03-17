/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.lineupFrame;

import java.util.List;

import football.match.Teams;
import football.person.Player;
import gui.frames.ControlFrame;
import gui.frames.panes.PanePlayer;
import gui.frames.panes.PanePersonIcons;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import language.Language;
import message.ErrorMsg;
import system.Main;

public class ShowLineupFrameTeam extends GridPane {
	private ControlFrame controlFrame;

	public ShowLineupFrameTeam(Teams selectedTeam, ControlFrame controlFrame) {
		if (Main.getMatch() == null) {
			ErrorMsg.showErrorMessage(206);
			return;
		}

		this.controlFrame = controlFrame;

		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(25);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(25);
		ColumnConstraints bottomPaneCol3 = new ColumnConstraints();
		bottomPaneCol3.setPercentWidth(25);
		ColumnConstraints bottomPaneCol4 = new ColumnConstraints();
		bottomPaneCol4.setPercentWidth(25);
		getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2, bottomPaneCol3, bottomPaneCol4);

		ScrollPane spAllPlayers = new ScrollPane();
		spAllPlayers.setHbarPolicy(ScrollBarPolicy.NEVER);
		spAllPlayers.setFitToWidth(true);
		spAllPlayers.setFitToHeight(true);
		spAllPlayers.setStyle("-fx-background-color:transparent;");
		spAllPlayers.setContent(buildAllPlayers(selectedTeam));

		add(spAllPlayers, 0, 0, 4, 1);
	}

	private VBox buildAllPlayers(Teams selectedTeam) {
		VBox allPlayer = new VBox();
		List<Player> lineup;
		switch (selectedTeam) {
		case HOME:
			lineup = Main.getMatch().getlLineup().getlHomePlayersOnField();
			break;
		case GUEST:
			lineup = Main.getMatch().getlLineup().getlGuestPlayersOnField();
			break;
		default:
			return allPlayer;
		}

		// BUG Check if it is an Bug here also. Wenn Spieler hinzugefügt wird dann wird
		// die Mannschaft hier nicht korrekt gelistet. Erst nach einem Restart.
		for (Player player : lineup) {
			GridPane gpPlayerLine = new GridPane();
			gpPlayerLine.setPadding(new Insets(5));
			gpPlayerLine.setHgap(5);
			gpPlayerLine.setVgap(10);

			ColumnConstraints gpPlayerLineCol1 = new ColumnConstraints();
			gpPlayerLineCol1.setPercentWidth(50);
			ColumnConstraints gpPlayerLineCol2 = new ColumnConstraints();
			gpPlayerLineCol2.setPercentWidth(50);

			gpPlayerLine.getColumnConstraints().addAll(gpPlayerLineCol1, gpPlayerLineCol2);

			Label lbPlayerName = new Label(player.getiNumber()+" - "+player.getsFirstname() + " " + player.getsLastname());

			Button btnShow = new Button(Language.getText("anzeigen"));
			btnShow.setMaxWidth(Double.MAX_VALUE);
			btnShow.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					controlFrame.changeMainScoreboardPane(new PanePlayer(controlFrame.getRootPaneMainScoreboard(), PanePersonIcons.NONE, player));
				}
			});

			gpPlayerLine.add(lbPlayerName, 0, 0);
			gpPlayerLine.add(btnShow, 1, 0);

			allPlayer.getChildren().add(gpPlayerLine);
		}

		return allPlayer;
	}
}
