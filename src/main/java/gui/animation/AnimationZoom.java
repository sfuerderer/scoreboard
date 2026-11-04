/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

import gui.frames.panes.PaneMainScoreboard;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * The new pane grows into the scoreboard with a small overshoot, the old one fades out.
 */
public class AnimationZoom implements IAnimation {
	private static final Duration DURATION = Duration.millis(550);
	private static final double D_START_SCALE = 0.8;

	@Override
	public Animation createAnimation(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane) {
		paneToAdd.setOpacity(0);
		ScaleTransition zoomIn = new ScaleTransition(DURATION, paneToAdd);
		zoomIn.setFromX(D_START_SCALE);
		zoomIn.setFromY(D_START_SCALE);
		zoomIn.setToX(1);
		zoomIn.setToY(1);
		zoomIn.setInterpolator(Easing.EASE_OUT_BACK);

		FadeTransition fadeIn = new FadeTransition(DURATION.divide(2), paneToAdd);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setInterpolator(Easing.EASE_OUT);

		FadeTransition fadeOut = new FadeTransition(DURATION.divide(2), paneToRemove);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);

		return new ParallelTransition(zoomIn, fadeIn, fadeOut);
	}

}
