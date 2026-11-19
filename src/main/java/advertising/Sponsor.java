/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import com.google.gson.annotations.Expose;

/**
 * A sponsor with its logo. The logo lies in the logo folder of the advertising folder and is optional, e.g. for
 * sponsors that only book full screen ads. An inactive sponsor is shown nowhere, not even in the full screen ads.
 */
public class Sponsor {
	@Expose
	public String sName;
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
