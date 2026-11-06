/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.net.MalformedURLException;

import gui.GUIControlling;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import message.ErrorMsg;
import system.Images;
import system.Main;
import system.settings.Frame;

public class PaneImage extends PaneMainScoreboard {

	public PaneImage(Pane rootPane, String sPathToImage) {
		super(rootPane,false);
		BackgroundImage myBI;

		BorderPane wrapperPane = new BorderPane();
		wrapperPane.prefWidthProperty().bind(this.widthProperty());
		// During a match the score bar stays visible below the picture, so the picture is fitted above it
		if (showsScoreBar())
			wrapperPane.prefHeightProperty().bind(this.heightProperty().subtract(SCORE_BAR_HEIGHT));
		else
			wrapperPane.prefHeightProperty().bind(this.heightProperty());

		try {
			Frame mainFrame = GUIControlling.getSettings().mainFrame;
			myBI = new BackgroundImage(
					Images.getImages().loadImageFromPath(sPathToImage, mainFrame.iWidth, mainFrame.iHeight),
					BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false));
			wrapperPane.setBackground(new Background(myBI));
		} catch (MalformedURLException e) {
			ErrorMsg.showErrorMessage(112);
		}
		setStyle("-fx-background-color: #000000;");
		getChildren().addAll(wrapperPane);
	}

	@Override
	public boolean showsScoreBar() {
		return Main.getMatch() != null;
	}
}
