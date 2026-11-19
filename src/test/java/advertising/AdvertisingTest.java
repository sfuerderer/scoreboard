/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AdvertisingTest {
	@TempDir
	Path tempDir;

	private void touch(String sFile) throws Exception {
		Path file = tempDir.resolve(sFile);
		Files.createDirectories(file.getParent());
		Files.write(file, new byte[] { 1 });
	}

	private Advertising load() throws Exception {
		Advertising advertising = new Advertising(tempDir.toFile());
		advertising.load();
		return advertising;
	}

	private static List<String> files(List<FullscreenSpot> lSpots) {
		List<String> lFiles = new ArrayList<String>();
		for (FullscreenSpot spot : lSpots)
			lFiles.add(spot.sFile);
		return lFiles;
	}

	@Test
	void withoutFileEveryFolderFileIsShownLikeBefore() throws Exception {
		touch("b.png");
		touch("a.jpg");
		touch("c.mp4");
		touch(".hidden.png");
		touch("notes.txt");
		touch("logos/logo.png");

		Advertising advertising = load();

		assertEquals(List.of("a.jpg", "b.png", "c.mp4"), files(advertising.getActiveFullscreenSpots()));
		assertTrue(advertising.getMainViewSponsors().isEmpty());
		for (AdPlacement placement : AdPlacement.values())
			assertNull(advertising.getPresentedBy(placement));
	}

	@Test
	void savedOrderAndActiveFlagAreKept() throws Exception {
		touch("a.jpg");
		touch("b.png");
		touch("c.mp4");
		Advertising advertising = load();
		AdvertisingConfig config = advertising.getConfig();
		config.lFullscreenSpots.clear();
		config.lFullscreenSpots.add(new FullscreenSpot("c.mp4", true, 0));
		config.lFullscreenSpots.add(new FullscreenSpot("a.jpg", true, 15));
		config.lFullscreenSpots.add(new FullscreenSpot("b.png", false, 0));
		advertising.save();

		Advertising loaded = load();

		assertEquals(List.of("c.mp4", "a.jpg"), files(loaded.getActiveFullscreenSpots()));
		assertEquals(15, loaded.getActiveFullscreenSpots().get(1).iDurationInSeconds);
	}

	@Test
	void filesAddedByHandAreAppendedAndMissingFilesRemoved() throws Exception {
		touch("b.png");
		Advertising advertising = load();
		advertising.getConfig().lFullscreenSpots.add(0, new FullscreenSpot("gone.png", true, 0));
		advertising.save();
		touch("a.jpg");

		Advertising loaded = load();

		assertEquals(List.of("b.png", "a.jpg"), files(loaded.getActiveFullscreenSpots()));
		assertEquals(List.of("b.png", "a.jpg"), files(loaded.getConfig().lFullscreenSpots));
	}

	@Test
	void spotsOfInactiveSponsorsAreNotShown() throws Exception {
		touch("a.jpg");
		touch("b.jpg");
		touch("c.jpg");
		Advertising advertising = load();
		AdvertisingConfig config = advertising.getConfig();
		config.lSponsors.add(new Sponsor("Active", null, true));
		config.lSponsors.add(new Sponsor("Paused", null, false));
		config.lFullscreenSpots.get(0).sSponsor = "Active";
		config.lFullscreenSpots.get(1).sSponsor = "Paused";
		advertising.save();

		Advertising loaded = load();

		assertEquals(List.of("a.jpg", "c.jpg"), files(loaded.getActiveFullscreenSpots()));
		assertEquals(List.of("b.jpg"), files(loaded.getConfig().getSpotsOf("Paused")));
		// Without a logo the sponsor can book full screen ads only
		assertTrue(loaded.getMainViewSponsors().isEmpty());
	}

	@Test
	void importedFilesDoNotOverwriteExistingOnes() throws Exception {
		touch("source/logo.png");
		File fSource = tempDir.resolve("source/logo.png").toFile();
		File fTarget = tempDir.resolve("logos").toFile();

		assertEquals("logo.png", Advertising.importFile(fSource, fTarget));
		assertEquals("logo-2.png", Advertising.importFile(fSource, fTarget));
		assertEquals("logo-3.png", Advertising.importFile(fSource, fTarget));
	}

	@Test
	void renamingAndRemovingSponsorsKeepsPlacesConsistent() {
		AdvertisingConfig config = new AdvertisingConfig();
		Sponsor one = new Sponsor("One", "one.png", true);
		config.lSponsors.add(one);
		config.lSponsors.add(new Sponsor("Two", "two.png", true));
		config.lMainViewSponsors.addAll(List.of("Two", "One"));
		config.mPresentedBy.put(AdPlacement.GOAL, "One");
		config.mPresentedBy.put(AdPlacement.CARD, "Two");
		FullscreenSpot spot = new FullscreenSpot("one.jpg", true, 0, "One");
		config.lFullscreenSpots.add(spot);

		config.renameSponsor(one, "Uno");

		assertEquals(List.of("Two", "Uno"), config.lMainViewSponsors);
		assertEquals("Uno", config.mPresentedBy.get(AdPlacement.GOAL));
		assertTrue(config.isSponsorPlaced("Uno"));
		assertEquals("Uno", spot.sSponsor);

		config.removeSponsor(one);

		assertEquals(List.of("Two"), config.lMainViewSponsors);
		assertNull(config.mPresentedBy.get(AdPlacement.GOAL));
		assertEquals("Two", config.mPresentedBy.get(AdPlacement.CARD));
		assertNull(spot.sSponsor);
		assertEquals(1, config.lFullscreenSpots.size());
		assertTrue(!config.hasSponsor("Uno") && config.hasSponsor("Two"));
	}

	@Test
	void sponsorsAreOnlyShownWhenActiveWithLogo() throws Exception {
		touch("logos/one.png");
		touch("logos/two.png");
		Advertising advertising = load();
		AdvertisingConfig config = advertising.getConfig();
		config.lSponsors.add(new Sponsor("One", "one.png", true));
		config.lSponsors.add(new Sponsor("Two", "two.png", false));
		config.lSponsors.add(new Sponsor("NoLogo", "missing.png", true));
		config.lMainViewSponsors.addAll(List.of("NoLogo", "Two", "One", "Unknown", "One"));
		config.mPresentedBy.put(AdPlacement.GOAL, "One");
		config.mPresentedBy.put(AdPlacement.CARD, "Two");
		config.mPresentedBy.put(AdPlacement.CORNERS, "NoLogo");
		advertising.save();

		Advertising loaded = load();

		assertEquals(1, loaded.getMainViewSponsors().size());
		assertEquals("One", loaded.getMainViewSponsors().get(0).sName);
		assertEquals("One", loaded.getPresentedBy(AdPlacement.GOAL).sName);
		assertNull(loaded.getPresentedBy(AdPlacement.CARD));
		assertNull(loaded.getPresentedBy(AdPlacement.CORNERS));
		assertNull(loaded.getPresentedBy(AdPlacement.VISITORS));
	}

	@Test
	void missingAndInvalidValuesAreRepaired() throws Exception {
		Files.writeString(tempDir.resolve(Advertising.S_FILE_NAME),
				"{ \"iMainViewMaxLogos\": 9, \"iMainViewRotationSeconds\": 0,"
						+ " \"mPresentedBy\": { \"GOAL\": \"\", \"UNKNOWN_PLACE\": \"X\" } }");

		AdvertisingConfig config = load().getConfig();

		assertEquals(AdvertisingConfig.DEFAULT_MAIN_VIEW_MAX_LOGOS, config.iMainViewMaxLogos);
		assertEquals(AdvertisingConfig.DEFAULT_MAIN_VIEW_ROTATION_SECONDS, config.iMainViewRotationSeconds);
		assertTrue(config.lSponsors.isEmpty());
		assertTrue(config.lMainViewSponsors.isEmpty());
		assertTrue(config.mPresentedBy.isEmpty());
		assertTrue(new File(tempDir.toFile(), Advertising.S_FILE_NAME).isFile());
	}
}
