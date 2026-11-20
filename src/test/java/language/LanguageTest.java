/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import football.Cards;
import football.person.CoachType;

class LanguageTest {

	@Test
	void getTextReturnsKeyBeforeInit() throws Exception {
		Field settings = Language.class.getDeclaredField("sSettings");
		settings.setAccessible(true);
		Object previous = settings.get(null);
		settings.set(null, null);
		try {
			assertEquals("gelbe_karte", Language.getText("gelbe_karte"));

			// Gson reads toString() of every enum constant when it creates the enum adapter
			Gson gson = new Gson();
			assertEquals(CoachType.values()[0], gson.fromJson("\"" + CoachType.values()[0].name() + "\"", CoachType.class));
			assertEquals(Cards.RED, gson.fromJson("\"RED\"", Cards.class));
		} finally {
			settings.set(null, previous);
		}
	}
}
