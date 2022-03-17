/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import javafx.scene.layout.Pane;

public class PaneBlack extends PaneMainScoreboard {

	public PaneBlack(Pane rootPane) {
		super(rootPane,false);
		setStyle("-fx-background-color: #000000;");
	}
}
