/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.net.MalformedURLException;

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

public class PaneCorners extends PaneMainScoreboard {

	public PaneCorners(Pane rootPane) {
		super(Images.getImages().loadImageFromResource("background1", "jpg"), rootPane);

		GridPane paneMain = new GridPane();
		paneMain.prefWidthProperty().bind(this.widthProperty());
		paneMain.prefHeightProperty().bind(this.heightProperty());
		paneMain.setPadding(new Insets(20));
		paneMain.setMaxWidth(Double.MAX_VALUE);

		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(25);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(50);
		bottomPaneCol2.setHalignment(HPos.CENTER);
		ColumnConstraints bottomPaneCol3 = new ColumnConstraints();
		bottomPaneCol3.setPercentWidth(25);
		bottomPaneCol3.setHalignment(HPos.RIGHT);
		paneMain.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2, bottomPaneCol3);

		ImageView ivLogoHome;
		ImageView ivLogoGuest;
		try {
			ivLogoHome = new ImageView(
					Images.getImages().loadImageFromPath(Main.getMatch().gettTeamHome().getsPathToLogo()));
			ivLogoGuest = new ImageView(
					Images.getImages().loadImageFromPath(Main.getMatch().gettTeamGuest().getsPathToLogo()));
		} catch (MalformedURLException e) {
			ErrorMsg.showErrorMessage(111);
			ivLogoHome = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
			ivLogoGuest = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
		}

		ivLogoHome.setFitHeight(100);
		ivLogoHome.setPreserveRatio(true);
		ivLogoGuest.setFitHeight(100);
		ivLogoGuest.setPreserveRatio(true);

		Label lbTitle = new Label(Language.getText("ecken"));
		lbTitle.setFont(Main.getMainFont());
		lbTitle.setTextFill(Color.web("#ffffff"));

		Label lbCorners = new Label("0:0");
		lbCorners.setFont(Main.getMainFont(150));
		lbCorners.setTextFill(Color.web("#ffffff"));
		GridPane.setHalignment(lbCorners, HPos.CENTER);
		lbCorners.textProperty().bind(Main.getMatch().sCorners);

		paneMain.add(ivLogoHome, 0, 0);
		paneMain.add(lbTitle, 1, 0);
		paneMain.add(ivLogoGuest, 2, 0);
		paneMain.add(lbCorners, 0, 1, 3, 1);

		getChildren().add(paneMain);
	}
}
