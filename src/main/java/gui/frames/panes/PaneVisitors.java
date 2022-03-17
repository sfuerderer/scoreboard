/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import system.Images;
import system.Main;

public class PaneVisitors extends PaneMainScoreboard {

	public PaneVisitors(Pane rootPane) {
		super(Images.getImages().loadImageFromResource("background1", "jpg"), rootPane);

		BorderPane paneMain = new BorderPane();
		paneMain.prefWidthProperty().bind(this.widthProperty());
		paneMain.prefHeightProperty().bind(this.heightProperty());

		VBox content = new VBox();
		content.setAlignment(Pos.CENTER);
		content.setSpacing(10);

		ImageView ivVisitors = new ImageView(Images.getImages().loadImageFromResource("Zuschauer2", "png"));
		ivVisitors.setFitHeight(150);
		ivVisitors.setPreserveRatio(true);

		Label lbVisitors = new Label(Main.getMatch().getiVisitors() + "");
		lbVisitors.setFont(Main.getMainFont(100));
		lbVisitors.setTextFill(Color.web("#ffffff"));

		content.getChildren().addAll(lbVisitors, ivVisitors);
		paneMain.setCenter(content);

		getChildren().add(paneMain);
	}
}
