/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ClockProgressTest {

	@Test
	void progressCountsFromTheStartOfTheCurrentHalftime() {
		assertEquals(0.0, ClockProgress.of("45:00", 45, 45), 1e-9);
		assertEquals(0.5, ClockProgress.of("67:30", 45, 45), 1e-9);
		assertEquals(0.5, ClockProgress.of("22:30", 0, 45), 1e-9);
	}

	@Test
	void progressStaysBetweenZeroAndOne() {
		assertEquals(1.0, ClockProgress.of("95:10", 45, 45), 1e-9);
		assertEquals(0.0, ClockProgress.of("10:00", 45, 45), 1e-9);
	}

	@Test
	void unreadableClockGivesNoProgress() {
		assertEquals(0.0, ClockProgress.of(null, 0, 45), 1e-9);
		assertEquals(0.0, ClockProgress.of("", 0, 45), 1e-9);
		assertEquals(0.0, ClockProgress.of("ab:cd", 0, 45), 1e-9);
		assertEquals(0.0, ClockProgress.of("12:00", 0, 0), 1e-9);
	}

	@Test
	void clockWithMoreThanTwoDigitsForMinutesIsRead() {
		assertEquals(0.5, ClockProgress.of("112:30", 105, 15), 1e-9);
	}
}
