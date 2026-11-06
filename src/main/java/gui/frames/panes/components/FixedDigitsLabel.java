/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Shows text such as "67:12" or "2:1" with every digit in a cell of the same width. The scoreboard font
 * has proportional digits (a "1" is much narrower than a "4"), so a normal label would jump sideways
 * every second while the clock runs. Other characters (":", "'") keep their natural width.
 */
public class FixedDigitsLabel extends HBox {
	private final Font font;
	private final Color color;
	private final double dDigitWidth;
	private final StringProperty sText = new SimpleStringProperty("");

	public FixedDigitsLabel(String text, Font font, Color color) {
		this.font = font;
		this.color = color;
		this.dDigitWidth = getWidestDigit(font);
		setAlignment(Pos.CENTER);
		setMaxWidth(Region.USE_PREF_SIZE);
		sText.addListener((observable, oldText, newText) -> showText(newText));
		sText.set(text);
	}

	public StringProperty textProperty() {
		return sText;
	}

	public void setText(String text) {
		sText.set(text);
	}

	public String getText() {
		return sText.get();
	}

	private void showText(String text) {
		String sShown = text == null ? "" : text;

		// The labels are reused, because the clock changes the text every second
		while (getChildren().size() > sShown.length())
			getChildren().remove(getChildren().size() - 1);
		while (getChildren().size() < sShown.length())
			getChildren().add(createCell());

		for (int i = 0; i < sShown.length(); i++) {
			char cCharacter = sShown.charAt(i);
			Label lbCell = (Label) getChildren().get(i);
			lbCell.setText(String.valueOf(cCharacter));
			double dWidth = Character.isDigit(cCharacter) ? dDigitWidth : Region.USE_COMPUTED_SIZE;
			lbCell.setMinWidth(Character.isDigit(cCharacter) ? dDigitWidth : Region.USE_PREF_SIZE);
			lbCell.setPrefWidth(dWidth);
		}
	}

	private Label createCell() {
		Label lbCell = new Label();
		lbCell.setFont(font);
		lbCell.setTextFill(color);
		lbCell.setAlignment(Pos.CENTER);
		return lbCell;
	}

	private static double getWidestDigit(Font font) {
		Text measure = new Text();
		measure.setFont(font);
		double dWidest = 0;
		for (char cDigit = '0'; cDigit <= '9'; cDigit++) {
			measure.setText(String.valueOf(cDigit));
			dWidest = Math.max(dWidest, measure.getLayoutBounds().getWidth());
		}
		return Math.ceil(dWidest);
	}
}
