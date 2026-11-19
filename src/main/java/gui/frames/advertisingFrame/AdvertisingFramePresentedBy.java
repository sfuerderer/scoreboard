/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.advertisingFrame;

import java.util.EnumMap;
import java.util.Map;

import advertising.AdPlacement;
import advertising.Advertising;
import advertising.Sponsor;
import gui.frames.panes.ScoreboardColors;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import language.Language;

/**
 * One sponsor per event view ("Goal presented by ..."), chosen from the sponsors with a logo.
 */
class AdvertisingFramePresentedBy extends AdvertisingTab {
	// Shown in the choice for "no sponsor"
	private static final String NO_SPONSOR = "—";

	private final Map<AdPlacement, ComboBox<String>> mChoices = new EnumMap<AdPlacement, ComboBox<String>>(
			AdPlacement.class);
	private final Map<AdPlacement, ImageView> mLogos = new EnumMap<AdPlacement, ImageView>(AdPlacement.class);
	private final Map<AdPlacement, Label> mStates = new EnumMap<AdPlacement, Label>(AdPlacement.class);
	private boolean bShowing;

	AdvertisingFramePresentedBy(Advertising advertising, Runnable onChange) {
		super(advertising, onChange);

		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(12);
		// Labels keep their full width
		ColumnConstraints labelColumn = new ColumnConstraints();
		labelColumn.setMinWidth(Region.USE_PREF_SIZE);
		grid.getColumnConstraints().add(labelColumn);

		int iRow = 0;
		for (AdPlacement placement : AdPlacement.values()) {
			ComboBox<String> cbSponsor = new ComboBox<String>();
			cbSponsor.setPrefWidth(260);
			cbSponsor.setOnAction(event -> choose(placement));
			ImageView ivLogo = new ImageView();
			ivLogo.setFitWidth(80);
			ivLogo.setFitHeight(26);
			ivLogo.setPreserveRatio(true);
			StackPane logoBox = new StackPane(ivLogo);
			logoBox.setMinSize(90, 32);
			logoBox.setMaxSize(90, 32);
			// Same blue as the event views
			logoBox.setBackground(new Background(new BackgroundFill(ScoreboardColors.PANEL, null, null)));
			Label lbState = new Label();
			lbState.setOpacity(0.6);

			mChoices.put(placement, cbSponsor);
			mLogos.put(placement, ivLogo);
			mStates.put(placement, lbState);
			grid.add(new Label(Language.getText(placement.getLanguageKey())), 0, iRow);
			grid.add(cbSponsor, 1, iRow);
			grid.add(logoBox, 2, iRow);
			grid.add(lbState, 3, iRow);
			iRow++;
		}

		Label lbHint = new Label(Language.getText("praesentiert_von_hinweis"));
		lbHint.setWrapText(true);

		setCenter(new VBox(20, grid, lbHint));
		refresh();
	}

	@Override
	String getTitleKey() {
		return "praesentiert_von_tab";
	}

	@Override
	void refresh() {
		bShowing = true;
		for (AdPlacement placement : AdPlacement.values()) {
			ComboBox<String> cbSponsor = mChoices.get(placement);
			// Cleared first: refilling the list loses the shown value, and setting the same value again would
			// not show it
			cbSponsor.setValue(null);
			cbSponsor.getItems().setAll(NO_SPONSOR);
			// Only sponsors with a logo can present an event
			for (Sponsor sponsor : advertising.getConfig().lSponsors)
				if (sponsor.hasLogo() && advertising.getLogoFile(sponsor).isFile())
					cbSponsor.getItems().add(sponsor.sName);
			String sChosen = advertising.getConfig().mPresentedBy.get(placement);
			if (sChosen != null && !cbSponsor.getItems().contains(sChosen))
				cbSponsor.getItems().add(sChosen);
			cbSponsor.setValue(sChosen == null ? NO_SPONSOR : sChosen);
			showState(placement);
		}
		bShowing = false;
	}

	private void choose(AdPlacement placement) {
		if (bShowing)
			return;
		String sChoice = mChoices.get(placement).getValue();
		if (sChoice == null || sChoice.equals(NO_SPONSOR))
			advertising.getConfig().mPresentedBy.remove(placement);
		else
			advertising.getConfig().mPresentedBy.put(placement, sChoice);
		showState(placement);
		changed();
	}

	private void showState(AdPlacement placement) {
		Sponsor sponsor = advertising.getSponsor(advertising.getConfig().mPresentedBy.get(placement));
		boolean bLogo = sponsor != null && sponsor.hasLogo() && advertising.getLogoFile(sponsor).isFile();
		mLogos.get(placement).setImage(bLogo ? loadImage(advertising.getLogoFile(sponsor), 160, 52) : null);
		String sState = "";
		if (sponsor != null && !bLogo)
			sState = Language.getText("ohne_logo");
		else if (sponsor != null && !sponsor.bActive)
			sState = Language.getText("sponsor_inaktiv");
		mStates.get(placement).setText(sState);
	}
}
