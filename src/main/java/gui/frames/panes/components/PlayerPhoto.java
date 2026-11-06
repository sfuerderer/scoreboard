/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import java.net.MalformedURLException;

import gui.frames.panes.ScoreboardColors;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import system.Images;

/**
 * Photo of a player or coach for the scoreboard.
 * <ul>
 * <li>Cut-out photos (transparent background, see tools/freistellen) stand directly on the club blue with a
 * light glow behind them, so a dark shirt does not blend into the background.</li>
 * <li>Other photos (e.g. studio photos with grey background) are shown as a card with a slanted edge like the
 * bar in the crest.</li>
 * </ul>
 */
public final class PlayerPhoto {
	// A corner pixel more transparent than this means the photo was cut out
	private static final double CUT_OUT_ALPHA = 0.05;
	// Share of the card width cut away at the top by the slanted edge
	private static final double SLANT = 0.26;
	private static final double EDGE_WIDTH = 12;
	// Width of the photo card relative to its height
	private static final double CARD_RATIO = 0.8;
	private static final String STYLE_CARD = "player-photo-card";

	private PlayerPhoto() {
	}

	/**
	 * @return the photo in the given height, null if there is no photo or it cannot be loaded
	 */
	public static Region create(String sPathToImage, double dHeight) {
		return create(sPathToImage, dHeight, dHeight);
	}

	/**
	 * @param dCutOutHeight height of a cut-out photo; it may reach beyond the wall, the bottom is cut off
	 * @param dCardHeight   height of a photo card, which should end at the edge of the free area
	 * @return the photo, null if there is no photo or it cannot be loaded
	 */
	public static Region create(String sPathToImage, double dCutOutHeight, double dCardHeight) {
		if (sPathToImage == null)
			return null;
		Image image;
		try {
			image = Images.getImages().loadImageFromPath(sPathToImage);
		} catch (MalformedURLException e) {
			return null;
		}
		if (image.isError() || image.getWidth() <= 0 || image.getHeight() <= 0)
			return null;
		return isCutOut(image) ? cutOut(image, dCutOutHeight) : card(image, dCardHeight);
	}

	/**
	 * Whether the photo is shown as a card (not cut out). Cards stand at the edge, cut-out photos need some room
	 * for the glow.
	 */
	public static boolean isCard(Region photo) {
		return photo != null && photo.getStyleClass().contains(STYLE_CARD);
	}

	static boolean isCutOut(Image image) {
		PixelReader reader = image.getPixelReader();
		if (reader == null)
			return false;
		int iRight = (int) image.getWidth() - 1;
		return reader.getColor(0, 0).getOpacity() < CUT_OUT_ALPHA
				&& reader.getColor(iRight, 0).getOpacity() < CUT_OUT_ALPHA;
	}

	private static Region cutOut(Image image, double dHeight) {
		double dWidth = image.getWidth() * dHeight / image.getHeight();
		ImageView ivPhoto = new ImageView(image);
		ivPhoto.setFitHeight(dHeight);
		ivPhoto.setPreserveRatio(true);
		ivPhoto.setSmooth(true);

		double dRadius = dHeight * 0.55;
		Circle glow = new Circle(dWidth / 2, dHeight * 0.5, dRadius);
		glow.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
				new Stop(0, Color.rgb(150, 175, 255, 0.55)),
				new Stop(0.45, Color.rgb(130, 160, 255, 0.18)),
				new Stop(0.68, Color.rgb(130, 160, 255, 0))));

		return fixedSize(new Pane(glow, ivPhoto), dWidth, dHeight);
	}

	private static Region card(Image image, double dHeight) {
		double dWidth = dHeight * CARD_RATIO;
		// Fill the card like CSS "cover", keeping the face in the upper part
		double dScale = Math.max(dWidth / image.getWidth(), dHeight / image.getHeight());
		double dViewWidth = dWidth / dScale;
		double dViewHeight = dHeight / dScale;
		ImageView ivPhoto = new ImageView(image);
		ivPhoto.setViewport(new Rectangle2D((image.getWidth() - dViewWidth) / 2,
				(image.getHeight() - dViewHeight) * 0.24, dViewWidth, dViewHeight));
		ivPhoto.setFitWidth(dWidth);
		ivPhoto.setFitHeight(dHeight);
		ivPhoto.setSmooth(true);
		ivPhoto.setClip(slanted(0, dWidth, dHeight));

		Polygon edge = slanted(-EDGE_WIDTH, dWidth, dHeight);
		edge.setFill(ScoreboardColors.TEXT);

		Region card = fixedSize(new Pane(edge, ivPhoto), dWidth, dHeight);
		card.getStyleClass().add(STYLE_CARD);
		return card;
	}

	private static Polygon slanted(double dOffset, double dWidth, double dHeight) {
		return new Polygon(SLANT * dWidth + dOffset, 0, dWidth, 0, dWidth, dHeight, dOffset, dHeight);
	}

	private static Region fixedSize(Pane pane, double dWidth, double dHeight) {
		pane.setMinSize(dWidth, dHeight);
		pane.setPrefSize(dWidth, dHeight);
		pane.setMaxSize(dWidth, dHeight);
		return pane;
	}
}
