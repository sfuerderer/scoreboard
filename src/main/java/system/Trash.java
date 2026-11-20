/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import java.awt.Desktop;
import java.io.File;

/**
 * Moves files into the trash of the operating system, so deleted advertising can be restored.
 */
public final class Trash {
	private Trash() {
	}

	/**
	 * @return true if the file or folder is in the trash afterwards (or did not exist)
	 */
	public static boolean moveToTrash(File file) {
		if (!file.exists())
			return true;
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MOVE_TO_TRASH))
				return Desktop.getDesktop().moveToTrash(file);
		} catch (RuntimeException e) {
			// Headless or not supported on this system
		}
		return false;
	}
}
