/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package advertising;

import java.util.ArrayList;
import java.util.List;

/**
 * Decides which sponsors the main view shows. If more sponsors are booked than fit, the view changes page by
 * page and starts again at the beginning, so every sponsor is shown equally often.
 */
public final class MainViewRotation {
	private MainViewRotation() {
	}

	/**
	 * @return how many different pages exist, 0 without sponsors
	 */
	public static int getPageCount(int iSponsors, int iMaxLogos) {
		if (iSponsors <= 0 || iMaxLogos <= 0)
			return 0;
		if (iSponsors <= iMaxLogos)
			return 1;
		// Pages wrap around, so after this many pages the first one comes again
		return iSponsors / gcd(iSponsors, iMaxLogos);
	}

	/**
	 * @return the sponsors of the given page, at most iMaxLogos; pages wrap around at the end of the list
	 */
	public static <T> List<T> getPage(List<T> lSponsors, int iMaxLogos, int iPage) {
		List<T> lPage = new ArrayList<T>();
		int iCount = lSponsors.size();
		if (iCount == 0 || iMaxLogos <= 0)
			return lPage;
		if (iCount <= iMaxLogos)
			return new ArrayList<T>(lSponsors);
		int iStart = (int) (((long) iPage * iMaxLogos) % iCount);
		for (int i = 0; i < iMaxLogos; i++)
			lPage.add(lSponsors.get((iStart + i) % iCount));
		return lPage;
	}

	private static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}
}
