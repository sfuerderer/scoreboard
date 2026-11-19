/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import exceptions.FileDidNotExistException;
import exceptions.FileNotCreatedException;
import message.ErrorMsg;
import system.FileHandling;

/**
 * Sponsors and advertising of the scoreboard, stored in the advertising folder:
 *
 * <pre>
 * advertisment/
 *   advertising.json
 *   sponsoren/&lt;sponsor&gt;/logo.png, 24.jpg, ...
 *   verein/trailer.mp4          (ads without a sponsor)
 * </pre>
 *
 * Files that are copied into these folders by hand are added to the rotation. Older installations kept every file
 * directly in the advertising folder; they are moved into this structure once. Without a sponsor placed anywhere
 * the scoreboard looks like before the sponsors were added.
 */
public class Advertising {
	public static final String S_FOLDER = "advertisment";
	public static final String S_FILE_NAME = "advertising.json";
	public static final String S_SPONSOR_FOLDER = "sponsoren";
	public static final String S_CLUB_FOLDER = "verein";
	public static final String S_LOGO_NAME = "logo";
	// Logos of the first version with a format of 0
	private static final String S_OLD_LOGO_FOLDER = "logos";

	private static Advertising advertising;

	private final File fFolder;
	private AdvertisingConfig config;

	Advertising(File fFolder) {
		this.fFolder = fFolder;
	}

	/**
	 * Loads the advertising on first use (at the start of the program). A broken file is replaced by the folder
	 * content, so the full screen rotation still works.
	 */
	public static Advertising getAdvertising() {
		if (advertising == null) {
			advertising = new Advertising(new File(S_FOLDER));
			try {
				advertising.load();
			} catch (IOException e) {
				advertising.config = new AdvertisingConfig();
				advertising.config.iFormat = AdvertisingConfig.FORMAT_SPONSOR_FOLDERS;
				advertising.syncWithFolder();
				ErrorMsg.showErrorMessage(118, e.getMessage());
			}
		}
		return advertising;
	}

	void load() throws IOException {
		File file = new File(fFolder, S_FILE_NAME);
		if (file.exists()) {
			try {
				config = FileHandling.getFileHandling().loadTFromFile(file.getPath(), AdvertisingConfig.class);
			} catch (FileDidNotExistException e) {
				config = null;
			}
			if (config == null)
				config = new AdvertisingConfig();
		} else {
			// Without a file the folder is in the old format (or empty, then nothing is moved)
			config = new AdvertisingConfig();
		}
		config.update();
		if (config.iFormat < AdvertisingConfig.FORMAT_SPONSOR_FOLDERS && migrateToSponsorFolders()) {
			try {
				save();
			} catch (FileNotCreatedException e) {
				throw new IOException(e);
			}
		}
		config.iFormat = AdvertisingConfig.FORMAT_SPONSOR_FOLDERS;
		syncWithFolder();
	}

	public void save() throws FileNotCreatedException, IOException {
		FileHandling.getFileHandling().saveTToFile(config, new File(fFolder, S_FILE_NAME).getPath());
	}

	public AdvertisingConfig getConfig() {
		return config;
	}

	public File getFolder() {
		return fFolder;
	}

	public File getClubFolder() {
		return new File(fFolder, S_CLUB_FOLDER);
	}

	public File getSponsorFolder(Sponsor sponsor) {
		return new File(new File(fFolder, S_SPONSOR_FOLDER), sponsor.sFolder);
	}

	/**
	 * @return the folder new full screen ads of the sponsor are copied to, the club folder for null
	 */
	public File getSpotFolder(String sSponsor) {
		Sponsor sponsor = getSponsor(sSponsor);
		return sponsor == null ? getClubFolder() : getSponsorFolder(sponsor);
	}

	public File getSpotFile(FullscreenSpot spot) {
		return new File(fFolder, spot.sFile);
	}

