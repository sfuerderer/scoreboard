/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.advertisingFrame;

import java.io.File;
import java.io.IOException;
import java.util.List;

import advertising.Advertising;
import advertising.AdvertisingConfig;
import advertising.FullscreenSpot;
import advertising.Sponsor;
import gui.Message;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import system.Trash;

/**
 * List of all sponsors with name, logo, full screen ads and whether they are shown at the moment.
 */
class AdvertisingFrameSponsors extends AdvertisingTab {
	private static final double LOGO_PREVIEW_WIDTH = 260;
	private static final double LOGO_PREVIEW_HEIGHT = 90;
	// The logos are shown on the club blue of the scoreboard, so the preview uses it as well
	private static final Color SCOREBOARD_BLUE = Color.web("#0c1d63");

	private final ListView<Sponsor> lvSponsors;
	private final TextField tfName;
	private final CheckBox cbActive;
	private final ImageView ivLogo;
	private final Label lbNoLogo;
	private final Button btnLogo;
	private final ListView<String> lvMedia;
	private final Button btnAddMedia;
	private final Button btnDeleteMedia;
	private final Button btnDelete;
	private boolean bShowing;

	AdvertisingFrameSponsors(Advertising advertising, Runnable onChange) {
		super(advertising, onChange);

		lvSponsors = new ListView<Sponsor>();
		lvSponsors.setPrefWidth(280);
		lvSponsors.setCellFactory(listView -> new SponsorCell());
		lvSponsors.getItems().setAll(getConfig().lSponsors);
		lvSponsors.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldSponsor, newSponsor) -> showSponsor(newSponsor));

		Button btnNew = new Button(Language.getText("neuer_sponsor"));
		btnNew.setMaxWidth(Double.MAX_VALUE);
		btnNew.setOnAction(event -> addSponsor());
		btnDelete = new Button(Language.getText("loeschen"));
		btnDelete.setMaxWidth(Double.MAX_VALUE);
		btnDelete.setOnAction(event -> deleteSponsor());
		HBox listButtons = new HBox(10, btnNew, btnDelete);
		HBox.setHgrow(btnNew, Priority.ALWAYS);
		HBox.setHgrow(btnDelete, Priority.ALWAYS);
		VBox left = new VBox(10, lvSponsors, listButtons);
		VBox.setVgrow(lvSponsors, Priority.ALWAYS);

		// Details of the selected sponsor
		tfName = new TextField();
		// Saved when leaving the field or pressing Enter, so typing does not write the file on every key
		tfName.setOnAction(event -> saveName());
		tfName.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
			if (!isFocused)
				saveName();
		});

		cbActive = new CheckBox(Language.getText("sponsor_aktiv"));
		cbActive.setOnAction(event -> {
			Sponsor sponsor = getSelected();
			if (sponsor != null && !bShowing) {
				sponsor.bActive = cbActive.isSelected();
				lvSponsors.refresh();
				changed();
			}
		});

		ivLogo = new ImageView();
		ivLogo.setFitWidth(LOGO_PREVIEW_WIDTH);
		ivLogo.setFitHeight(LOGO_PREVIEW_HEIGHT);
		ivLogo.setPreserveRatio(true);
		lbNoLogo = new Label(Language.getText("sponsor_kein_logo"));
		lbNoLogo.setTextFill(Color.WHITE);
		lbNoLogo.setWrapText(true);
		StackPane logoBox = new StackPane(ivLogo, lbNoLogo);
		logoBox.setPadding(new Insets(10));
		logoBox.setMinSize(LOGO_PREVIEW_WIDTH + 20, LOGO_PREVIEW_HEIGHT + 20);
		logoBox.setMaxSize(LOGO_PREVIEW_WIDTH + 20, LOGO_PREVIEW_HEIGHT + 20);
		logoBox.setBackground(new Background(new BackgroundFill(SCOREBOARD_BLUE, null, null)));

		btnLogo = new Button(Language.getText("logo_waehlen"));
		btnLogo.setOnAction(event -> changeLogo());

		btnAddMedia = new Button(Language.getText("werbemittel_hinzufuegen"));
		btnAddMedia.setOnAction(event -> addMedia());
		btnDeleteMedia = new Button(Language.getText("loeschen"));
		btnDeleteMedia.setOnAction(event -> deleteMedia());
		lvMedia = new ListView<String>();
		lvMedia.setPrefHeight(150);
		lvMedia.setCellFactory(listView -> new MediaCell());
		lvMedia.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldMedium, newMedium) -> btnDeleteMedia.setDisable(newMedium == null));
		HBox mediaButtons = new HBox(10, btnAddMedia, btnDeleteMedia);

		Label lbHint = new Label(Language.getText("sponsor_hinweis"));
		lbHint.setWrapText(true);

		GridPane details = new GridPane();
		details.setHgap(20);
		details.setVgap(12);
		details.setPadding(new Insets(0, 0, 0, 20));
		// Labels keep their full width, the space is taken from the right column
		ColumnConstraints labelColumn = new ColumnConstraints();
		labelColumn.setMinWidth(Region.USE_PREF_SIZE);
		details.getColumnConstraints().add(labelColumn);
		details.add(new Label(Language.getText("sponsor_name")), 0, 0);
		details.add(tfName, 1, 0);
		details.add(cbActive, 1, 1);
		Label lbLogo = new Label(Language.getText("logo"));
		GridPane.setValignment(lbLogo, VPos.TOP);
		details.add(lbLogo, 0, 2);
		details.add(logoBox, 1, 2);
		details.add(btnLogo, 1, 3);
		Label lbMedia = new Label(Language.getText("werbemittel"));
		GridPane.setValignment(lbMedia, VPos.TOP);
		details.add(lbMedia, 0, 4);
		details.add(lvMedia, 1, 4);
		details.add(mediaButtons, 1, 5);
		details.add(lbHint, 0, 6, 2, 1);

		setLeft(left);
		setCenter(details);

		if (!lvSponsors.getItems().isEmpty())
			lvSponsors.getSelectionModel().selectFirst();
		else
			showSponsor(null);
	}

	@Override
	String getTitleKey() {
		return "sponsoren";
	}

	@Override
	void refresh() {
		lvSponsors.refresh();
		showSponsor(getSelected());
	}

	private AdvertisingConfig getConfig() {
		return advertising.getConfig();
	}

	private Sponsor getSelected() {
		return lvSponsors.getSelectionModel().getSelectedItem();
	}

	private void showSponsor(Sponsor sponsor) {
		bShowing = true;
		boolean bEmpty = sponsor == null;
		tfName.setDisable(bEmpty);
		cbActive.setDisable(bEmpty);
		btnLogo.setDisable(bEmpty);
		btnAddMedia.setDisable(bEmpty);
		btnDelete.setDisable(bEmpty);
		tfName.setText(bEmpty ? "" : sponsor.sName);
		cbActive.setSelected(!bEmpty && sponsor.bActive);
		ivLogo.setImage(bEmpty || !sponsor.hasLogo() ? null
				: loadImage(advertising.getLogoFile(sponsor), LOGO_PREVIEW_WIDTH, LOGO_PREVIEW_HEIGHT));
		lbNoLogo.setVisible(!bEmpty && ivLogo.getImage() == null);
		if (bEmpty)
			lvMedia.getItems().clear();
		else
			lvMedia.getItems().setAll(advertising.getMedia(sponsor));
		btnDeleteMedia.setDisable(lvMedia.getSelectionModel().getSelectedItem() == null);
		bShowing = false;
	}

	private void addSponsor() {
		String sBase = Language.getText("neuer_sponsor_name");
		String sName = sBase;
		for (int i = 2; getConfig().hasSponsor(sName); i++)
			sName = sBase + " " + i;

		Sponsor sponsor = advertising.addSponsor(sName);
		lvSponsors.getItems().add(sponsor);
		lvSponsors.getSelectionModel().select(sponsor);
		changed();
		tfName.requestFocus();
		tfName.selectAll();
	}

	private void changeLogo() {
		Sponsor sponsor = getSelected();
		if (sponsor == null)
			return;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Language.getText("logo"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter(Language.getText("bilder"), "*.png", "*.jpg",
				"*.jpeg", "*.PNG", "*.JPG", "*.JPEG"));
		File fLogo = fileChooser.showOpenDialog(getScene().getWindow());
		if (fLogo == null)
			return;
		try {
			advertising.setLogo(sponsor, fLogo);
		} catch (IOException e) {
			ErrorMsg.showErrorMessage(119, e.getMessage());
			return;
		}
		showSponsor(sponsor);
		lvSponsors.refresh();
		changed();
	}

	private void addMedia() {
		Sponsor sponsor = getSelected();
		if (sponsor == null)
			return;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Language.getText("werbemittel_hinzufuegen"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter(Language.getText("vollbild_dateien"), "*.png",
				"*.jpg", "*.mp4", "*.PNG", "*.JPG", "*.MP4"));
		List<File> files = fileChooser.showOpenMultipleDialog(getScene().getWindow());
		if (files == null)
			return;
		String sLast = null;
		for (File file : files) {
			if (!FullscreenSpot.isSupported(file.getName()))
				continue;
			try {
				sLast = advertising.addMedia(sponsor, file);
			} catch (IOException e) {
				ErrorMsg.showErrorMessage(119, e.getMessage());
			}
		}
		showSponsor(sponsor);
		if (sLast != null)
			lvMedia.getSelectionModel().select(sLast);
	}

	private void deleteMedia() {
		String sPath = lvMedia.getSelectionModel().getSelectedItem();
		if (sPath == null)
			return;
		String sHint = advertising.isInFullscreen(sPath) ? Language.getText("werbemittel_loeschen_hinweis") : null;
		if (!Message.confirmDelete("werbemittel_loeschen_frage", sHint))
			return;
		File file = advertising.getFile(sPath);
		if (!Trash.moveToTrash(file)) {
			ErrorMsg.showErrorMessage(122, file.getPath());
			return;
		}
		advertising.removeMedia(sPath);
		showSponsor(getSelected());
		changed();
	}

	private void saveName() {
		Sponsor sponsor = getSelected();
		if (sponsor == null || bShowing)
			return;
		String sNewName = tfName.getText().trim();
		if (sNewName.equals(sponsor.sName))
			return;
		if (sNewName.isEmpty() || getConfig().hasSponsor(sNewName)) {
			tfName.setText(sponsor.sName);
			ErrorMsg.showErrorMessage(214);
			return;
		}
		getConfig().renameSponsor(sponsor, sNewName);
		lvSponsors.refresh();
		changed();
	}

	private void deleteSponsor() {
		Sponsor sponsor = getSelected();
		if (sponsor == null)
			return;
		File fFolder = advertising.getSponsorFolder(sponsor);
		String sHint = Language.getText("sponsor_loeschen_dateien")
				.replace("%s", Advertising.S_SPONSOR_FOLDER + "/" + sponsor.sFolder);
		if (getConfig().isSponsorPlaced(sponsor.sName))
			sHint += "\n\n" + Language.getText("sponsor_loeschen_hinweis");
		if (!Message.confirmDelete("sponsor_loeschen_frage", sHint))
			return;
		// The trash keeps the files, so a sponsor that comes back can be restored
		if (!Trash.moveToTrash(fFolder)) {
			ErrorMsg.showErrorMessage(122, fFolder.getPath());
			return;
		}
		getConfig().removeSponsor(sponsor);
		lvSponsors.getItems().remove(sponsor);
		changed();
	}

	private class MediaCell extends ListCell<String> {
		private final ImageView ivThumbnail = new ImageView();
		private final Label lbVideo = new Label(Language.getText("vollbild_video"));
		private final StackPane thumbnailBox = new StackPane(ivThumbnail, lbVideo);
		private final Label lbName = new Label();
		private final Label lbUsed = new Label();
		private final HBox content = new HBox(10, thumbnailBox, lbName, lbUsed);

		MediaCell() {
			ivThumbnail.setFitWidth(56);
			ivThumbnail.setFitHeight(30);
			ivThumbnail.setPreserveRatio(true);
			lbVideo.setTextFill(Color.WHITE);
			thumbnailBox.setMinSize(56, 30);
			thumbnailBox.setMaxSize(56, 30);
			thumbnailBox.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			lbUsed.setOpacity(0.6);
			content.setAlignment(Pos.CENTER_LEFT);
			setPrefWidth(0);
		}

		@Override
		protected void updateItem(String sPath, boolean bEmpty) {
			super.updateItem(sPath, bEmpty);
			if (bEmpty || sPath == null) {
				setGraphic(null);
				return;
			}
			FullscreenSpot medium = new FullscreenSpot(sPath, true, 0);
			lbVideo.setVisible(medium.isVideo());
			ivThumbnail.setImage(medium.isVideo() ? null : loadImage(advertising.getFile(sPath), 112, 60));
			lbName.setText(medium.getFileName());
			lbUsed.setText(advertising.isInFullscreen(sPath) ? Language.getText("im_vollbild") : "");
			setGraphic(content);
		}
	}

	private class SponsorCell extends ListCell<Sponsor> {
		private final ImageView ivThumbnail = new ImageView();
		private final StackPane thumbnailBox = new StackPane(ivThumbnail);
		private final Label lbName = new Label();
		private final HBox content = new HBox(10, thumbnailBox, lbName);

		SponsorCell() {
			ivThumbnail.setFitWidth(48);
			ivThumbnail.setFitHeight(24);
			ivThumbnail.setPreserveRatio(true);
			thumbnailBox.setMinSize(56, 28);
			thumbnailBox.setMaxSize(56, 28);
			// Same blue as the scoreboard, so white logos stay visible
			thumbnailBox.setBackground(new Background(new BackgroundFill(SCOREBOARD_BLUE, null, null)));
			lbName.setMinWidth(0);
			content.setAlignment(Pos.CENTER_LEFT);
			// Long names are shortened instead of making the list scroll sideways
			setPrefWidth(0);
		}

		@Override
		protected void updateItem(Sponsor sponsor, boolean bEmpty) {
			super.updateItem(sponsor, bEmpty);
			if (bEmpty || sponsor == null) {
				setGraphic(null);
				return;
			}
			ivThumbnail.setImage(sponsor.hasLogo() ? loadImage(advertising.getLogoFile(sponsor), 96, 48) : null);
			lbName.setText(sponsor.bActive ? sponsor.sName
					: sponsor.sName + " (" + Language.getText("sponsor_inaktiv") + ")");
			lbName.setOpacity(sponsor.bActive ? 1 : 0.5);
			setGraphic(content);
		}
	}
}
