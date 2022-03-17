/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.net.MalformedURLException;
import java.util.List;

import football.person.Coach;
import football.person.Player;
import gui.Components;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import language.Language;
import message.ErrorMsg;
import system.Images;

public class PaneLineupComplete extends PaneMainScoreboard {

	public PaneLineupComplete(Pane rootPane, List<Player> playersToShow, String pathToLogo, Coach coach) {
		super(Images.getImages().loadImageFromResource("background1", "jpg"), rootPane);

		BorderPane paneLineup = new BorderPane();
		paneLineup.prefWidthProperty().bind(this.widthProperty());
		paneLineup.prefHeightProperty().bind(this.heightProperty());
		paneLineup.setPadding(new Insets(I_PADDING));

		VBox lineupBox = new VBox();
		lineupBox.setAlignment(Pos.CENTER_LEFT);
		lineupBox.setSpacing(2);

		VBox coachBox = new VBox();
		coachBox.setAlignment(Pos.CENTER_RIGHT);
		coachBox.setSpacing(2);

		ImageView ivLogo;
		try {
			ivLogo = new ImageView(Images.getImages().loadImageFromPath(pathToLogo));
		} catch (MalformedURLException e) {
			ErrorMsg.showErrorMessage(111);
			ivLogo = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
		}

		ivLogo.setFitHeight(100);
		ivLogo.setPreserveRatio(true);

		for (Player player : playersToShow)
			lineupBox.getChildren().add(getPlayerLine(player));

		if (coach != null) {
			Label lbCoachHeadline = Components.getLabel(Language.getText("trainer"));
			Label lbCoachFirstname = Components.getLabel(coach.getsFirstname());
			Label lbCoachLastname = Components.getLabel(coach.getsLastname());

			coachBox.getChildren().addAll(lbCoachHeadline, lbCoachFirstname, lbCoachLastname);
		}

		BorderPane.setAlignment(ivLogo, Pos.CENTER_LEFT);
		paneLineup.setLeft(ivLogo);
		paneLineup.setCenter(lineupBox);
		paneLineup.setRight(coachBox);

		getChildren().add(paneLineup);
	}

	private Pane getPlayerLine(Player player) {
		GridPane panePlayerLine = new GridPane();
		panePlayerLine.setPadding(new Insets(0, 0, 0, 40));
		panePlayerLine.setMaxWidth(Double.MAX_VALUE);

		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(10);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(90);

		panePlayerLine.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2);

		Label lbNumber = Components.getLabel(player.getiNumber() + "");

		Label lbPlayerName = Components.getLabel(player.getsFirstname() + " " + player.getsLastname());

		panePlayerLine.add(lbNumber, 0, 0);
		panePlayerLine.add(lbPlayerName, 1, 0);

		return panePlayerLine;
	}

}
