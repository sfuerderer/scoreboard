/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.advertisingFrame;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import advertising.Advertising;
import advertising.FullscreenSpot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import language.Language;
import message.ErrorMsg;
import system.settings.Settings;

/**
 * Images and videos of the full screen advertising: which ones run, in which order and how long.
 */
class AdvertisingFrameFullscreen extends AdvertisingTab {
	private static final double PREVIEW_WIDTH = 257;
	private static final double PREVIEW_HEIGHT = 140;
	private static final int MAX_DURATION_IN_SECONDS = 600;

	private final Settings sSettings;
	private final ListView<FullscreenSpot> lvSpots;
	private final ImageView ivPreview;
	private final Label lbVideo;
	private final Spinner<Integer> spDuration;
	private final Button btnUp;
	private final Button btnDown;
	private boolean bShowing;

	AdvertisingFrameFullscreen(Advertising advertising, Runnable onChange, Settings sSettings) {
		super(advertising, onChange);
		this.sSettings = sSettings;

		lvSpots = new ListView<FullscreenSpot>();
		lvSpots.setPrefWidth(340);
		lvSpots.setCellFactory(listView -> new SpotCell());
		lvSpots.getItems().setAll(getSpots());
		lvSpots.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldSpot, newSpot) -> showSpot(newSpot));

		Button btnAdd = new Button(Language.getText("hinzufuegen"));
		btnAdd.setOnAction(event -> addSpot());
		btnUp = new Button("▲");
		btnUp.setOnAction(event -> move(-1));
		btnDown = new Button("▼");
		btnDown.setOnAction(event -> move(1));
		btnAdd.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(btnAdd, Priority.ALWAYS);
		HBox listButtons = new HBox(10, btnAdd, btnUp, btnDown);
		VBox left = new VBox(10, lvSpots, listButtons);
		VBox.setVgrow(lvSpots, Priority.ALWAYS);

		// Details of the selected spot
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
		Label lbPreview = new Label(Language.getText("vorschau"));
		GridPane.setValignment(lbPreview, VPos.TOP);
		details.add(lbPreview, 0, 0);
		details.add(previewBox, 1, 0);
		details.add(new Label(Language.getText("vollbild_dauer")), 0, 1);
		details.add(spDuration, 1, 1);
		details.add(lbDurationHint, 1, 2);
		details.add(lbHint, 0, 3, 2, 1);

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

	private List<FullscreenSpot> getSpots() {
		return advertising.getConfig().lFullscreenSpots;
	}

	private FullscreenSpot getSelected() {
		return lvSpots.getSelectionModel().getSelectedItem();
	}

	private void showSpot(FullscreenSpot spot) {
		bShowing = true;
		boolean bEmpty = spot == null;
		int iIndex = lvSpots.getSelectionModel().getSelectedIndex();
		btnUp.setDisable(bEmpty || iIndex <= 0);
		btnDown.setDisable(bEmpty || iIndex >= lvSpots.getItems().size() - 1);
		spDuration.getValueFactory().setValue(bEmpty ? 0 : spot.iDurationInSeconds);
		boolean bVideo = !bEmpty && spot.isVideo();
		// Videos always run as long as they are
		spDuration.setDisable(bEmpty || bVideo);
		lbVideo.setVisible(bVideo);
		ivPreview.setImage(bEmpty || bVideo ? null
				: loadImage(advertising.getSpotFile(spot), PREVIEW_WIDTH, PREVIEW_HEIGHT));
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

	private void addSpot() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Language.getText("vollbild"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter(Language.getText("vollbild_dateien"), "*.png",
				"*.jpg", "*.mp4", "*.PNG", "*.JPG", "*.MP4"));
		List<File> files = fileChooser.showOpenMultipleDialog(getScene().getWindow());
		if (files == null)
			return;
		FullscreenSpot lastSpot = null;
		for (File file : files) {
			if (!FullscreenSpot.isSupported(file.getName()))
				continue;
			try {
				String sName = Advertising.importFile(file, advertising.getFolder());
				lastSpot = new FullscreenSpot(sName, true, 0);
				getSpots().add(lastSpot);
			} catch (IOException e) {
				ErrorMsg.showErrorMessage(119, e.getMessage());
			}
		}
		if (lastSpot == null)
			return;
		lvSpots.getItems().setAll(getSpots());
		lvSpots.getSelectionModel().select(lastSpot);
		changed();
	}

	private class SpotCell extends ListCell<FullscreenSpot> {
		private final CheckBox cbActive = new CheckBox();
		private final Label lbName = new Label();
		private final Label lbInfo = new Label();
		private final HBox content = new HBox(10, cbActive, lbName, lbInfo);

		SpotCell() {
			content.setAlignment(Pos.CENTER_LEFT);
			lbInfo.setOpacity(0.6);
			cbActive.setOnAction(event -> {
				FullscreenSpot spot = getItem();
				if (spot == null)
					return;
				spot.bActive = cbActive.isSelected();
				lbName.setOpacity(spot.bActive ? 1 : 0.5);
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
			cbActive.setSelected(spot.bActive);
			lbName.setText(spot.sFile);
			lbName.setOpacity(spot.bActive ? 1 : 0.5);
			String sDuration = spot.isVideo() ? Language.getText("vollbild_video")
					: spot.iDurationInSeconds > 0 ? spot.iDurationInSeconds + " s"
							: sSettings.iTimeBetweenAdvertisingInMiliseconds / 1000 + " s";
			lbInfo.setText(sDuration);
			setGraphic(content);
		}
	}
}
