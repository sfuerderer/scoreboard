/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Background of the main scoreboard panes: flat club blue with a lighter diagonal band, taken from the
 * white bar in the SC Sand crest. It is drawn instead of loaded from an image, so it stays sharp at
 * every resolution and has no texture that flickers on the LED wall.
 */
public final class ScoreboardBackground {
	public static final Color CLUB_BLUE = Color.web("#0c1d63");
	public static final Color BAND_BLUE = Color.web("#15297f");

	// Start and end of the band along the gradient line (0 = top left, 1 = bottom right)
	private static final double BAND_START = 0.50;
	private static final double BAND_END = 0.66;
	// Two stops at almost the same offset give a hard edge instead of a soft transition
	private static final double HARD_EDGE = 0.0001;

	private ScoreboardBackground() {
	}

	/**
	 * The band runs from bottom left to top right like the bar in the crest. The gradient line points
	 * down and to the right at 132° (measured like CSS, 0° = up) on the 514 × 280 layout of the wall.
	 */
	public static Background crestDiagonal() {
		LinearGradient gradient = new LinearGradient(0.0887, -0.18, 0.9113, 1.18, true, CycleMethod.NO_CYCLE,
				new Stop(0, CLUB_BLUE),
				new Stop(BAND_START, CLUB_BLUE),
				new Stop(BAND_START + HARD_EDGE, BAND_BLUE),
				new Stop(BAND_END, BAND_BLUE),
				new Stop(BAND_END + HARD_EDGE, CLUB_BLUE),
				new Stop(1, CLUB_BLUE));
		return new Background(new BackgroundFill(gradient, null, null));
	}
}
