/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

import gui.frames.panes.PaneMainScoreboard;
import javafx.animation.Animation;
import javafx.scene.layout.StackPane;

/**
 * A transition between two panes of the main scoreboard. Both panes are already children of the
 * root pane when the animation is created. The caller removes the old pane and calls
 * {@link #cleanUp} afterwards, also when the animation is finished early.
 */
public interface IAnimation {
	public Animation createAnimation(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane);

	public default void cleanUp(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane) {
		resetPane(paneToAdd);
		resetPane(paneToRemove);
	}

	public static void resetPane(PaneMainScoreboard pane) {
		pane.setOpacity(1);
		pane.setTranslateX(0);
		pane.setTranslateY(0);
		pane.setScaleX(1);
		pane.setScaleY(1);
		pane.setVisible(true);
	}
}
