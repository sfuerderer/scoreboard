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
	@Expose
	public String sFile;
	@Expose
	public boolean bActive;
	// 0 uses the default time between advertising of the settings; videos run as long as they are
	@Expose
	public int iDurationInSeconds;

	public FullscreenSpot(String sFile, boolean bActive, int iDurationInSeconds) {
		this.sFile = sFile;
		this.bActive = bActive;
		this.iDurationInSeconds = iDurationInSeconds;
	}

	public boolean isVideo() {
		return isVideo(sFile);
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
		return sFileName.charAt(0) != '.' && (isVideo(sFileName) || isImage(sFileName));
	}
}
