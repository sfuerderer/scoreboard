/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exceptions.FileDidNotExistException;
import exceptions.FileNotCreatedException;
import message.ErrorMsg;
import system.FileHandling;

/**
 * Sponsors and advertising of the scoreboard, stored in the advertising folder. Without a file every image and
 * video in the folder is shown in the full screen rotation and no sponsor is placed anywhere, so the scoreboard
 * looks like before the sponsors were added.
 */
public class Advertising {
	public static final String S_FOLDER = "advertisment";
	public static final String S_FILE_NAME = "advertising.json";
	public static final String S_LOGO_FOLDER = "logos";

	private static Advertising advertising;

	private final File fFolder;
	private AdvertisingConfig config;

	Advertising(File fFolder) {
		this.fFolder = fFolder;
	}

	/**
	 * Loads the advertising on first use. A broken file is replaced by the folder content, so the full screen
	 * rotation still works.
	 */
	public static Advertising getAdvertising() {
		if (advertising == null) {
			advertising = new Advertising(new File(S_FOLDER));
			try {
				advertising.load();
			} catch (IOException e) {
				advertising.config = advertising.createFromFolder();
				ErrorMsg.showErrorMessage(118, e.getMessage());
			}
		}
		return advertising;
	}

	void load() throws IOException {
		File file = new File(fFolder, S_FILE_NAME);
		if (!file.exists()) {
			config = createFromFolder();
			return;
		}
		try {
			config = FileHandling.getFileHandling().loadTFromFile(file.getPath(), AdvertisingConfig.class);
		} catch (FileDidNotExistException e) {
			config = null;
		}
		if (config == null)
			config = new AdvertisingConfig();
		config.update();
		addNewFolderFiles();
	}

	public void save() throws FileNotCreatedException, IOException {
		FileHandling.getFileHandling().saveTToFile(config, new File(fFolder, S_FILE_NAME).getPath());
	}

	public AdvertisingConfig getConfig() {
		return config;
	}

	/**
	 * Replaces the stored values, e.g. with the edited copy of the advertising window.
	 */
	public void setConfig(AdvertisingConfig config) {
		config.update();
		this.config = config;
	}

	public File getFolder() {
		return fFolder;
	}

	public File getLogoFolder() {
		return new File(fFolder, S_LOGO_FOLDER);
	}

	public File getSpotFile(FullscreenSpot spot) {
		return new File(fFolder, spot.sFile);
	}

	public File getLogoFile(Sponsor sponsor) {
		return new File(getLogoFolder(), sponsor.sLogoFile);
	}

	public Sponsor getSponsor(String sName) {
		if (sName == null)
			return null;
		for (Sponsor sponsor : config.lSponsors)
			if (sName.equals(sponsor.sName))
				return sponsor;
		return null;
	}

	/**
	 * @return the sponsor if it can be shown: active and its logo exists
	 */
	private Sponsor getShowableSponsor(String sName) {
		Sponsor sponsor = getSponsor(sName);
		if (sponsor == null || !sponsor.bActive || sponsor.sLogoFile == null || sponsor.sLogoFile.isEmpty()
				|| !getLogoFile(sponsor).isFile())
			return null;
		return sponsor;
	}

	/**
	 * @return the sponsor of the placement or null, then the placement is shown without a sponsor
	 */
	public Sponsor getPresentedBy(AdPlacement placement) {
		return getShowableSponsor(config.mPresentedBy.get(placement));
	}

	/**
	 * @return the sponsors of the main view in their order, empty if the main view shows no sponsor
	 */
	public List<Sponsor> getMainViewSponsors() {
		List<Sponsor> lResult = new ArrayList<Sponsor>();
		for (String sName : config.lMainViewSponsors) {
			Sponsor sponsor = getShowableSponsor(sName);
			if (sponsor != null && !lResult.contains(sponsor))
				lResult.add(sponsor);
		}
		return lResult;
	}

	/**
	 * @return the active images and videos of the full screen rotation that exist, in their order
	 */
	public List<FullscreenSpot> getActiveFullscreenSpots() {
		List<FullscreenSpot> lResult = new ArrayList<FullscreenSpot>();
		for (FullscreenSpot spot : config.lFullscreenSpots)
			if (spot.bActive && FullscreenSpot.isSupported(spot.sFile) && getSpotFile(spot).isFile())
				lResult.add(spot);
		return lResult;
	}

	/**
	 * Every supported file in the folder in alphabetical order, the way the rotation worked before.
	 */
	AdvertisingConfig createFromFolder() {
		AdvertisingConfig newConfig = new AdvertisingConfig();
		for (String sFile : listFolderFiles())
			newConfig.lFullscreenSpots.add(new FullscreenSpot(sFile, true, 0));
		return newConfig;
	}

	/**
	 * Files that were copied into the folder by hand are shown like before, at the end of the rotation.
	 */
	public void addNewFolderFiles() {
		Set<String> sKnown = new HashSet<String>();
		for (FullscreenSpot spot : config.lFullscreenSpots)
			sKnown.add(spot.sFile);
		for (String sFile : listFolderFiles())
			if (!sKnown.contains(sFile))
				config.lFullscreenSpots.add(new FullscreenSpot(sFile, true, 0));
	}

	private List<String> listFolderFiles() {
		List<String> lFiles = new ArrayList<String>();
		File[] files = fFolder.listFiles(File::isFile);
		if (files != null)
			for (File file : files)
				if (FullscreenSpot.isSupported(file.getName()))
					lFiles.add(file.getName());
		Collections.sort(lFiles);
		return lFiles;
	}
}
