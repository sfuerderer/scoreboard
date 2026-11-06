/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Lets the elements of a view slide in one after the other instead of all at once, like on TV graphics.
 */
public final class BuildIn {
	private static final Duration DURATION = Duration.millis(450);
	private static final Duration STAGGER = Duration.millis(110);
	private static final double DISTANCE = 40;

	private BuildIn() {
	}

	/**
	 * Starts the animation; the nodes are invisible until their turn. Null nodes are skipped.
	 */
	public static void play(Node... nodes) {
		int iIndex = 0;
		for (Node node : nodes) {
			if (node == null)
				continue;
			double dTargetX = node.getTranslateX();
			node.setOpacity(0);

			TranslateTransition slide = new TranslateTransition(DURATION, node);
			slide.setFromX(dTargetX + DISTANCE);
			slide.setToX(dTargetX);
			slide.setInterpolator(Interpolator.EASE_OUT);
			FadeTransition fade = new FadeTransition(DURATION, node);
			fade.setFromValue(0);
			fade.setToValue(1);

			ParallelTransition transition = new ParallelTransition(slide, fade);
			transition.setDelay(STAGGER.multiply(iIndex++));
			transition.play();
		}
	}
}
