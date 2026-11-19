/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.advertisingFrame;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advertising.Advertising;
import advertising.FullscreenSpot;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import language.Language;
import message.ErrorMsg;
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
	 * Lets the user choose images and videos, copies them into the advertising folder and appends them to the
	 * full screen rotation.
	 *
	 * @param sSponsor the sponsor of the new spots or null
	 * @return the new spots, empty if nothing was chosen
	 */
	protected List<FullscreenSpot> importSpots(String sSponsor) {
		List<FullscreenSpot> lNewSpots = new ArrayList<FullscreenSpot>();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Language.getText("vollbild"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter(Language.getText("vollbild_dateien"), "*.png",
				"*.jpg", "*.mp4", "*.PNG", "*.JPG", "*.MP4"));
		List<File> files = fileChooser.showOpenMultipleDialog(getScene().getWindow());
		if (files == null)
			return lNewSpots;
		for (File file : files) {
			if (!FullscreenSpot.isSupported(file.getName()))
				continue;
			try {
				lNewSpots.add(advertising.addSpot(file, sSponsor));
			} catch (IOException e) {
				ErrorMsg.showErrorMessage(119, e.getMessage());
			}
		}
		if (!lNewSpots.isEmpty())
			changed();
		return lNewSpots;
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
