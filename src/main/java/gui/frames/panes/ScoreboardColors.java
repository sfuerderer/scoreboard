/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import javafx.scene.paint.Color;

/**
 * Colors of the scoreboard design (version 2). The background colors are in {@link ScoreboardBackground}.
 */
public final class ScoreboardColors {
	// Text; slightly blue instead of pure white, which blooms on the LED wall
	public static final Color TEXT = Color.web("#f4f6fc");
	// Secondary text: competition, first names, separators
	public static final Color TEXT_DIM = Color.web("#8e98c4");
	// Accent: halftime label, progress of the halftime
	public static final Color ACCENT = Color.web("#2d4fe0");
	// Dark bars (clock, score bar), set off from the club blue background
	public static final Color PANEL = Color.web("#050d3a");
	// Track of the progress bar
	public static final Color PROGRESS_TRACK = Color.web("#1a2147");

	private ScoreboardColors() {
	}
}
