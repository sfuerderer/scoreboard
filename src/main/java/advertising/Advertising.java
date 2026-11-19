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
 * The files in a sponsor folder are the media of the sponsor (its library). The places on the scoreboard choose
 * from it, e.g. the full screen ads are a list of chosen media. Older installations kept every file directly in
 * the advertising folder; they are moved into this structure once and keep running as ads without a sponsor.
 * Without a sponsor placed anywhere the scoreboard looks like before the sponsors were added.
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
		return getFile(spot.sFile);
	}

	/**
	 * @param sPath path inside the advertising folder, e.g. "sponsoren/baier/24.jpg"
	 */
	public File getFile(String sPath) {
		return new File(fFolder, sPath);
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
	 * @return the images and videos in the sponsor folder (without the logo) as paths inside the advertising
	 *         folder, sorted by name
	 */
	public List<String> getMedia(Sponsor sponsor) {
		List<String> lMedia = new ArrayList<String>();
		if (sponsor.sFolder == null)
			return lMedia;
		for (File file : listSupportedFiles(getSponsorFolder(sponsor)))
			if (!isLogo(file))
				lMedia.add(getRelativePath(file));
		return lMedia;
	}

	/**
	 * Copies the image or video into the sponsor folder. It is not shown anywhere until a place chooses it.
	 *
	 * @return the path of the copy inside the advertising folder
	 */
	public String addMedia(Sponsor sponsor, File fSource) throws IOException {
		return getRelativePath(importFile(fSource, getSponsorFolder(sponsor)));
	}

	/**
	 * Appends the medium of the sponsor to the full screen ads.
	 */
	public FullscreenSpot addToFullscreen(Sponsor sponsor, String sPath) {
		FullscreenSpot spot = new FullscreenSpot(sPath, true, 0, sponsor.sName);
		config.lFullscreenSpots.add(spot);
		return spot;
	}

	public boolean isInFullscreen(String sPath) {
		for (FullscreenSpot spot : config.lFullscreenSpots)
			if (spot.sFile.equals(sPath))
				return true;
		return false;
	}

	/**
	 * Removes the medium from all places, e.g. before its file is deleted.
	 */
	public void removeMedia(String sPath) {
		config.lFullscreenSpots.removeIf(spot -> spot.sFile.equals(sPath));
	}

	/**
	 * Gives a spot without a sponsor (from an older installation) to a sponsor and moves its file into the sponsor
	 * folder.
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
	 * Removes full screen ads whose file was deleted and takes a logo copied into a sponsor folder by hand. New
	 * files in the folders only become media of the sponsor; they are shown once a place chooses them.
	 */
	public void syncWithFolder() {
		config.lFullscreenSpots.removeIf(spot -> !getSpotFile(spot).isFile());
		for (Sponsor sponsor : config.lSponsors) {
			if (sponsor.sFolder == null || (sponsor.hasLogo() && getLogoFile(sponsor).isFile()))
				continue;
			sponsor.sLogoFile = null;
			for (File file : listSupportedFiles(getSponsorFolder(sponsor)))
				if (isLogo(file) && FullscreenSpot.isImage(file.getName())) {
					sponsor.sLogoFile = file.getName();
					break;
				}
		}
	}

	private static boolean isLogo(File file) {
		return file.getName().toLowerCase().startsWith(S_LOGO_NAME + ".");
	}

	/**
	 * Files directly in the advertising folder that are not in the list yet: before the sponsors they were all
	 * shown, so they are added as ads without a sponsor.
	 */
	private void addUnknownRootFiles() {
		Set<String> sKnown = new HashSet<String>();
		for (FullscreenSpot spot : config.lFullscreenSpots)
			sKnown.add(spot.sFile);
		for (File file : listSupportedFiles(fFolder)) {
			String sPath = getRelativePath(file);
			if (!sKnown.contains(sPath))
				config.lFullscreenSpots.add(new FullscreenSpot(sPath, true, 0, null));
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

		addUnknownRootFiles();

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
