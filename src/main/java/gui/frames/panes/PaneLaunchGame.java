/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.net.MalformedURLException;
import java.util.Locale;

import football.match.Match;
import gui.frames.ControlFrame;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import language.Language;
import message.ErrorMsg;
import system.Images;
import system.Main;

public class PaneLaunchGame extends PaneMainScoreboard {

	public PaneLaunchGame(Pane rootPane, ControlFrame controlFrame) {
		super(ScoreboardBackground.crestDiagonal(), rootPane);
		Match match = Main.getMatch();

		GridPane paneMain = new GridPane();
		paneMain.prefWidthProperty().bind(this.widthProperty());
		paneMain.prefHeightProperty().bind(this.heightProperty());
		paneMain.setPadding(new Insets(20));
		paneMain.setMaxWidth(Double.MAX_VALUE);

		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(50);
		bottomPaneCol1.setHalignment(HPos.RIGHT);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(50);
		bottomPaneCol2.setHalignment(HPos.LEFT);
		paneMain.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2);

		ImageView ivLogoHome;
		ImageView ivLogoGuest;
		try {
			ivLogoHome = new ImageView(Images.getImages().loadImageFromPath(match.gettTeamHome().getsPathToLogo()));
			ivLogoGuest = new ImageView(Images.getImages().loadImageFromPath(match.gettTeamGuest().getsPathToLogo()));
		} catch (MalformedURLException e) {
			ErrorMsg.showErrorMessage(111);
			ivLogoHome = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
			ivLogoGuest = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
		}

		ivLogoHome.setFitHeight(175);
		ivLogoHome.setPreserveRatio(true);
		ivLogoGuest.setFitHeight(175);
		ivLogoGuest.setPreserveRatio(true);

		Label lbHeadline = new Label(Language.getText("spiel_gestartet").toUpperCase(Locale.GERMAN));
		lbHeadline.setFont(Main.getMainFont(40));
		lbHeadline.setTextFill(Color.web("#ffffff"));

		paneMain.add(lbHeadline, 0, 0, 2, 1);
		paneMain.add(ivLogoHome, 0, 1);
		paneMain.add(ivLogoGuest, 1, 1);

		getChildren().add(paneMain);
	}
}
