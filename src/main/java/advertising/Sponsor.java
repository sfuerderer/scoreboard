/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import com.google.gson.annotations.Expose;

/**
 * A sponsor with its own folder for the logo and the full screen ads. The logo is optional, e.g. for sponsors
 * that only book full screen ads. An inactive sponsor is shown nowhere, not even in the full screen ads.
 */
public class Sponsor {
	@Expose
	public String sName;
	// Name of the sponsor folder; it stays the same when the sponsor is renamed
	@Expose
	public String sFolder;
	// Name of the logo inside the sponsor folder
	@Expose
	public String sLogoFile;
	@Expose
	public boolean bActive;

	public Sponsor(String sName, String sLogoFile, boolean bActive) {
		this.sName = sName;
		this.sLogoFile = sLogoFile;
		this.bActive = bActive;
	}

	public boolean hasLogo() {
		return sLogoFile != null && !sLogoFile.isEmpty();
	}
}
