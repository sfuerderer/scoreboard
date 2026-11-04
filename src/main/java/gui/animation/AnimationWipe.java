/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

import gui.frames.panes.PaneMainScoreboard;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import system.Defaults;

/**
 * A slanted bar sweeps across the scoreboard and hides the change of the panes, like the
 * stinger transitions known from sports broadcasts.
 */
public class AnimationWipe implements IAnimation {
	private static final Duration DURATION_HALF = Duration.millis(350);
	private static final String S_BAR_ID = "animationWipeBar";

	@Override
	public Animation createAnimation(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane) {
		double dWidth = rootPane.getWidth();
		double dHeight = rootPane.getHeight();
		double dSlant = dHeight / 2;

		// Wide enough to cover the whole scoreboard in the middle of the animation
		Polygon bar = new Polygon(
				dSlant, 0,
				dWidth + 2 * dSlant, 0,
				dWidth + dSlant, dHeight,
				0, dHeight);
		bar.setFill(Color.web(Defaults.ANIMATION_WIPE_COLOR));
		bar.setId(S_BAR_ID);
		bar.setManaged(false);
		rootPane.getChildren().add(bar);

		paneToAdd.setVisible(false);

		TranslateTransition barIn = new TranslateTransition(DURATION_HALF, bar);
		barIn.setFromX(-(dWidth + 2 * dSlant));
		barIn.setToX(-dSlant);
		barIn.setInterpolator(Easing.EASE_IN);
		barIn.setOnFinished(evt -> {
			paneToAdd.setVisible(true);
			paneToRemove.setVisible(false);
		});

		TranslateTransition barOut = new TranslateTransition(DURATION_HALF, bar);
		barOut.setFromX(-dSlant);
		barOut.setToX(dWidth);
		barOut.setInterpolator(Easing.EASE_OUT);

		return new SequentialTransition(barIn, barOut);
	}

	@Override
	public void cleanUp(PaneMainScoreboard paneToAdd, PaneMainScoreboard paneToRemove, StackPane rootPane) {
		IAnimation.super.cleanUp(paneToAdd, paneToRemove, rootPane);
		rootPane.getChildren().removeIf(node -> S_BAR_ID.equals(node.getId()));
	}

}
