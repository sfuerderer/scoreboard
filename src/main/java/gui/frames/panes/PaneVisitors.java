/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.util.Locale;

import gui.frames.panes.components.ScoreboardText;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import language.Language;
import system.Main;

/**
 * Number of spectators, e.g. "1.248 ZUSCHAUER".
 */
public class PaneVisitors extends PaneMainScoreboard {

	public PaneVisitors(Pane rootPane) {
		super(ScoreboardBackground.crestDiagonal(), rootPane);

		Label lbVisitors = ScoreboardText.label(String.format(Locale.GERMAN, "%,d", Main.getMatch().getiVisitors()),
				FontWeight.EXTRA_BOLD, 130, ScoreboardColors.TEXT);
		Label lbTitle = ScoreboardText.label(Language.getText("zuschauer"), FontWeight.SEMI_BOLD, 34,
				ScoreboardColors.TEXT_DIM);

		VBox content = new VBox(-6, lbVisitors, lbTitle);
		content.setAlignment(Pos.CENTER);
		content.prefWidthProperty().bind(widthProperty());
		content.prefHeightProperty().bind(heightProperty());
		getChildren().add(content);
	}
}
