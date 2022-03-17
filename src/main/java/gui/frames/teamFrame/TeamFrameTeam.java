/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.teamFrame;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import football.Team;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import language.Language;
import message.ErrorMsg;
import system.Images;
import system.Resetable;

public class TeamFrameTeam extends GridPane implements Resetable{

	private ImageView ivLogoImage;
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

		add(lbLogo, 0, 0, 4, 1);
		add(ivLogoImage, 0, 1);
		add(btnLoadImage, 1, 1, 3, 1);
		updateInformations();
	}

	private void updateInformations() {
		if (team == null) {
			ivLogoImage.setImage(Images.getImages().loadImageFromResource("no-image", "png"));
			return;
		}

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
