/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes.components;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advertising.Advertising;
import advertising.MainViewRotation;
import advertising.Sponsor;
import gui.frames.panes.ScoreboardColors;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import system.Images;

/**
 * Bar with the clock at the bottom of the main view. If sponsors are booked for the main view, their logos are
 * shown left and right of the clock and change page by page; without sponsors the bar only shows the clock.
 */
public class ClockBar extends StackPane {
	private static final double LOGO_HEIGHT = 44;
	private static final double LOGO_GAP = 12;
	private static final double SIDE_PADDING = 12;
	// Space between the clock and the logos next to it
	private static final double CLOCK_GAP = 20;
	private static final Duration FADE = Duration.millis(400);
	// Logos are decoded larger than shown, because the wall scales the layout up
	private static final double DECODE_SCALE = 3;

	private final Node clock;
	private final Map<String, Image> mLogos = new HashMap<String, Image>();
	private HBox hbLeft;
	private HBox hbRight;
	private List<Sponsor> lSponsors;
	private int iPage;
	private Timeline rotation;
	private boolean bRunning;

	// Kept as a field: the advertising lives longer than this bar, so it only holds a weak reference
	private final ChangeListener<Number> advertisingListener = (observable, oldValue, newValue) -> showSponsors();

	public ClockBar(Node clock, double dHeight) {
		super(clock);
		this.clock = clock;
		setBackground(new Background(new BackgroundFill(ScoreboardColors.PANEL, null, null)));
		setMinHeight(dHeight);
		setPrefHeight(dHeight);

		Advertising.getAdvertising().changesProperty().addListener(new WeakChangeListener<Number>(advertisingListener));
		showSponsors();
	}

	/**
	 * Starts changing the logos; called when the view appears.
	 */
	public void start() {
		bRunning = true;
		startRotation();
	}

	/**
	 * Stops changing the logos; called when the view disappears, otherwise the timer would keep it alive.
	 */
	public void stop() {
		bRunning = false;
		stopRotation();
	}

	private void showSponsors() {
		stopRotation();
		lSponsors = Advertising.getAdvertising().getMainViewSponsors();
		iPage = 0;
		if (lSponsors.isEmpty()) {
			// Exactly the bar without sponsors
			getChildren().setAll(clock);
			return;
		}
		hbLeft = createSide(Pos.CENTER_RIGHT);
		hbRight = createSide(Pos.CENTER_LEFT);
		HBox row = new HBox(CLOCK_GAP, hbLeft, clock, hbRight);
		row.setAlignment(Pos.CENTER);
		row.setPadding(new Insets(0, SIDE_PADDING, 0, SIDE_PADDING));
		getChildren().setAll(row);
		showPage();
		if (bRunning)
			startRotation();
	}

	private static HBox createSide(Pos alignment) {
		HBox side = new HBox(LOGO_GAP);
		side.setAlignment(Pos.CENTER);
		// Both sides get the same width, so the clock stays in the middle
		side.setPrefWidth(0);
		side.setMinWidth(0);
		HBox.setHgrow(side, Priority.ALWAYS);
		return side;
	}

	private void showPage() {
		int iMaxLogos = Advertising.getAdvertising().getConfig().iMainViewMaxLogos;
		List<Sponsor> lPage = MainViewRotation.getPage(lSponsors, iMaxLogos, iPage);
		int iLeft = MainViewRotation.getLeftCount(lPage.size());
		// The logos of a side share its width; fewer logos are shown larger
		int iSlotsPerSide = Math.max(1, MainViewRotation.getLeftCount(Math.min(iMaxLogos, lSponsors.size())));
		hbLeft.getChildren().clear();
		hbRight.getChildren().clear();
		for (int i = 0; i < lPage.size(); i++)
			(i < iLeft ? hbLeft : hbRight).getChildren().add(createLogo(lPage.get(i), i < iLeft ? hbLeft : hbRight,
					iSlotsPerSide));
	}

	private ImageView createLogo(Sponsor sponsor, HBox side, int iSlotsPerSide) {
		ImageView ivLogo = new ImageView(loadLogo(sponsor));
		ivLogo.setPreserveRatio(true);
		ivLogo.setSmooth(true);
		ivLogo.setFitHeight(LOGO_HEIGHT);
		ivLogo.fitWidthProperty().bind(side.widthProperty().subtract(LOGO_GAP * (iSlotsPerSide - 1))
				.divide(iSlotsPerSide));
		return ivLogo;
	}

	private Image loadLogo(Sponsor sponsor) {
		File file = Advertising.getAdvertising().getLogoFile(sponsor);
		return mLogos.computeIfAbsent(file.getAbsolutePath(), key -> {
			try {
				return Images.getImages().loadImageFromPath(key, 200 * DECODE_SCALE, LOGO_HEIGHT * DECODE_SCALE);
			} catch (MalformedURLException e) {
				return null;
			}
		});
	}

	private void startRotation() {
		int iMaxLogos = Advertising.getAdvertising().getConfig().iMainViewMaxLogos;
		if (rotation != null || lSponsors.isEmpty()
				|| MainViewRotation.getPageCount(lSponsors.size(), iMaxLogos) < 2)
			return;
		int iSeconds = Advertising.getAdvertising().getConfig().iMainViewRotationSeconds;
		rotation = new Timeline(new KeyFrame(Duration.seconds(iSeconds), event -> nextPage()));
		rotation.setCycleCount(Timeline.INDEFINITE);
		rotation.play();
	}

	private void stopRotation() {
		if (rotation != null) {
			rotation.stop();
			rotation = null;
		}
	}

	private void nextPage() {
		ParallelTransition fadeOut = new ParallelTransition(fade(hbLeft, 1, 0), fade(hbRight, 1, 0));
		fadeOut.setOnFinished(event -> {
			iPage++;
			showPage();
		});
		new SequentialTransition(fadeOut, new ParallelTransition(fade(hbLeft, 0, 1), fade(hbRight, 0, 1))).play();
	}

	private static FadeTransition fade(Node node, double dFrom, double dTo) {
		FadeTransition fade = new FadeTransition(FADE.divide(2), node);
		fade.setFromValue(dFrom);
		fade.setToValue(dTo);
		return fade;
	}
}
