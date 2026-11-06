/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import java.util.Locale;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import system.Main;

/**
 * Labels for the scoreboard in the scoreboard font, upper case like on TV graphics.
 */
public final class ScoreboardText {

	private ScoreboardText() {
	}

	public static Label label(String sText, FontWeight weight, int iSize, Color color) {
		Label label = new Label(upper(sText));
		label.setFont(Main.getMainFont(weight, iSize));
		label.setTextFill(color);
		return label;
	}

	/**
	 * Label whose font gets smaller until the text fits into the width, so long team names are not cut off.
	 */
	public static Label fittingLabel(String sText, FontWeight weight, int iMaxSize, int iMinSize, double dMaxWidth,
			Color color) {
		String sShown = upper(sText);
		int iSize = iMaxSize;
		while (iSize > iMinSize && textWidth(sShown, Main.getMainFont(weight, iSize)) > dMaxWidth)
			iSize--;
		Label label = label(sShown, weight, iSize, color);
		label.setMaxWidth(dMaxWidth);
		return label;
	}

	public static String upper(String sText) {
		return sText == null ? "" : sText.toUpperCase(Locale.GERMAN);
	}

	private static double textWidth(String sText, Font font) {
		Text measure = new Text(sText);
		measure.setFont(font);
		return measure.getLayoutBounds().getWidth();
	}
}
