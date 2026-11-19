/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.advertisingFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import advertising.Advertising;
import advertising.AdvertisingConfig;
import advertising.MainViewRotation;
import advertising.Sponsor;
import gui.frames.panes.ScoreboardColors;
import gui.frames.panes.components.ClockBar;
import gui.frames.panes.components.FixedDigitsLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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
import javafx.scene.text.FontWeight;
import language.Language;
import system.Defaults;
import system.Main;

/**
 * Sponsors whose logos are shown in the clock bar of the main view, with the number of logos at the same time
 * and how often they change.
 */
class AdvertisingFrameMainView extends AdvertisingTab {
	private static final int MIN_ROTATION_SECONDS = 3;
	private static final int MAX_ROTATION_SECONDS = 120;
	// Same height as the clock bar of the main view
	private static final double CLOCK_BAR_HEIGHT = 64;
	private static final double PREVIEW_SCALE = 0.8;

	private final ListView<String> lvSponsors;
	private final Spinner<Integer> spMaxLogos;
	private final Spinner<Integer> spRotation;
	private final Label lbInfo;
	private final Button btnUp;
	private final Button btnDown;
	private final Button btnRemove;
	private boolean bShowing;

	AdvertisingFrameMainView(Advertising advertising, Runnable onChange) {
		super(advertising, onChange);

		lvSponsors = new ListView<String>();
		lvSponsors.setPrefWidth(300);
		lvSponsors.setCellFactory(listView -> new SponsorCell());
		lvSponsors.getSelectionModel().selectedIndexProperty()
				.addListener((observable, oldIndex, newIndex) -> updateButtons());

		Button btnAdd = new Button(Language.getText("vollbild_hinzufuegen"));
		btnAdd.setOnAction(event -> addSponsors());
		btnAdd.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(btnAdd, Priority.ALWAYS);
		btnUp = new Button("▲");
		btnUp.setOnAction(event -> move(-1));
		btnDown = new Button("▼");
		btnDown.setOnAction(event -> move(1));
		btnRemove = new Button(Language.getText("vollbild_entfernen"));
		btnRemove.setOnAction(event -> removeSponsor());
		HBox listButtons = new HBox(10, btnAdd, btnUp, btnDown, btnRemove);
		VBox left = new VBox(10, lvSponsors, listButtons);
		VBox.setVgrow(lvSponsors, Priority.ALWAYS);

		spMaxLogos = createSpinner(1, AdvertisingConfig.MAIN_VIEW_MAX_LOGOS_LIMIT, getConfig().iMainViewMaxLogos);
		spMaxLogos.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (bShowing || newValue == getConfig().iMainViewMaxLogos)
				return;
			getConfig().iMainViewMaxLogos = newValue;
			changedMainView();
		});
		spRotation = createSpinner(MIN_ROTATION_SECONDS, MAX_ROTATION_SECONDS,
				Math.max(MIN_ROTATION_SECONDS, getConfig().iMainViewRotationSeconds));
		spRotation.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (bShowing || newValue == getConfig().iMainViewRotationSeconds)
				return;
			getConfig().iMainViewRotationSeconds = newValue;
			changedMainView();
		});

		lbInfo = new Label();
		lbInfo.setWrapText(true);

		// The real clock bar, so the preview shows exactly what the wall shows (first page)
		FixedDigitsLabel lbClock = new FixedDigitsLabel("67:12", Main.getMainFont(FontWeight.BOLD, 50),
				ScoreboardColors.TEXT);
		ClockBar preview = new ClockBar(lbClock, CLOCK_BAR_HEIGHT);
		preview.setMinSize(Defaults.SCOREBOARD_DESIGN_WIDTH, CLOCK_BAR_HEIGHT);
		preview.setMaxSize(Defaults.SCOREBOARD_DESIGN_WIDTH, CLOCK_BAR_HEIGHT);
		preview.setScaleX(PREVIEW_SCALE);
		preview.setScaleY(PREVIEW_SCALE);
		Group previewGroup = new Group(preview);

		Label lbHint = new Label(Language.getText("hauptansicht_hinweis"));
		lbHint.setWrapText(true);

		GridPane details = new GridPane();
		details.setHgap(20);
		details.setVgap(15);
		details.setPadding(new Insets(0, 0, 0, 20));
		// Labels keep their full width, the space is taken from the right column
		ColumnConstraints labelColumn = new ColumnConstraints();
		labelColumn.setMinWidth(Region.USE_PREF_SIZE);
		details.getColumnConstraints().add(labelColumn);
		details.add(new Label(Language.getText("hauptansicht_max_logos")), 0, 0);
		details.add(spMaxLogos, 1, 0);
		details.add(new Label(Language.getText("hauptansicht_wechsel")), 0, 1);
		details.add(spRotation, 1, 1);
		details.add(lbInfo, 0, 2, 2, 1);
		Label lbPreview = new Label(Language.getText("vorschau"));
		GridPane.setValignment(lbPreview, VPos.TOP);
		details.add(lbPreview, 0, 3, 2, 1);
		details.add(previewGroup, 0, 4, 2, 1);
		details.add(lbHint, 0, 5, 2, 1);

		setLeft(left);
		setCenter(details);
		refresh();
	}

	@Override
	String getTitleKey() {
		return "hauptansicht";
	}

	@Override
	void refresh() {
		bShowing = true;
		int iSelected = lvSponsors.getSelectionModel().getSelectedIndex();
		lvSponsors.getItems().setAll(getConfig().lMainViewSponsors);
		if (iSelected >= 0 && iSelected < lvSponsors.getItems().size())
			lvSponsors.getSelectionModel().select(iSelected);
		spMaxLogos.getValueFactory().setValue(getConfig().iMainViewMaxLogos);
		spRotation.getValueFactory().setValue(Math.max(MIN_ROTATION_SECONDS, getConfig().iMainViewRotationSeconds));
		bShowing = false;
		updateButtons();
		updateInfo();
	}

	private AdvertisingConfig getConfig() {
		return advertising.getConfig();
	}

	private Spinner<Integer> createSpinner(int iMin, int iMax, int iValue) {
		Spinner<Integer> spinner = new Spinner<Integer>(iMin, iMax, iValue);
		spinner.setEditable(true);
		spinner.setPrefWidth(100);
		// Typed values are only taken over by the spinner when leaving the field
		Runnable commit = () -> {
			try {
				int iTyped = Integer.parseInt(spinner.getEditor().getText().trim());
				spinner.getValueFactory().setValue(Math.max(iMin, Math.min(iMax, iTyped)));
			} catch (NumberFormatException e) {
				// Keeps the last valid value
			}
			spinner.getEditor().setText(String.valueOf(spinner.getValue()));
		};
		spinner.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
			if (!isFocused)
				commit.run();
		});
		spinner.getEditor().setOnAction(event -> commit.run());
		return spinner;
	}

	private void changedMainView() {
		lvSponsors.getItems().setAll(getConfig().lMainViewSponsors);
		updateButtons();
		updateInfo();
		changed();
	}

	private void updateButtons() {
		int iIndex = lvSponsors.getSelectionModel().getSelectedIndex();
		btnUp.setDisable(iIndex <= 0);
		btnDown.setDisable(iIndex < 0 || iIndex >= lvSponsors.getItems().size() - 1);
		btnRemove.setDisable(iIndex < 0);
	}

	private void updateInfo() {
		int iShown = advertising.getMainViewSponsors().size();
		int iMax = getConfig().iMainViewMaxLogos;
		int iPages = MainViewRotation.getPageCount(iShown, iMax);
		String sInfo;
		if (iShown == 0)
			sInfo = Language.getText("hauptansicht_info_leer");
		else if (iPages <= 1)
			sInfo = Language.getText("hauptansicht_info_fest");
		else
			sInfo = Language.getText("hauptansicht_info_wechsel").replace("%d",
					String.valueOf(iPages * getConfig().iMainViewRotationSeconds));
		lbInfo.setText(sInfo);
	}

	private void move(int iDirection) {
		int iIndex = lvSponsors.getSelectionModel().getSelectedIndex();
		int iNewIndex = iIndex + iDirection;
		List<String> lNames = getConfig().lMainViewSponsors;
		if (iIndex < 0 || iNewIndex < 0 || iNewIndex >= lNames.size())
			return;
		Collections.swap(lNames, iIndex, iNewIndex);
		changedMainView();
		lvSponsors.getSelectionModel().select(iNewIndex);
	}

	private void removeSponsor() {
		int iIndex = lvSponsors.getSelectionModel().getSelectedIndex();
		if (iIndex < 0)
			return;
		getConfig().lMainViewSponsors.remove(iIndex);
		changedMainView();
		lvSponsors.getSelectionModel().select(Math.min(iIndex, lvSponsors.getItems().size() - 1));
	}

	private void addSponsors() {
		List<String> lChosen = new AddDialog().showAndWait().orElse(new ArrayList<String>());
		if (lChosen.isEmpty())
			return;
		getConfig().lMainViewSponsors.addAll(lChosen);
		changedMainView();
		lvSponsors.getSelectionModel().select(lvSponsors.getItems().size() - 1);
	}

	private String getStateText(Sponsor sponsor) {
		if (sponsor == null)
			return null;
		if (!sponsor.hasLogo() || !advertising.getLogoFile(sponsor).isFile())
			return Language.getText("ohne_logo");
		if (!sponsor.bActive)
			return Language.getText("sponsor_inaktiv");
		return null;
	}

	private StackPane createThumbnail(Sponsor sponsor) {
		ImageView ivLogo = new ImageView(sponsor == null || !sponsor.hasLogo() ? null
				: loadImage(advertising.getLogoFile(sponsor), 112, 56));
		ivLogo.setFitWidth(56);
		ivLogo.setFitHeight(28);
		ivLogo.setPreserveRatio(true);
		StackPane box = new StackPane(ivLogo);
		box.setMinSize(64, 32);
		box.setMaxSize(64, 32);
		// Same dark blue as the clock bar
		box.setBackground(new Background(new BackgroundFill(ScoreboardColors.PANEL, null, null)));
		return box;
	}

	/**
	 * Lets the user pick sponsors that are not in the main view yet.
	 */
	private class AddDialog extends Dialog<List<String>> {
		AddDialog() {
			initOwner(getScene().getWindow());
			setTitle(Language.getText("hauptansicht"));
			setHeaderText(Language.getText("hauptansicht_auswahl"));
			ButtonType btnAdd = new ButtonType(Language.getText("hinzufuegen"), ButtonData.OK_DONE);
			getDialogPane().getButtonTypes().addAll(btnAdd,
					new ButtonType(Language.getText("abbrechen"), ButtonData.CANCEL_CLOSE));

			List<CheckBox> lChecks = new ArrayList<CheckBox>();
			VBox vbSponsors = new VBox(8);
			for (Sponsor sponsor : getConfig().lSponsors) {
				if (getConfig().lMainViewSponsors.contains(sponsor.sName))
					continue;
				CheckBox cb = new CheckBox(sponsor.sName);
				cb.setUserData(sponsor.sName);
				String sState = getStateText(sponsor);
				if (sState != null)
					cb.setText(sponsor.sName + " (" + sState + ")");
				// Without a logo there is nothing to show in the clock bar
				cb.setDisable(!sponsor.hasLogo() || !advertising.getLogoFile(sponsor).isFile());
				lChecks.add(cb);
				HBox row = new HBox(10, createThumbnail(sponsor), cb);
				row.setAlignment(Pos.CENTER_LEFT);
				vbSponsors.getChildren().add(row);
			}
			if (vbSponsors.getChildren().isEmpty())
				vbSponsors.getChildren().add(new Label(Language.getText("hauptansicht_alle_gewaehlt")));
			ScrollPane spSponsors = new ScrollPane(vbSponsors);
			spSponsors.setFitToWidth(true);
			spSponsors.setPrefSize(380, 320);
			getDialogPane().setContent(spSponsors);

			setResultConverter(buttonType -> {
				List<String> lChosen = new ArrayList<String>();
				if (buttonType == btnAdd)
					for (CheckBox cb : lChecks)
						if (cb.isSelected() && !cb.isDisabled())
							lChosen.add((String) cb.getUserData());
				return lChosen;
			});
		}
	}

	private class SponsorCell extends ListCell<String> {
		private final Label lbName = new Label();
		private final Label lbState = new Label();

		SponsorCell() {
			lbName.setMinWidth(0);
			lbState.setOpacity(0.6);
			lbState.setMinWidth(Region.USE_PREF_SIZE);
			// Long names are shortened instead of making the list scroll sideways
			setPrefWidth(0);
		}

		@Override
		protected void updateItem(String sName, boolean bEmpty) {
			super.updateItem(sName, bEmpty);
			if (bEmpty || sName == null) {
				setGraphic(null);
				return;
			}
			Sponsor sponsor = advertising.getSponsor(sName);
			String sState = getStateText(sponsor);
			lbName.setText(sName);
			lbState.setText(sState == null ? "" : sState);
			HBox content = new HBox(10, createThumbnail(sponsor), lbName, lbState);
			content.setAlignment(Pos.CENTER_LEFT);
			content.setOpacity(sState == null ? 1 : 0.5);
			setGraphic(content);
		}
	}
}
