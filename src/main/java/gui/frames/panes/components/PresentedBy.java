/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import java.net.MalformedURLException;

import advertising.Advertising;
import advertising.Sponsor;
import gui.frames.panes.ScoreboardColors;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.FontWeight;
import language.Language;
import system.Images;

/**
 * Small badge "PRESENTED BY [logo]" for a sponsor of an event view.
 */
public final class PresentedBy {
	private static final double LOGO_HEIGHT = 26;
	private static final double LOGO_MAX_WIDTH = 120;
	// Logos are decoded larger than shown, because the wall scales the layout up
	private static final double DECODE_SCALE = 3;

	private PresentedBy() {
	}

	public static HBox create(Sponsor sponsor) {
		Label lbText = ScoreboardText.label(Language.getText("praesentiert_von").toUpperCase(), FontWeight.SEMI_BOLD,
				13, ScoreboardColors.TEXT_DIM);
		ImageView ivLogo = new ImageView();
		try {
			ivLogo.setImage(Images.getImages().loadImageFromPath(
					Advertising.getAdvertising().getLogoFile(sponsor).getAbsolutePath(),
					LOGO_MAX_WIDTH * DECODE_SCALE, LOGO_HEIGHT * DECODE_SCALE));
		} catch (MalformedURLException e) {
			// Without the logo the text alone would look odd, but the sponsor is checked to have one
		}
		ivLogo.setFitHeight(LOGO_HEIGHT);
		ivLogo.setFitWidth(LOGO_MAX_WIDTH);
		ivLogo.setPreserveRatio(true);
		ivLogo.setSmooth(true);
		HBox badge = new HBox(8, lbText, ivLogo);
		badge.setAlignment(Pos.CENTER_RIGHT);
		badge.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		return badge;
	}
}