	public File getLogoFile(Sponsor sponsor) {
		return new File(getSponsorFolder(sponsor), sponsor.sLogoFile);
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
	 * Adds a new sponsor with its own folder.
	 */
	public Sponsor addSponsor(String sName) {
		Sponsor sponsor = new Sponsor(sName, null, true);
		sponsor.sFolder = createFolderName(sName);
		config.lSponsors.add(sponsor);
		return sponsor;
	}

	/**
	 * Copies the logo into the sponsor folder as "logo.&lt;extension&gt;" and removes an older logo with another
	 * extension.
	 */
	public void setLogo(Sponsor sponsor, File fLogo) throws IOException {
		File fSponsorFolder = getSponsorFolder(sponsor);
		Files.createDirectories(fSponsorFolder.toPath());
		String sNewLogo = S_LOGO_NAME + getExtension(fLogo.getName());
		Files.copy(fLogo.toPath(), new File(fSponsorFolder, sNewLogo).toPath(), StandardCopyOption.REPLACE_EXISTING);
		if (sponsor.hasLogo() && !sponsor.sLogoFile.equals(sNewLogo))
			Files.deleteIfExists(getLogoFile(sponsor).toPath());
		sponsor.sLogoFile = sNewLogo;
	}

	/**
	 * Copies the file into the folder of the sponsor (or the club) and appends it to the rotation.
	 */
	public FullscreenSpot addSpot(File fSource, String sSponsor) throws IOException {
		File fTarget = importFile(fSource, getSpotFolder(sSponsor));
		FullscreenSpot spot = new FullscreenSpot(getRelativePath(fTarget), true, 0, sSponsor);
		config.lFullscreenSpots.add(spot);
		return spot;
	}

	/**
	 * Gives the spot to another sponsor (or the club) and moves its file into that folder.
	 */
	public void moveSpot(FullscreenSpot spot, String sSponsor) throws IOException {
		File fTarget = moveFile(getSpotFile(spot), getSpotFolder(sSponsor));
		spot.sFile = getRelativePath(fTarget);
		spot.sSponsor = sSponsor;
	}

	/**
	 * @return the sponsor if it can be shown: active and its logo exists
	 */
	private Sponsor getShowableSponsor(String sName) {
		Sponsor sponsor = getSponsor(sName);
		if (sponsor == null || !sponsor.bActive || !sponsor.hasLogo() || !getLogoFile(sponsor).isFile())
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
			if (spot.bActive && FullscreenSpot.isSupported(spot.sFile) && getSpotFile(spot).isFile()
					&& isSponsorActive(spot.sSponsor))
				lResult.add(spot);
		return lResult;
	}

	/**
	 * @return false only for an inactive sponsor; spots without or with an unknown sponsor are shown
	 */
	private boolean isSponsorActive(String sName) {
		Sponsor sponsor = getSponsor(sName);
		return sponsor == null || sponsor.bActive;
	}

	/**
	 * Files that were copied into the folders by hand are added at the end of the rotation, a file in a sponsor
	 * folder belongs to that sponsor. Spots whose file was deleted are removed.
	 */
	public void syncWithFolder() {
		config.lFullscreenSpots.removeIf(spot -> !getSpotFile(spot).isFile());
		Set<String> sKnown = new HashSet<String>();
		for (FullscreenSpot spot : config.lFullscreenSpots)
			sKnown.add(spot.sFile);

		// Files directly in the advertising folder still work, e.g. if someone copies them there out of habit
		addUnknownFiles(fFolder, null, sKnown);
		addUnknownFiles(getClubFolder(), null, sKnown);
		for (Sponsor sponsor : config.lSponsors)
			if (sponsor.sFolder != null)
				addUnknownFiles(getSponsorFolder(sponsor), sponsor, sKnown);
	}

	private void addUnknownFiles(File fSpotFolder, Sponsor sponsor, Set<String> sKnown) {
		for (File file : listSupportedFiles(fSpotFolder)) {
			String sPath = getRelativePath(file);
			// A logo in the sponsor folder never runs as full screen ad; one copied there by hand is used as logo
			if (sponsor != null && file.getName().toLowerCase().startsWith(S_LOGO_NAME + ".")) {
				if (!sponsor.hasLogo() || !getLogoFile(sponsor).isFile())
					sponsor.sLogoFile = file.getName();
				continue;
			}
			if (sKnown.contains(sPath))
				continue;
			config.lFullscreenSpots.add(new FullscreenSpot(sPath, true, 0, sponsor == null ? null : sponsor.sName));
			sKnown.add(sPath);
		}
	}

	/**
	 * Moves the files of the first format (everything directly in the advertising folder, logos in "logos") into
	 * one folder per sponsor and the club folder. Nothing is deleted.
	 *
	 * @return true if the configuration changed and has to be saved
	 */
	boolean migrateToSponsorFolders() {
		boolean bChanged = false;
		for (Sponsor sponsor : config.lSponsors) {
			if (sponsor.sFolder == null) {
				sponsor.sFolder = createFolderName(sponsor.sName);
				bChanged = true;
			}
			if (sponsor.hasLogo()) {
				File fOldLogo = new File(new File(fFolder, S_OLD_LOGO_FOLDER), sponsor.sLogoFile);
				if (fOldLogo.isFile()) {
					try {
						File fSponsorFolder = getSponsorFolder(sponsor);
						Files.createDirectories(fSponsorFolder.toPath());
						String sNewLogo = S_LOGO_NAME + getExtension(fOldLogo.getName());
						Files.move(fOldLogo.toPath(), new File(fSponsorFolder, sNewLogo).toPath());
						sponsor.sLogoFile = sNewLogo;
						bChanged = true;
					} catch (IOException e) {
						ErrorMsg.showErrorMessage(121, fOldLogo.getPath());
					}
				}
			}
		}

		// Files that are not in the list yet were shown before as well, they become ads of the club
		Set<String> sKnown = new HashSet<String>();
		for (FullscreenSpot spot : config.lFullscreenSpots)
			sKnown.add(spot.sFile);
		addUnknownFiles(fFolder, null, sKnown);

		for (FullscreenSpot spot : config.lFullscreenSpots) {
			File fSpot = getSpotFile(spot);
			// Only files directly in the advertising folder are moved
			if (spot.sFile.contains("/") || !fSpot.isFile())
				continue;
			try {
				moveSpot(spot, getSponsor(spot.sSponsor) == null ? null : spot.sSponsor);
				bChanged = true;
			} catch (IOException e) {
				ErrorMsg.showErrorMessage(121, fSpot.getPath());
			}
		}

		File fOldLogoFolder = new File(fFolder, S_OLD_LOGO_FOLDER);
		String[] aLeft = fOldLogoFolder.list();
		if (aLeft != null && aLeft.length == 0)
			fOldLogoFolder.delete();
		return bChanged;
	}

	/**
	 * @return a folder name that only uses a-z, 0-9 and "-" (umlauts break on the FAT formatted sticks) and is
	 *         not used by another sponsor
	 */
	String createFolderName(String sName) {
		String sBase = toFolderName(sName);
		Set<String> sUsed = new HashSet<String>();
		for (Sponsor sponsor : config.lSponsors)
			if (sponsor.sFolder != null)
				sUsed.add(sponsor.sFolder);
		File fSponsors = new File(fFolder, S_SPONSOR_FOLDER);
		String sFolderName = sBase;
		// A folder left by a deleted sponsor is not reused, its files would show up at the new sponsor
		for (int i = 2; sUsed.contains(sFolderName) || new File(fSponsors, sFolderName).exists(); i++)
			sFolderName = sBase + "-" + i;
		return sFolderName;
	}

	static String toFolderName(String sName) {
		String sLower = sName.toLowerCase(Locale.GERMAN).replace("ä", "ae").replace("ö", "oe").replace("ü", "ue")
				.replace("ß", "ss");
		String sAscii = Normalizer.normalize(sLower, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		String sFolderName = sAscii.replaceAll("[^a-z0-9]+", "-").replaceAll("^-+|-+$", "");
		return sFolderName.isEmpty() ? "sponsor" : sFolderName;
	}

	private String getRelativePath(File file) {
		return fFolder.toPath().relativize(file.toPath()).toString().replace(File.separatorChar, '/');
	}

	private static String getExtension(String sFileName) {
		int iDot = sFileName.lastIndexOf('.');
		return iDot > 0 ? sFileName.substring(iDot).toLowerCase() : "";
	}

	/**
	 * Copies the file into the folder. An existing file with the same name is not overwritten, the copy gets a
	 * number instead (24.jpg, 24-2.jpg, ...).
	 *
	 * @return the copy
	 */
	static File importFile(File fSource, File fTargetFolder) throws IOException {
		File fTarget = getFreeFile(fSource.getName(), fTargetFolder);
		Files.copy(fSource.toPath(), fTarget.toPath());
		return fTarget;
	}

	private static File moveFile(File fSource, File fTargetFolder) throws IOException {
		if (fSource.getParentFile() != null
				&& fSource.getParentFile().getAbsoluteFile().equals(fTargetFolder.getAbsoluteFile()))
			return fSource;
		File fTarget = getFreeFile(fSource.getName(), fTargetFolder);
		Files.move(fSource.toPath(), fTarget.toPath());
		return fTarget;
	}

	private static File getFreeFile(String sName, File fFolder) throws IOException {
		Files.createDirectories(fFolder.toPath());
		int iDot = sName.lastIndexOf('.');
		String sBase = iDot > 0 ? sName.substring(0, iDot) : sName;
		String sExtension = iDot > 0 ? sName.substring(iDot) : "";
		File fTarget = new File(fFolder, sName);
		for (int i = 2; fTarget.exists(); i++)
			fTarget = new File(fFolder, sBase + "-" + i + sExtension);
		return fTarget;
	}

	private static List<File> listSupportedFiles(File fSpotFolder) {
		List<File> lFiles = new ArrayList<File>();
		File[] files = fSpotFolder.listFiles(File::isFile);
		if (files != null)
			for (File file : files)
				if (FullscreenSpot.isSupported(file.getName()))
					lFiles.add(file);
		Collections.sort(lFiles);
		return lFiles;
	}
}
