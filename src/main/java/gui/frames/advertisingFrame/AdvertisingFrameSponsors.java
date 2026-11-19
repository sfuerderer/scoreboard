/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.advertisingFrame;

import java.io.File;
import java.io.IOException;

import advertising.Advertising;
import advertising.AdvertisingConfig;
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

/**
 * List of all sponsors with name, logo and whether they are shown at the moment.
 */
class AdvertisingFrameSponsors extends AdvertisingTab {
	private static final double LOGO_PREVIEW_WIDTH = 260;
	private static final double LOGO_PREVIEW_HEIGHT = 110;
	// The logos are shown on the club blue of the scoreboard, so the preview uses it as well
	private static final Color SCOREBOARD_BLUE = Color.web("#0c1d63");

	private final ListView<Sponsor> lvSponsors;
	private final TextField tfName;
	private final CheckBox cbActive;
	private final ImageView ivLogo;
	private final Button btnLogo;
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
		StackPane logoBox = new StackPane(ivLogo);
		logoBox.setPadding(new Insets(10));
		logoBox.setMinSize(LOGO_PREVIEW_WIDTH + 20, LOGO_PREVIEW_HEIGHT + 20);
		logoBox.setBackground(new Background(new BackgroundFill(SCOREBOARD_BLUE, null, null)));

		btnLogo = new Button(Language.getText("logo_aendern"));
		btnLogo.setOnAction(event -> changeLogo());

		Label lbHint = new Label(Language.getText("sponsor_hinweis"));
		lbHint.setWrapText(true);

		GridPane details = new GridPane();
		details.setHgap(20);
		details.setVgap(15);
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
		details.add(lbHint, 0, 4, 2, 1);

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
		btnDelete.setDisable(bEmpty);
		tfName.setText(bEmpty ? "" : sponsor.sName);
		cbActive.setSelected(!bEmpty && sponsor.bActive);
		ivLogo.setImage(bEmpty ? null
				: loadImage(advertising.getLogoFile(sponsor), LOGO_PREVIEW_WIDTH, LOGO_PREVIEW_HEIGHT));
		bShowing = false;
	}

	private File chooseLogo() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Language.getText("logo"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter(Language.getText("bilder"), "*.png", "*.jpg",
				"*.jpeg", "*.PNG", "*.JPG", "*.JPEG"));
		return fileChooser.showOpenDialog(getScene().getWindow());
	}

	/**
	 * @return the name of the logo in the logo folder or null if it could not be copied
	 */
	private String importLogo(File fLogo) {
		try {
			return Advertising.importFile(fLogo, advertising.getLogoFolder());
		} catch (IOException e) {
			ErrorMsg.showErrorMessage(119, e.getMessage());
			return null;
		}
	}

	private void addSponsor() {
		File fLogo = chooseLogo();
		if (fLogo == null)
			return;
		String sLogo = importLogo(fLogo);
		if (sLogo == null)
			return;

		// The file name is a good first name, the user can change it afterwards
		String sBase = fLogo.getName().contains(".")
				? fLogo.getName().substring(0, fLogo.getName().lastIndexOf('.'))
				: fLogo.getName();
		String sName = sBase;
		for (int i = 2; getConfig().hasSponsor(sName); i++)
			sName = sBase + " " + i;

		Sponsor sponsor = new Sponsor(sName, sLogo, true);
		getConfig().lSponsors.add(sponsor);
		lvSponsors.getItems().add(sponsor);
		lvSponsors.getSelectionModel().select(sponsor);
		changed();
		tfName.requestFocus();
		tfName.selectAll();
	}

	private void changeLogo() {
		Sponsor sponsor = getSelected();
		File fLogo = sponsor == null ? null : chooseLogo();
		if (fLogo == null)
			return;
		String sLogo = importLogo(fLogo);
		if (sLogo == null)
			return;
		sponsor.sLogoFile = sLogo;
		showSponsor(sponsor);
		lvSponsors.refresh();
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
		String sHint = getConfig().isSponsorPlaced(sponsor.sName) ? Language.getText("sponsor_loeschen_hinweis")
				: null;
		if (!Message.confirmDelete("sponsor_loeschen_frage", sHint))
			return;
		// The logo file stays in the folder, it might belong to a contract that is renewed later
		getConfig().removeSponsor(sponsor);
		lvSponsors.getItems().remove(sponsor);
		changed();
	}

	private class SponsorCell extends ListCell<Sponsor> {
		private final ImageView ivThumbnail = new ImageView();
		private final Label lbName = new Label();
		private final HBox content = new HBox(10, ivThumbnail, lbName);

		SponsorCell() {
			ivThumbnail.setFitWidth(48);
			ivThumbnail.setFitHeight(24);
			ivThumbnail.setPreserveRatio(true);
			content.setAlignment(Pos.CENTER_LEFT);
		}

		@Override
		protected void updateItem(Sponsor sponsor, boolean bEmpty) {
			super.updateItem(sponsor, bEmpty);
			if (bEmpty || sponsor == null) {
				setGraphic(null);
				return;
			}
			ivThumbnail.setImage(loadImage(advertising.getLogoFile(sponsor), 96, 48));
			lbName.setText(sponsor.bActive ? sponsor.sName
					: sponsor.sName + " (" + Language.getText("sponsor_inaktiv") + ")");
			lbName.setOpacity(sponsor.bActive ? 1 : 0.5);
			setGraphic(content);
		}
	}
}
