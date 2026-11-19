/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

/**
 * Everything that is stored in the advertising file.
 */
public class AdvertisingConfig {
	public static final int MAIN_VIEW_MAX_LOGOS_LIMIT = 4;
	public static final int DEFAULT_MAIN_VIEW_MAX_LOGOS = 4;
	public static final int DEFAULT_MAIN_VIEW_ROTATION_SECONDS = 10;

	@Expose
	public List<Sponsor> lSponsors = new ArrayList<Sponsor>();
	@Expose
	public List<FullscreenSpot> lFullscreenSpots = new ArrayList<FullscreenSpot>();
	// Names of the sponsors in the main view, in this order
	@Expose
	public List<String> lMainViewSponsors = new ArrayList<String>();
	@Expose
	public int iMainViewMaxLogos = DEFAULT_MAIN_VIEW_MAX_LOGOS;
	@Expose
	public int iMainViewRotationSeconds = DEFAULT_MAIN_VIEW_ROTATION_SECONDS;
	// Sponsor name per placement, a missing entry means no sponsor
	@Expose
	public Map<AdPlacement, String> mPresentedBy = new EnumMap<AdPlacement, String>(AdPlacement.class);

	public boolean hasSponsor(String sName) {
		for (Sponsor sponsor : lSponsors)
			if (sponsor.sName.equals(sName))
				return true;
		return false;
	}

	/**
	 * @return true if the sponsor is placed in the main view or presents an event
	 */
	public boolean isSponsorPlaced(String sName) {
		return lMainViewSponsors.contains(sName) || mPresentedBy.containsValue(sName);
	}

	/**
	 * Renames the sponsor and keeps its places.
	 */
	public void renameSponsor(Sponsor sponsor, String sNewName) {
		String sOldName = sponsor.sName;
		sponsor.sName = sNewName;
		lMainViewSponsors.replaceAll(sName -> sName.equals(sOldName) ? sNewName : sName);
		mPresentedBy.replaceAll((placement, sName) -> sName.equals(sOldName) ? sNewName : sName);
	}

	/**
	 * Removes the sponsor from the list and from all places.
	 */
	public void removeSponsor(Sponsor sponsor) {
		lSponsors.remove(sponsor);
		lMainViewSponsors.removeIf(sName -> sName.equals(sponsor.sName));
		mPresentedBy.values().removeIf(sName -> sName.equals(sponsor.sName));
	}

	/**
	 * Fills values that are missing in older or hand edited files.
	 */
	void update() {
		if (lSponsors == null)
			lSponsors = new ArrayList<Sponsor>();
		lSponsors.removeIf(sponsor -> sponsor == null || sponsor.sName == null);
		if (lFullscreenSpots == null)
			lFullscreenSpots = new ArrayList<FullscreenSpot>();
		lFullscreenSpots.removeIf(spot -> spot == null || spot.sFile == null || spot.sFile.isEmpty());
		if (lMainViewSponsors == null)
			lMainViewSponsors = new ArrayList<String>();
		lMainViewSponsors.removeIf(sName -> sName == null);
		if (iMainViewMaxLogos < 1 || iMainViewMaxLogos > MAIN_VIEW_MAX_LOGOS_LIMIT)
			iMainViewMaxLogos = DEFAULT_MAIN_VIEW_MAX_LOGOS;
		if (iMainViewRotationSeconds < 1)
			iMainViewRotationSeconds = DEFAULT_MAIN_VIEW_ROTATION_SECONDS;
		Map<AdPlacement, String> mCleaned = new EnumMap<AdPlacement, String>(AdPlacement.class);
		// Unknown placements are read as null keys
		if (mPresentedBy != null)
			mPresentedBy.forEach((placement, sName) -> {
				if (placement != null && sName != null && !sName.isEmpty())
					mCleaned.put(placement, sName);
			});
		mPresentedBy = mCleaned;
		for (FullscreenSpot spot : lFullscreenSpots)
			if (spot.iDurationInSeconds < 0)
				spot.iDurationInSeconds = 0;
	}
}
