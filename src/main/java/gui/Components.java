package gui;

import java.util.Locale;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import system.Main;

public class Components {

	private static final Insets DEFAULT_INSETS = new Insets(5, 0, 5, 0);

	/**
	 * Creates an JavaFx Separator
	 * 
	 * @return
	 */
	public static Separator getSeparator() {
		return getSeparator(VPos.CENTER, DEFAULT_INSETS);
	}

	public static Separator getSeparator(VPos verticalPosition) {
		return getSeparator(verticalPosition);
	}

	public static Separator getSeparator(Insets padding) {
		return getSeparator(VPos.CENTER, padding);
	}

	public static Separator getSeparator(VPos verticalPosition, Insets padding) {
		Separator separator = new Separator();
		separator.setValignment(verticalPosition);
		separator.setPadding(padding);

		return separator;
	}

	public static Label getLabelControlFrame(String text) {
		return getLabelControlFrame(text, -1);
	}

	public static Label getLabelControlFrame(String text, Pos horizontalAlignment) {
		return getLabelControlFrame(text, horizontalAlignment, -1);
	}

	public static Label getLabelControlFrame(String text, int fontSize) {
		return getLabelControlFrame(text, Pos.TOP_LEFT, fontSize);
	}

	public static Label getLabelControlFrame(String text, Pos horizontalAlignment, int fontSize) {
		Label label = new Label(text);
		if (fontSize > -1)
			label.setFont(Font.font(fontSize));

		label.setAlignment(horizontalAlignment);

		return label;
	}
	
	public static Label getLabel(String text) {
		return getLabel(text, -1);
	}
	
	public static Label getLabel(String text, int fontSize) {
		return getLabel(text, fontSize, Color.web("#ffffff"));
	}
	
	public static Label getLabel(String text, Color color) {
		return getLabel(text, -1, color);
	}
	
	/**
	 * Label for the scoreboards: scoreboard font, upper case like on TV graphics.
	 */
	public static Label getLabel(String text, int fontSize, Color color) {
		Label label = new Label(text == null ? "" : text.toUpperCase(Locale.GERMAN));
		
		if (fontSize > -1)
			label.setFont(Main.getMainFont(fontSize));
		else
			label.setFont(Main.getMainFont(17));
		
		label.setTextFill(color);
		
		return label;
	}
}
