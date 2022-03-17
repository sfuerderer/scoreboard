/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.frameFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class FramesFunctionDate implements FramesFunction {
	@Override
	public Pane getSceneToDisplay() {
		HBox displayPane = new HBox();
		displayPane.setPadding(new Insets(2));
		displayPane.setAlignment(Pos.CENTER);

		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		Date date = new Date();

		Text tDate = new Text(formatter.format(date));
		tDate.getStyleClass().add("sf-text-otherFrames");

		displayPane.getChildren().add(tDate);

		displayPane.setStyle("-fx-background-color: #000000;");

		return displayPane;
	}

}
