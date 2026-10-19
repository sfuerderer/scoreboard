/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import language.Languages;
import language.Texts;

class AnimationsTest {

	static class Data {
		@Expose
		Animations animation;
	}

	@Test
	void everyAnimationHasTextInAllLanguages() throws Exception {
		try (Reader reader = new InputStreamReader(AnimationsTest.class.getResourceAsStream("/config/language.json"),
				StandardCharsets.UTF_8)) {
			Texts texts = new Gson().fromJson(reader, Texts.class);
			for (Animations animation : Animations.values()) {
				for (Languages language : Languages.values()) {
					assertNotNull(texts.getTextInLanguage(language, animation.getsLanguageKey()));
				}
			}
			for (Languages language : Languages.values()) {
				assertNotNull(texts.getTextInLanguage(language, "animation"));
			}
		}
	}

	@Test
	void unknownAnimationInSettingsIsReadAsNull() {
		// Settings.update() replaces null with the default, so a settings.json written by a newer
		// version with an unknown animation does not break the start
		Data data = new Gson().fromJson("{ \"animation\": \"DOES_NOT_EXIST\" }", Data.class);
		assertNull(data.animation);
	}

	@Test
	void easeOutBackStartsAndEndsOnTargetAndOvershoots() {
		assertEquals(0, Easing.EASE_OUT_BACK.interpolate(0.0, 1.0, 0.0), 1e-9);
		assertEquals(1, Easing.EASE_OUT_BACK.interpolate(0.0, 1.0, 1.0), 1e-9);

		double dMax = 0;
		for (int i = 0; i <= 100; i++) {
			dMax = Math.max(dMax, Easing.EASE_OUT_BACK.interpolate(0.0, 1.0, i / 100.0));
		}
		assertTrue(dMax > 1);
	}
}
