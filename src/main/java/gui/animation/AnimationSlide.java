/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

import gui.frames.panes.PaneMainScoreboard;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * The new pane pushes the old one out of the scoreboard, either to the left or to the top.
 */
public class AnimationSlide implements IAnimation {
	private static final Duration DURATION = Duration.millis(600);
	private final boolean bVertical;

	public AnimationSlide(boolean bVertical) {
		this.bVertical = bVertical;
	}

	@Override
	public Animation createAnimation(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane) {
		double dDistance = bVertical ? rootPane.getHeight() : rootPane.getWidth();

		TranslateTransition slideIn = new TranslateTransition(DURATION, paneToAdd);
		TranslateTransition slideOut = new TranslateTransition(DURATION, paneToRemove);
		if (bVertical) {
			paneToAdd.setTranslateY(dDistance);
			slideIn.setFromY(dDistance);
			slideIn.setToY(0);
			slideOut.setFromY(0);
			slideOut.setToY(-dDistance);
		} else {
			paneToAdd.setTranslateX(dDistance);
			slideIn.setFromX(dDistance);
			slideIn.setToX(0);
			slideOut.setFromX(0);
			slideOut.setToX(-dDistance);
		}
		slideIn.setInterpolator(Easing.EASE_OUT);
		slideOut.setInterpolator(Easing.EASE_OUT);

		return new ParallelTransition(slideIn, slideOut);
	}

}
