/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.advertisingFrame;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import advertising.Advertising;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import system.Images;

/**
 * One tab of the advertising window.
 */
abstract class AdvertisingTab extends BorderPane {
	protected final Advertising advertising;
	private final Runnable onChange;
	private final Map<String, Image> mImages = new HashMap<String, Image>();

	AdvertisingTab(Advertising advertising, Runnable onChange) {
		this.advertising = advertising;
		this.onChange = onChange;
		setPadding(new Insets(20));
	}

	abstract String getTitleKey();

	/**
	 * Called when the tab is shown, so changes of the other tabs are visible.
	 */
	void refresh() {
	}

	/**
	 * Saves the advertising after a change.
	 */
	protected void changed() {
		onChange.run();
	}

	/**
	 * @return the scaled down image or null if the file is missing; loaded once per file and size
	 */
	protected Image loadImage(File file, double dMaxWidth, double dMaxHeight) {
		if (!file.isFile())
			return null;
		return mImages.computeIfAbsent(file.getAbsolutePath() + "@" + dMaxWidth + "x" + dMaxHeight, key -> {
			try {
				return Images.getImages().loadImageFromPath(file.getAbsolutePath(), dMaxWidth, dMaxHeight);
			} catch (MalformedURLException e) {
				return null;
			}
		});
	}
}
