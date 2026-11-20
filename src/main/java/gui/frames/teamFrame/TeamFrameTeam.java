/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.teamFrame;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;

import football.Team;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import language.Language;
import message.ErrorMsg;
import system.Defaults;
import system.Images;
import system.Resetable;

public class TeamFrameTeam extends GridPane implements Resetable{

	private ImageView ivLogoImage;
	private TextField tfShortName;
	private ColorPicker cpColor;
	private Team team;

	public TeamFrameTeam(Team team) {
		this.team = team;
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
		bottomPaneCol1.setPercentWidth(25);
		ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
		bottomPaneCol2.setPercentWidth(25);
		ColumnConstraints bottomPaneCol3 = new ColumnConstraints();
		bottomPaneCol3.setPercentWidth(25);
		ColumnConstraints bottomPaneCol4 = new ColumnConstraints();
		bottomPaneCol4.setPercentWidth(25);
		getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2, bottomPaneCol3, bottomPaneCol4);

		ivLogoImage = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
		ivLogoImage.setFitHeight(100);
		ivLogoImage.setPreserveRatio(true);

		Label lbLogo = new Label(Language.getText("logo"));

		final FileChooser fileChooserLogoImage = new FileChooser();
		Button btnLoadImage = new Button(Language.getText("lade_bild"));
		btnLoadImage.setMaxWidth(Double.MAX_VALUE);
		btnLoadImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File image = fileChooserLogoImage.showOpenDialog(null);
				if (image != null) {
					try {
						String sPathToLogo = Images.getImages().moveLogoImage(team, image);
						team.setsPathToLogo(sPathToLogo);
						team.save();
						updateInformations();
					} catch (IOException e) {
						ErrorMsg.showErrorMessage(110);
					}
				}
			}
		});

		Label lbShortName = new Label(Language.getText("kurzname"));
		tfShortName = new TextField();
		tfShortName.setPromptText(Language.getText("kurzname_hinweis"));
		tfShortName.setTextFormatter(new TextFormatter<String>(
				change -> change.getControlNewText().length() <= Defaults.TEAM_SHORT_NAME_MAX_LENGTH ? change : null));
		// Saved when leaving the field or pressing Enter, so typing does not write the file on every key
		tfShortName.setOnAction(event -> saveShortName());
		tfShortName.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
			if (!isFocused)
				saveShortName();
		});

		Label lbColor = new Label(Language.getText("teamfarbe"));
		cpColor = new ColorPicker();
		cpColor.setMaxWidth(Double.MAX_VALUE);
		cpColor.setOnAction(event -> {
			if (team != null) {
				team.setsColor(toHex(cpColor.getValue()));
				team.save();
			}
		});

		add(lbLogo, 0, 0, 4, 1);
		add(ivLogoImage, 0, 1);
		add(btnLoadImage, 1, 1, 3, 1);
		add(lbShortName, 0, 2, 4, 1);
		add(tfShortName, 0, 3, 4, 1);
		add(lbColor, 0, 4, 4, 1);
		add(cpColor, 0, 5, 2, 1);
		updateInformations();
	}

	private void saveShortName() {
		if (team == null)
			return;
		String sOld = team.getsShortName();
		team.setsShortName(tfShortName.getText());
		if (!Objects.equals(sOld, team.getsShortName()))
			team.save();
	}

	private static String toHex(Color color) {
		return String.format("#%02x%02x%02x", Math.round(color.getRed() * 255), Math.round(color.getGreen() * 255),
				Math.round(color.getBlue() * 255));
	}

	private void updateInformations() {
		if (team == null) {
			ivLogoImage.setImage(Images.getImages().loadImageFromResource("no-image", "png"));
			tfShortName.setText("");
			cpColor.setValue(Color.web(Defaults.TEAM_COLOR));
			return;
		}

		tfShortName.setText(team.getsShortName() == null ? "" : team.getsShortName());
		cpColor.setValue(Color.web(team.getDisplayColor()));

		if (team.getsPathToLogo() != null) {
			try {
				ivLogoImage.setImage(Images.getImages().loadImageFromPath(team.getsPathToLogo()));
			} catch (MalformedURLException e) {
				ErrorMsg.showErrorMessage(112);
			}
		} else {
			ivLogoImage.setImage(Images.getImages().loadImageFromResource("no-image", "png"));
		}
	}

	@Override
	public void reset() {
		updateInformations();
	}

}
