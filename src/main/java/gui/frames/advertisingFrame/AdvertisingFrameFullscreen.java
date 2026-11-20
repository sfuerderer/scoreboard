/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.advertisingFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import advertising.Advertising;
import advertising.FullscreenSpot;
import advertising.Sponsor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import language.Language;
import message.ErrorMsg;
import system.settings.Settings;

/**
 * The full screen ads: a list of media chosen from the sponsors, with order, duration and on/off.
 */
class AdvertisingFrameFullscreen extends AdvertisingTab {
	private static final double PREVIEW_WIDTH = 257;
	private static final double PREVIEW_HEIGHT = 140;
	private static final int MAX_DURATION_IN_SECONDS = 600;

	private final Settings sSettings;
	private final ListView<FullscreenSpot> lvSpots;
	private final ImageView ivPreview;
	private final Label lbVideo;
	private final Label lbSponsor;
	private final Spinner<Integer> spDuration;
	private final ComboBox<String> cbAssign;
	private final Label lbAssign;
	private final Button btnUp;
	private final Button btnDown;
	private final Button btnRemove;
	private boolean bShowing;
	// Sponsor chosen in the last add dialog, preselected the next time
	private String lastSponsor;

	AdvertisingFrameFullscreen(Advertising advertising, Runnable onChange, Settings sSettings) {
		super(advertising, onChange);
		this.sSettings = sSettings;

		lvSpots = new ListView<FullscreenSpot>();
		lvSpots.setPrefWidth(380);
		lvSpots.setCellFactory(listView -> new SpotCell());
		lvSpots.getItems().setAll(getSpots());
		lvSpots.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldSpot, newSpot) -> showSpot(newSpot));

		Button btnAdd = new Button(Language.getText("vollbild_hinzufuegen"));
		btnAdd.setOnAction(event -> addSpots());
		btnAdd.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(btnAdd, Priority.ALWAYS);
		btnUp = new Button("▲");
		btnUp.setOnAction(event -> move(-1));
		btnDown = new Button("▼");
		btnDown.setOnAction(event -> move(1));
		btnRemove = new Button(Language.getText("vollbild_entfernen"));
		btnRemove.setOnAction(event -> removeSpot());
		HBox listButtons = new HBox(10, btnAdd, btnUp, btnDown, btnRemove);
		VBox left = new VBox(10, lvSpots, listButtons);
		VBox.setVgrow(lvSpots, Priority.ALWAYS);

		// Details of the selected entry
		lbSponsor = new Label();
		lbSponsor.setFont(Font.font(null, FontWeight.BOLD, 14));

		ivPreview = new ImageView();
		ivPreview.setFitWidth(PREVIEW_WIDTH);
		ivPreview.setFitHeight(PREVIEW_HEIGHT);
		ivPreview.setPreserveRatio(true);
		lbVideo = new Label(Language.getText("vollbild_video"));
		lbVideo.setTextFill(Color.WHITE);
		StackPane previewBox = new StackPane(ivPreview, lbVideo);
		previewBox.setMinSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
		previewBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		previewBox.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		spDuration = new Spinner<Integer>(0, MAX_DURATION_IN_SECONDS, 0);
		spDuration.setEditable(true);
		spDuration.setPrefWidth(100);
		spDuration.valueProperty().addListener((observable, oldValue, newValue) -> saveDuration());
		// Typed values are only taken over by the spinner when leaving the field
		spDuration.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
			if (!isFocused)
				commitTypedDuration();
		});
		spDuration.getEditor().setOnAction(event -> commitTypedDuration());
		Label lbDurationHint = new Label(Language.getText("vollbild_dauer_hinweis")
				.replace("%s", String.valueOf(sSettings.iTimeBetweenAdvertisingInMiliseconds / 1000)));
		lbDurationHint.setWrapText(true);

		// Only for ads without a sponsor from older installations
		lbAssign = new Label(Language.getText("sponsor_zuordnen"));
		cbAssign = new ComboBox<String>();
		cbAssign.setPromptText(Language.getText("sponsor_waehlen"));
		cbAssign.setOnAction(event -> assignSponsor());

		Label lbHint = new Label(Language.getText("vollbild_hinweis"));
		lbHint.setWrapText(true);

		GridPane details = new GridPane();
		details.setHgap(20);
		details.setVgap(15);
		details.setPadding(new Insets(0, 0, 0, 20));
		// Labels keep their full width, the space is taken from the right column
		ColumnConstraints labelColumn = new ColumnConstraints();
		labelColumn.setMinWidth(Region.USE_PREF_SIZE);
		details.getColumnConstraints().add(labelColumn);
		details.add(new Label(Language.getText("sponsor")), 0, 0);
		details.add(lbSponsor, 1, 0);
		Label lbPreview = new Label(Language.getText("vorschau"));
		GridPane.setValignment(lbPreview, VPos.TOP);
		details.add(lbPreview, 0, 1);
		details.add(previewBox, 1, 1);
		details.add(new Label(Language.getText("vollbild_dauer")), 0, 2);
		details.add(spDuration, 1, 2);
		details.add(lbDurationHint, 1, 3);
		details.add(lbAssign, 0, 4);
		details.add(cbAssign, 1, 4);
		details.add(lbHint, 0, 5, 2, 1);

		setLeft(left);
		setCenter(details);

		if (!lvSpots.getItems().isEmpty())
			lvSpots.getSelectionModel().selectFirst();
		else
			showSpot(null);
	}

	@Override
	String getTitleKey() {
		return "vollbild";
	}

	@Override
	void refresh() {
		FullscreenSpot selected = getSelected();
		lvSpots.getItems().setAll(getSpots());
		if (selected != null && getSpots().contains(selected))
			lvSpots.getSelectionModel().select(selected);
		else
			lvSpots.getSelectionModel().selectFirst();
		showSpot(getSelected());
	}

	private List<FullscreenSpot> getSpots() {
		return advertising.getConfig().lFullscreenSpots;
	}

	private FullscreenSpot getSelected() {
		return lvSpots.getSelectionModel().getSelectedItem();
	}

	private String getSponsorText(FullscreenSpot spot) {
		Sponsor sponsor = advertising.getSponsor(spot.sSponsor);
		if (sponsor == null)
			return Language.getText("ohne_sponsor");
		return sponsor.bActive ? sponsor.sName
				: sponsor.sName + " (" + Language.getText("sponsor_inaktiv") + ")";
	}

	private void showSpot(FullscreenSpot spot) {
		bShowing = true;
		boolean bEmpty = spot == null;
		int iIndex = lvSpots.getSelectionModel().getSelectedIndex();
		btnUp.setDisable(bEmpty || iIndex <= 0);
		btnDown.setDisable(bEmpty || iIndex >= lvSpots.getItems().size() - 1);
		btnRemove.setDisable(bEmpty);
		lbSponsor.setText(bEmpty ? "" : getSponsorText(spot));

		boolean bVideo = !bEmpty && spot.isVideo();
		// Videos always run as long as they are
		spDuration.setDisable(bEmpty || bVideo);
		spDuration.getValueFactory().setValue(bEmpty ? 0 : spot.iDurationInSeconds);
		lbVideo.setVisible(bVideo);
		ivPreview.setImage(bEmpty || bVideo ? null
				: loadImage(advertising.getSpotFile(spot), PREVIEW_WIDTH, PREVIEW_HEIGHT));

		boolean bWithoutSponsor = !bEmpty && advertising.getSponsor(spot.sSponsor) == null;
		lbAssign.setVisible(bWithoutSponsor);
		cbAssign.setVisible(bWithoutSponsor);
		List<String> lNames = new ArrayList<String>();
		for (Sponsor sponsor : advertising.getConfig().lSponsors)
			lNames.add(sponsor.sName);
		cbAssign.getItems().setAll(lNames);
		cbAssign.setValue(null);
		bShowing = false;
	}

	private void commitTypedDuration() {
		try {
			int iValue = Integer.parseInt(spDuration.getEditor().getText().trim());
			spDuration.getValueFactory().setValue(Math.max(0, Math.min(MAX_DURATION_IN_SECONDS, iValue)));
		} catch (NumberFormatException e) {
			ErrorMsg.showErrorMessage(205);
		}
		spDuration.getEditor().setText(String.valueOf(spDuration.getValue()));
	}

	private void saveDuration() {
		FullscreenSpot spot = getSelected();
		if (spot == null || bShowing || spDuration.getValue() == spot.iDurationInSeconds)
			return;
		spot.iDurationInSeconds = spDuration.getValue();
		lvSpots.refresh();
		changed();
	}

	private void assignSponsor() {
		FullscreenSpot spot = getSelected();
		String sSponsor = cbAssign.getValue();
		if (spot == null || bShowing || sSponsor == null)
			return;
		try {
			advertising.moveSpot(spot, sSponsor);
		} catch (IOException e) {
			ErrorMsg.showErrorMessage(121, e.getMessage());
			return;
		}
		lvSpots.refresh();
		showSpot(spot);
		changed();
	}

	private void move(int iDirection) {
		int iIndex = lvSpots.getSelectionModel().getSelectedIndex();
		int iNewIndex = iIndex + iDirection;
		if (iIndex < 0 || iNewIndex < 0 || iNewIndex >= getSpots().size())
			return;
		Collections.swap(getSpots(), iIndex, iNewIndex);
		lvSpots.getItems().setAll(getSpots());
		lvSpots.getSelectionModel().select(iNewIndex);
		changed();
	}

	/**
	 * Removes the entry from the full screen ads; the file stays a medium of the sponsor.
	 */
	private void removeSpot() {
		FullscreenSpot spot = getSelected();
		if (spot == null)
			return;
		int iIndex = lvSpots.getSelectionModel().getSelectedIndex();
		getSpots().remove(spot);
		lvSpots.getItems().setAll(getSpots());
		lvSpots.getSelectionModel().select(Math.min(iIndex, getSpots().size() - 1));
		changed();
	}

	private void addSpots() {
		List<Sponsor> lWithMedia = new ArrayList<Sponsor>();
		for (Sponsor sponsor : advertising.getConfig().lSponsors)
			if (!advertising.getMedia(sponsor).isEmpty())
				lWithMedia.add(sponsor);
		if (lWithMedia.isEmpty()) {
			ErrorMsg.showErrorMessage(215);
			return;
		}
		Optional<List<String>> result = new AddDialog(lWithMedia).showAndWait();
		if (result.isEmpty() || result.get().isEmpty())
			return;
		// The dialog remembers the chosen sponsor
		Sponsor sponsor = advertising.getSponsor(lastSponsor);
		FullscreenSpot lastSpot = null;
		for (String sPath : result.get())
			lastSpot = advertising.addToFullscreen(sponsor, sPath);
		lvSpots.getItems().setAll(getSpots());
		lvSpots.getSelectionModel().select(lastSpot);
		changed();
	}

	/**
	 * Lets the user pick a sponsor and some of its media.
	 */
	private class AddDialog extends Dialog<List<String>> {
		private final ComboBox<String> cbSponsor = new ComboBox<String>();
		private final VBox vbMedia = new VBox(8);
		private final List<CheckBox> lChecks = new ArrayList<CheckBox>();

		AddDialog(List<Sponsor> lSponsors) {
			initOwner(getScene().getWindow());
			setTitle(Language.getText("vollbild_hinzufuegen"));
			setHeaderText(Language.getText("vollbild_auswahl"));
			ButtonType btnAdd = new ButtonType(Language.getText("hinzufuegen"), ButtonData.OK_DONE);
			getDialogPane().getButtonTypes().addAll(btnAdd,
					new ButtonType(Language.getText("abbrechen"), ButtonData.CANCEL_CLOSE));

			for (Sponsor sponsor : lSponsors)
				cbSponsor.getItems().add(sponsor.sName);
			cbSponsor.setOnAction(event -> showMedia());
			String sStart = lastSponsor != null && cbSponsor.getItems().contains(lastSponsor) ? lastSponsor
					: cbSponsor.getItems().get(0);
			cbSponsor.setValue(sStart);

			ScrollPane spMedia = new ScrollPane(vbMedia);
			spMedia.setFitToWidth(true);
			spMedia.setPrefSize(380, 220);
			GridPane content = new GridPane();
			content.setHgap(15);
			content.setVgap(15);
			content.add(new Label(Language.getText("sponsor")), 0, 0);
			content.add(cbSponsor, 1, 0);
			content.add(spMedia, 0, 1, 2, 1);
			getDialogPane().setContent(content);
			showMedia();

			setResultConverter(buttonType -> {
				List<String> lChosen = new ArrayList<String>();
				if (buttonType != btnAdd)
					return lChosen;
				lastSponsor = cbSponsor.getValue();
				for (CheckBox cb : lChecks)
					if (cb.isSelected() && !cb.isDisabled())
						lChosen.add((String) cb.getUserData());
				return lChosen;
			});
		}

		private void showMedia() {
			Sponsor sponsor = advertising.getSponsor(cbSponsor.getValue());
			lChecks.clear();
			vbMedia.getChildren().clear();
			for (String sPath : advertising.getMedia(sponsor)) {
				FullscreenSpot preview = new FullscreenSpot(sPath, true, 0);
				CheckBox cb = new CheckBox(preview.getFileName());
				cb.setUserData(sPath);
				ImageView iv = new ImageView(preview.isVideo() ? null
						: loadImage(advertising.getFile(sPath), 96, 52));
				StackPane thumbnail = new StackPane(iv);
				thumbnail.setMinSize(96, 52);
				thumbnail.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
				if (preview.isVideo())
					thumbnail.getChildren().add(whiteLabel(Language.getText("vollbild_video")));
				if (advertising.isInFullscreen(sPath)) {
					// Each medium is in the list once
					cb.setSelected(true);
					cb.setDisable(true);
					cb.setText(cb.getText() + " (" + Language.getText("bereits_enthalten") + ")");
				} else
					cb.setSelected(true);
				lChecks.add(cb);
				HBox row = new HBox(10, thumbnail, cb);
				row.setAlignment(Pos.CENTER_LEFT);
				vbMedia.getChildren().add(row);
			}
		}

		private Label whiteLabel(String sText) {
			Label lb = new Label(sText);
			lb.setTextFill(Color.WHITE);
			return lb;
		}
	}

	private class SpotCell extends ListCell<FullscreenSpot> {
		private final CheckBox cbActive = new CheckBox();
		private final Label lbSponsorName = new Label();
		private final Label lbName = new Label();
		private final Label lbInfo = new Label();
		private final HBox content = new HBox(10, cbActive, lbSponsorName, lbName, lbInfo);

		SpotCell() {
			content.setAlignment(Pos.CENTER_LEFT);
			lbSponsorName.setFont(Font.font(null, FontWeight.BOLD, 12));
			lbSponsorName.setMinWidth(0);
			lbName.setMinWidth(Region.USE_PREF_SIZE);
			lbInfo.setMinWidth(Region.USE_PREF_SIZE);
			lbInfo.setOpacity(0.6);
			// Long names are shortened instead of making the list scroll sideways
			setPrefWidth(0);
			cbActive.setOnAction(event -> {
				FullscreenSpot spot = getItem();
				if (spot == null)
					return;
				spot.bActive = cbActive.isSelected();
				updateItem(spot, false);
				changed();
			});
		}

		@Override
		protected void updateItem(FullscreenSpot spot, boolean bEmpty) {
			super.updateItem(spot, bEmpty);
			if (bEmpty || spot == null) {
				setGraphic(null);
				return;
			}
			Sponsor sponsor = advertising.getSponsor(spot.sSponsor);
			cbActive.setSelected(spot.bActive);
			lbSponsorName.setText(getSponsorText(spot));
			lbName.setText(spot.getFileName());
			lbInfo.setText(spot.isVideo() ? Language.getText("vollbild_video")
					: (spot.iDurationInSeconds > 0 ? spot.iDurationInSeconds
							: sSettings.iTimeBetweenAdvertisingInMiliseconds / 1000) + " s");
			content.setOpacity(spot.bActive && (sponsor == null || sponsor.bActive) ? 1 : 0.5);
			setGraphic(content);
		}
	}
}
