/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class MainViewRotationTest {
	private static final List<Integer> SIX = Arrays.asList(1, 2, 3, 4, 5, 6);

	@Test
	void noSponsorsGiveNoPage() {
		assertEquals(0, MainViewRotation.getPageCount(0, 4));
		assertEquals(Collections.emptyList(), MainViewRotation.getPage(Collections.<Integer>emptyList(), 4, 0));
	}

	@Test
	void fewSponsorsAreAlwaysShownTogether() {
		List<Integer> lTwo = Arrays.asList(1, 2);
		assertEquals(1, MainViewRotation.getPageCount(2, 4));
		assertEquals(lTwo, MainViewRotation.getPage(lTwo, 4, 0));
		assertEquals(lTwo, MainViewRotation.getPage(lTwo, 4, 7));
		assertEquals(Arrays.asList(1, 2, 3, 4), MainViewRotation.getPage(Arrays.asList(1, 2, 3, 4), 4, 3));
	}

	@Test
	void moreSponsorsThanPlacesRotateAndWrap() {
		assertEquals(Arrays.asList(1, 2, 3, 4), MainViewRotation.getPage(SIX, 4, 0));
		assertEquals(Arrays.asList(5, 6, 1, 2), MainViewRotation.getPage(SIX, 4, 1));
		assertEquals(Arrays.asList(3, 4, 5, 6), MainViewRotation.getPage(SIX, 4, 2));
		assertEquals(3, MainViewRotation.getPageCount(6, 4));
		assertEquals(MainViewRotation.getPage(SIX, 4, 0), MainViewRotation.getPage(SIX, 4, 3));
	}

	@Test
	void logosAreSplitAroundTheClock() {
		assertEquals(0, MainViewRotation.getLeftCount(0));
		assertEquals(1, MainViewRotation.getLeftCount(1));
		assertEquals(1, MainViewRotation.getLeftCount(2));
		assertEquals(2, MainViewRotation.getLeftCount(3));
		assertEquals(2, MainViewRotation.getLeftCount(4));
	}

	@Test
	void everySponsorIsShownEquallyOftenPerCycle() {
		int[] aShown = new int[7];
		int iPages = MainViewRotation.getPageCount(6, 4);
		for (int iPage = 0; iPage < iPages; iPage++)
			for (int iSponsor : MainViewRotation.getPage(SIX, 4, iPage))
				aShown[iSponsor]++;
		for (int iSponsor = 1; iSponsor <= 6; iSponsor++)
			assertEquals(2, aShown[iSponsor]);
	}
}
