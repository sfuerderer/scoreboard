/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import language.Languages;
import language.Texts;
import system.Defaults;

class ScalingModeTest {

	static class Data {
		@Expose
		ScalingMode scalingMode;
	}

	@Test
	void fillUsesResolutionOfTheWall() {
		Frame frame = new Frame(true, 969, 400, 0, 0, null);
		assertEquals(Defaults.SCOREBOARD_DESIGN_WIDTH, ScalingMode.FILL.getLayoutWidth(frame));
		assertEquals(Defaults.SCOREBOARD_DESIGN_HEIGHT, ScalingMode.FILL.getLayoutHeight(frame));
	}

	@Test
	void containUsesWindowSize() {
		Frame frame = new Frame(true, 969, 400, 0, 0, null);
		assertEquals(969, ScalingMode.CONTAIN.getLayoutWidth(frame));
		assertEquals(400, ScalingMode.CONTAIN.getLayoutHeight(frame));
	}

	@Test
	void fillUsesGivenDesignSizeForOtherScoreboards() {
		Frame frame = new Frame(true, 130, 32, 0, 0, null);
		assertEquals(Defaults.OTHER_SCOREBOARD_DESIGN_WIDTH,
				ScalingMode.FILL.getLayoutWidth(frame, Defaults.OTHER_SCOREBOARD_DESIGN_WIDTH));
		assertEquals(Defaults.OTHER_SCOREBOARD_DESIGN_HEIGHT,
				ScalingMode.FILL.getLayoutHeight(frame, Defaults.OTHER_SCOREBOARD_DESIGN_HEIGHT));
		assertEquals(130, ScalingMode.CONTAIN.getLayoutWidth(frame, Defaults.OTHER_SCOREBOARD_DESIGN_WIDTH));
		assertEquals(32, ScalingMode.CONTAIN.getLayoutHeight(frame, Defaults.OTHER_SCOREBOARD_DESIGN_HEIGHT));
	}

	@Test
	void everyModeHasTextInAllLanguages() throws Exception {
		try (Reader reader = new InputStreamReader(ScalingModeTest.class.getResourceAsStream("/config/language.json"),
				StandardCharsets.UTF_8)) {
			Texts texts = new Gson().fromJson(reader, Texts.class);
			for (Languages language : Languages.values()) {
				assertNotNull(texts.getTextInLanguage(language, "scaling"));
				for (ScalingMode mode : ScalingMode.values()) {
					assertNotNull(texts.getTextInLanguage(language, mode.getsLanguageKey()));
				}
			}
		}
	}

	@Test
	void unknownModeInSettingsIsReadAsNull() {
		// Settings.update() replaces null with the default
		Data data = new Gson().fromJson("{ \"scalingMode\": \"DOES_NOT_EXIST\" }", Data.class);
		assertNull(data.scalingMode);
	}
}
