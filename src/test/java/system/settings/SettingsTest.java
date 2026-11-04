/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import javafx.scene.paint.Color;
import language.Languages;
import language.Texts;
import system.Defaults;

class SettingsTest {

	@Test
	void validColorsAreAccepted() {
		assertTrue(Settings.isValidColor(Defaults.ANIMATION_WIPE_COLOR));
		assertTrue(Settings.isValidColor("0x0057b8ff"));
	}

	@Test
	void invalidColorsAreRejected() {
		assertFalse(Settings.isValidColor(null));
		assertFalse(Settings.isValidColor(""));
		assertFalse(Settings.isValidColor("not a color"));
	}

	@Test
	void colorFromColorPickerCanBeReadAgain() {
		// The settings frame stores the color with toString()
		Color color = Color.web("#12ab34");
		assertTrue(Settings.isValidColor(color.toString()));
		assertEquals(color, Color.web(color.toString()));
	}

	@Test
	void wipeColorHasTextInAllLanguages() throws Exception {
		try (Reader reader = new InputStreamReader(SettingsTest.class.getResourceAsStream("/config/language.json"),
				StandardCharsets.UTF_8)) {
			Texts texts = new Gson().fromJson(reader, Texts.class);
			for (Languages language : Languages.values()) {
				assertNotNull(texts.getTextInLanguage(language, "animation_wipe_color"));
			}
		}
	}
}
