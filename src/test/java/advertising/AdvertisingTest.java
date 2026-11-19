/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
		Files.write(file, sFile.getBytes());
	}

	private boolean exists(String sFile) {
		return Files.isRegularFile(tempDir.resolve(sFile));
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
	void emptyFolderStaysEmpty() throws Exception {
		Advertising advertising = load();

		assertTrue(advertising.getActiveFullscreenSpots().isEmpty());
		assertFalse(exists(Advertising.S_FILE_NAME));
	}

	@Test
	void oldFolderWithoutFileBecomesClubAdsInTheSameOrder() throws Exception {
		touch("b.png");
		touch("a.jpg");
		touch("c.mp4");
		touch(".hidden.png");
		touch("notes.txt");

		Advertising advertising = load();

		assertEquals(List.of("verein/a.jpg", "verein/b.png", "verein/c.mp4"),
				files(advertising.getActiveFullscreenSpots()));
		assertTrue(exists("verein/a.jpg") && !exists("a.jpg"));
		assertTrue(exists("notes.txt"));
		assertTrue(advertising.getMainViewSponsors().isEmpty());
		for (AdPlacement placement : AdPlacement.values())
			assertNull(advertising.getPresentedBy(placement));
		// Saved, so the next start does not migrate again
		assertEquals(files(advertising.getActiveFullscreenSpots()), files(load().getActiveFullscreenSpots()));
	}

	@Test
	void firstFormatIsMovedIntoSponsorFolders() throws Exception {
		touch("01.png");
		touch("02.jpg");
		touch("03.jpg");
		touch("04.jpg");
		touch("logos/suewag.png");
		Files.writeString(tempDir.resolve(Advertising.S_FILE_NAME), "{"
				+ "\"lSponsors\": [{\"sName\": \"Süwag\", \"sLogoFile\": \"suewag.png\", \"bActive\": true},"
				+ " {\"sName\": \"Roth Die Autohäuser\", \"bActive\": true}],"
				+ "\"lFullscreenSpots\": [{\"sFile\": \"02.jpg\", \"bActive\": true, \"iDurationInSeconds\": 0,"
				+ " \"sSponsor\": \"Süwag\"}, {\"sFile\": \"01.png\", \"bActive\": false, \"iDurationInSeconds\": 12,"
				+ " \"sSponsor\": \"Roth Die Autohäuser\"}, {\"sFile\": \"03.jpg\", \"bActive\": true,"
				+ " \"iDurationInSeconds\": 0}],"
				+ "\"lMainViewSponsors\": [\"Süwag\"]}");

		Advertising advertising = load();
		AdvertisingConfig config = advertising.getConfig();

		assertEquals(List.of("sponsoren/suewag/02.jpg", "sponsoren/roth-die-autohaeuser/01.png", "verein/03.jpg",
				"verein/04.jpg"), files(config.lFullscreenSpots));
		assertFalse(config.lFullscreenSpots.get(1).bActive);
		assertEquals(12, config.lFullscreenSpots.get(1).iDurationInSeconds);
		assertTrue(exists("sponsoren/suewag/logo.png"));
		assertFalse(Files.exists(tempDir.resolve("logos")));
		assertEquals("Süwag", advertising.getMainViewSponsors().get(0).sName);
		assertEquals(AdvertisingConfig.FORMAT_SPONSOR_FOLDERS, config.iFormat);

		Advertising loaded = load();
		assertEquals(files(config.lFullscreenSpots), files(loaded.getConfig().lFullscreenSpots));
		assertEquals("logo.png", loaded.getSponsor("Süwag").sLogoFile);
	}

	@Test
	void filesCopiedIntoSponsorFoldersBecomeMediaButAreNotShownYet() throws Exception {
		Advertising advertising = load();
		advertising.addSponsor("Baier");
		advertising.getConfig().iFormat = AdvertisingConfig.FORMAT_SPONSOR_FOLDERS;
		advertising.save();
		touch("sponsoren/baier/25.jpg");
		touch("sponsoren/baier/24.jpg");
		touch("sponsoren/baier/logo.png");

		Advertising loaded = load();
		Sponsor baier = loaded.getSponsor("Baier");

		assertEquals("logo.png", baier.sLogoFile);
		assertEquals(List.of("sponsoren/baier/24.jpg", "sponsoren/baier/25.jpg"), loaded.getMedia(baier));
		// The full screen ads only show what was chosen from the sponsors
		assertTrue(loaded.getConfig().lFullscreenSpots.isEmpty());

		loaded.addToFullscreen(baier, "sponsoren/baier/25.jpg");
		assertTrue(loaded.isInFullscreen("sponsoren/baier/25.jpg"));
		assertFalse(loaded.isInFullscreen("sponsoren/baier/24.jpg"));
		loaded.removeMedia("sponsoren/baier/25.jpg");
		assertTrue(loaded.getConfig().lFullscreenSpots.isEmpty());
	}

	@Test
	void savedOrderAndActiveFlagAreKeptAndMissingFilesRemoved() throws Exception {
		touch("verein/a.jpg");
		touch("verein/b.png");
		touch("verein/c.mp4");
		Advertising advertising = load();
		AdvertisingConfig config = advertising.getConfig();
		config.iFormat = AdvertisingConfig.FORMAT_SPONSOR_FOLDERS;
		config.lFullscreenSpots.clear();
		config.lFullscreenSpots.add(new FullscreenSpot("verein/c.mp4", true, 0));
		config.lFullscreenSpots.add(new FullscreenSpot("verein/gone.png", true, 0));
		config.lFullscreenSpots.add(new FullscreenSpot("verein/a.jpg", true, 15));
		config.lFullscreenSpots.add(new FullscreenSpot("verein/b.png", false, 0));
		advertising.save();

		Advertising loaded = load();

		assertEquals(List.of("verein/c.mp4", "verein/a.jpg"), files(loaded.getActiveFullscreenSpots()));
		assertEquals(15, loaded.getActiveFullscreenSpots().get(1).iDurationInSeconds);
		assertEquals(3, loaded.getConfig().lFullscreenSpots.size());
	}

	@Test
	void sponsorsAreOnlyShownWhenActiveWithLogo() throws Exception {
		Advertising advertising = load();
		Sponsor one = advertising.addSponsor("One");
		Sponsor two = advertising.addSponsor("Two");
		advertising.addSponsor("NoLogo").sLogoFile = "logo.png";
		touch("sponsoren/one/logo.png");
		touch("sponsoren/two/logo.png");
		one.sLogoFile = "logo.png";
		two.sLogoFile = "logo.png";
		two.bActive = false;
		AdvertisingConfig config = advertising.getConfig();
		config.lMainViewSponsors.addAll(List.of("NoLogo", "Two", "One", "Unknown", "One"));
		config.mPresentedBy.put(AdPlacement.GOAL, "One");
		config.mPresentedBy.put(AdPlacement.CARD, "Two");
		config.mPresentedBy.put(AdPlacement.CORNERS, "NoLogo");

		assertEquals(List.of(one), advertising.getMainViewSponsors());
		assertEquals(one, advertising.getPresentedBy(AdPlacement.GOAL));
		assertNull(advertising.getPresentedBy(AdPlacement.CARD));
		assertNull(advertising.getPresentedBy(AdPlacement.CORNERS));
		assertNull(advertising.getPresentedBy(AdPlacement.VISITORS));
	}

	@Test
	void spotsOfInactiveSponsorsAreNotShown() throws Exception {
		touch("source/a.jpg");
		touch("source/b.jpg");
		Advertising advertising = load();
		Sponsor active = advertising.addSponsor("Active");
		Sponsor paused = advertising.addSponsor("Paused");
		paused.bActive = false;
		advertising.addToFullscreen(active, advertising.addMedia(active, tempDir.resolve("source/a.jpg").toFile()));
		advertising.addToFullscreen(paused, advertising.addMedia(paused, tempDir.resolve("source/b.jpg").toFile()));

		assertEquals(List.of("sponsoren/active/a.jpg"), files(advertising.getActiveFullscreenSpots()));
		assertEquals(List.of("sponsoren/paused/b.jpg"), files(advertising.getConfig().getSpotsOf("Paused")));
		assertTrue(advertising.getMainViewSponsors().isEmpty());
	}

	@Test
	void mediaAndLogosAreCopiedWithoutOverwritingAndSpotsWithoutSponsorCanBeAssigned() throws Exception {
		touch("source/24.jpg");
		touch("source/logo.jpg");
		touch("source/new-logo.png");
		touch("verein/old.jpg");
		File fSpot = tempDir.resolve("source/24.jpg").toFile();
		Advertising advertising = load();
		Sponsor baier = advertising.addSponsor("Baier");

		assertEquals("sponsoren/baier/24.jpg", advertising.addMedia(baier, fSpot));
		assertEquals("sponsoren/baier/24-2.jpg", advertising.addMedia(baier, fSpot));

		FullscreenSpot old = new FullscreenSpot("verein/old.jpg", true, 0);
		advertising.getConfig().lFullscreenSpots.add(old);
		advertising.moveSpot(old, "Baier");
		assertEquals("sponsoren/baier/old.jpg", old.sFile);
		assertEquals("Baier", old.sSponsor);
		assertTrue(exists("sponsoren/baier/old.jpg") && !exists("verein/old.jpg"));

		advertising.setLogo(baier, tempDir.resolve("source/logo.jpg").toFile());
		advertising.setLogo(baier, tempDir.resolve("source/new-logo.png").toFile());
		assertEquals("logo.png", baier.sLogoFile);
		assertFalse(exists("sponsoren/baier/logo.jpg"));
		assertTrue(exists("source/logo.jpg"));
		assertEquals(List.of("sponsoren/baier/24-2.jpg", "sponsoren/baier/24.jpg", "sponsoren/baier/old.jpg"),
				advertising.getMedia(baier));
	}

	@Test
	void folderNamesAreSafeAndUnique() throws Exception {
		assertEquals("suewag", Advertising.toFolderName("Süwag"));
		assertEquals("roth-die-autohaeuser", Advertising.toFolderName("Roth Die Autohäuser"));
		assertEquals("mcdonald-s", Advertising.toFolderName("McDonald's"));
		assertEquals("cafe-strasse", Advertising.toFolderName("Café Straße"));
		assertEquals("sponsor", Advertising.toFolderName("!!!"));

		touch("sponsoren/joma/old.jpg");
		Advertising advertising = load();
		assertEquals("joma-2", advertising.addSponsor("Joma").sFolder);
		assertEquals("joma-3", advertising.addSponsor("JOMA!").sFolder);
	}

	@Test
	void missingAndInvalidValuesAreRepaired() throws Exception {
		Files.writeString(tempDir.resolve(Advertising.S_FILE_NAME),
				"{ \"iFormat\": 1, \"iMainViewMaxLogos\": 9, \"iMainViewRotationSeconds\": 0,"
						+ " \"mPresentedBy\": { \"GOAL\": \"\", \"UNKNOWN_PLACE\": \"X\" } }");

		AdvertisingConfig config = load().getConfig();

		assertEquals(AdvertisingConfig.DEFAULT_MAIN_VIEW_MAX_LOGOS, config.iMainViewMaxLogos);
		assertEquals(AdvertisingConfig.DEFAULT_MAIN_VIEW_ROTATION_SECONDS, config.iMainViewRotationSeconds);
		assertTrue(config.lSponsors.isEmpty());
		assertTrue(config.lMainViewSponsors.isEmpty());
		assertTrue(config.mPresentedBy.isEmpty());
	}

	@Test
	void renamingAndRemovingSponsorsKeepsPlacesConsistent() {
		AdvertisingConfig config = new AdvertisingConfig();
		Sponsor one = new Sponsor("One", "logo.png", true);
		config.lSponsors.add(one);
		config.lSponsors.add(new Sponsor("Two", "logo.png", true));
		config.lMainViewSponsors.addAll(List.of("Two", "One"));
		config.mPresentedBy.put(AdPlacement.GOAL, "One");
		config.mPresentedBy.put(AdPlacement.CARD, "Two");
		FullscreenSpot spot = new FullscreenSpot("sponsoren/one/a.jpg", true, 0, "One");
		config.lFullscreenSpots.add(spot);
		config.lFullscreenSpots.add(new FullscreenSpot("verein/b.jpg", true, 0));

		config.renameSponsor(one, "Uno");

		assertEquals(List.of("Two", "Uno"), config.lMainViewSponsors);
		assertEquals("Uno", config.mPresentedBy.get(AdPlacement.GOAL));
		assertTrue(config.isSponsorPlaced("Uno"));
		assertEquals("Uno", spot.sSponsor);

		config.removeSponsor(one);

		assertEquals(List.of("Two"), config.lMainViewSponsors);
		assertNull(config.mPresentedBy.get(AdPlacement.GOAL));
		assertEquals("Two", config.mPresentedBy.get(AdPlacement.CARD));
		assertEquals(List.of("verein/b.jpg"), files(config.lFullscreenSpots));
		assertTrue(!config.hasSponsor("Uno") && config.hasSponsor("Two"));
	}
}
