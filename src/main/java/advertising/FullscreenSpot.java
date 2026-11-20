/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import com.google.gson.annotations.Expose;

/**
 * An image or video of the full screen advertising rotation.
 */
public class FullscreenSpot {
	// Path inside the advertising folder with "/" as separator, e.g. "sponsoren/baier/24.jpg"
	@Expose
	public String sFile;
	@Expose
	public boolean bActive;
	// 0 uses the default time between advertising of the settings; videos run as long as they are
	@Expose
	public int iDurationInSeconds;
	// Name of the sponsor the spot belongs to, null for spots of the club (e.g. a trailer)
	@Expose
	public String sSponsor;

	public FullscreenSpot(String sFile, boolean bActive, int iDurationInSeconds) {
		this(sFile, bActive, iDurationInSeconds, null);
	}

	public FullscreenSpot(String sFile, boolean bActive, int iDurationInSeconds, String sSponsor) {
		this.sFile = sFile;
		this.bActive = bActive;
		this.iDurationInSeconds = iDurationInSeconds;
		this.sSponsor = sSponsor;
	}

	public boolean isVideo() {
		return isVideo(sFile);
	}

	/**
	 * @return the file name without folders, as shown to the user
	 */
	public String getFileName() {
		return sFile.substring(sFile.lastIndexOf('/') + 1);
	}

	static String getFileExtension(String sFileName) {
		int iDot = sFileName.lastIndexOf('.');
		return iDot > 0 ? sFileName.substring(iDot + 1).toLowerCase() : "";
	}

	public static boolean isVideo(String sFileName) {
		return getFileExtension(sFileName).equals("mp4");
	}

	public static boolean isImage(String sFileName) {
		String sExtension = getFileExtension(sFileName);
		return sExtension.equals("jpg") || sExtension.equals("png");
	}

	/**
	 * @return true for visible images and videos the rotation can show
	 */
	public static boolean isSupported(String sFileName) {
		String sName = sFileName.substring(sFileName.lastIndexOf('/') + 1);
		return !sName.isEmpty() && sName.charAt(0) != '.' && (isVideo(sName) || isImage(sName));
	}
}
