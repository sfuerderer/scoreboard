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
	// Accent: halftime label
	public static final Color ACCENT = Color.web("#2d4fe0");
	// Dark bars (clock, score bar), set off from the club blue background
	public static final Color PANEL = Color.web("#050d3a");
	// Lighter accent that stands out on the club blue (exclamation mark, bars in event views)
	public static final Color ACCENT_LIGHT = Color.web("#7f9bff");
	// Outline of the large shirt number behind a player
	public static final Color NUMBER_OUTLINE = Color.web("#f4f6fc", 0.2);
	public static final Color CARD_YELLOW = Color.web("#ffd21f");
	public static final Color CARD_RED = Color.web("#e3262f");
	// Red for text on the club blue; the card red is too dark there
	public static final Color CARD_RED_TEXT = Color.web("#ff5a62");
	public static final Color SUBSTITUTION_IN = Color.web("#22c55e");
	public static final Color SUBSTITUTION_OUT = Color.web("#e3262f");

	private ScoreboardColors() {
	}
}
