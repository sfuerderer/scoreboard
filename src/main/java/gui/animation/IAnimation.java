/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

import gui.frames.panes.PaneMainScoreboard;
import javafx.scene.layout.StackPane;

public interface IAnimation {
	public void doAnimationIn(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane);
	public void doAnimationOut(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane);
}
