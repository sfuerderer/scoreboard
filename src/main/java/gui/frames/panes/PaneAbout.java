/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import system.Images;

public class PaneAbout extends PaneMainScoreboard{
	
	public PaneAbout(Pane rootPane) {
		super(rootPane);
		setStyle("-fx-background-color: #000000;");
		
		ImageView imgLogo = new ImageView(Images.getImages().loadImageFromResource("logo_it_darkMode", "png"));
		imgLogo.setFitHeight(200);
		imgLogo.setPreserveRatio(true);
		
		HBox hbxResult = new HBox();
		hbxResult.prefWidthProperty().bind(this.widthProperty());
		hbxResult.prefHeightProperty().bind(this.heightProperty());
		hbxResult.setAlignment(Pos.CENTER);

		hbxResult.getChildren().add(imgLogo);
		
		getChildren().add(hbxResult);
	}

}
