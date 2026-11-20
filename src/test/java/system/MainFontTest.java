/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import javafx.scene.text.FontWeight;
import language.Languages;
import language.Texts;

class MainFontTest {

	@Test
	void everyFontWeightHasAFontFile() {
		for (FontWeight weight : FontWeight.values()) {
			String sPath = "/fonts/" + Main.getMainFontFile(weight) + ".ttf";
			assertNotNull(MainFontTest.class.getResource(sPath), sPath + " fehlt");
		}
	}

	@Test
	void matchStartedTextExistsInAllLanguages() throws Exception {
		try (Reader reader = new InputStreamReader(MainFontTest.class.getResourceAsStream("/config/language.json"),
				StandardCharsets.UTF_8)) {
			Texts texts = new Gson().fromJson(reader, Texts.class);
			for (Languages language : Languages.values()) {
				assertNotNull(texts.getTextInLanguage(language, "spiel_gestartet"));
			}
		}
	}
}
