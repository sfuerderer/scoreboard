/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.net.MalformedURLException;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import message.ErrorMsg;
import system.Images;
import system.Main;

public class PanePerson extends PaneMainScoreboard {
	private Image imgPerson;
	private ImageView ivPersonImage;

	public PanePerson(Pane rootPane, PanePersonIcons icon, String sText1) {
		this(rootPane, icon, sText1, null, null, null);
	}

	public PanePerson(Pane rootPane, PanePersonIcons icon, String sText1, String sText2) {
		this(rootPane, icon, sText1, sText2, null, null);
	}

	public PanePerson(Pane rootPane, PanePersonIcons icon, String sText1, String sText2, String sText3) {
		this(rootPane, icon, sText1, sText2, sText3, null);
	}

	public PanePerson(Pane rootPane, PanePersonIcons icon, String sText1, String sText2, String sText3,
			String sPathToImage) {
		super(ScoreboardBackground.crestDiagonal(), rootPane);

		BorderPane panePlayer = new BorderPane();
		panePlayer.prefWidthProperty().bind(this.widthProperty());
		panePlayer.prefHeightProperty().bind(this.heightProperty());
		panePlayer.setPadding(new Insets(I_PADDING));

		VBox playerData = new VBox();
		playerData.setAlignment(Pos.CENTER);
		playerData.setSpacing(10);

		// Upper case like on TV graphics; the old scoreboard font only had capital letters
		Label lbText1 = new Label(sText1 == null ? "" : sText1.toUpperCase(Locale.GERMAN));
		Label lbText2 = new Label(sText2 == null ? "" : sText2.toUpperCase(Locale.GERMAN));
		Label lbText3 = new Label(sText3 == null ? "" : sText3.toUpperCase(Locale.GERMAN));

		lbText1.setTextFill(Color.web("#ffffff"));
		lbText1.setFont(Main.getMainFont(40));
		lbText2.setTextFill(Color.web("#ffffff"));
		lbText2.setFont(Main.getMainFont(40));
		lbText3.setTextFill(Color.web("#ffffff"));
		lbText3.setFont(Main.getMainFont(40));

		ivPersonImage = null;
		if (sPathToImage != null) {
			try {
				imgPerson = Images.getImages().loadImageFromPath(sPathToImage);
				ivPersonImage = new ImageView(imgPerson);
			} catch (MalformedURLException e) {
				ErrorMsg.showErrorMessage(112);
			}
			ivPersonImage.setFitHeight(getPrefHeight() - 2 * I_PADDING);
			ivPersonImage.setPreserveRatio(true);
			panePlayer.setLeft(ivPersonImage);
			playerData.setAlignment(Pos.BOTTOM_RIGHT);
		}

		if (icon == PanePersonIcons.NONE || icon == null) {
			playerData.getChildren().addAll(lbText1, lbText2, lbText3);
		} else {
			ImageView ivIcon = new ImageView(getIcon(icon));
			ivIcon.setFitHeight(50);
			ivIcon.setPreserveRatio(true);
			playerData.getChildren().addAll(ivIcon, lbText1, lbText2, lbText3);
		}

		panePlayer.setCenter(playerData);

		getChildren().add(panePlayer);
	}

	@Override
	public void paneDidDisappear() {
		super.paneDidDisappear();
		if (ivPersonImage != null) {
			imgPerson = null;
			ivPersonImage.setImage(null);
		}
	}

	private Image getIcon(PanePersonIcons icon) {
		switch (icon) {
		case BALL:
			return Images.getImages().loadImageFromResource("fussball", "png");
		case RED_CARD:
			return Images.getImages().loadImageFromResource("rote_karte", "png");
		case REPLACEMENT_IN:
			return Images.getImages().loadImageFromResource("einwechselung", "png");
		case REPLACEMENT_OUT:
			return Images.getImages().loadImageFromResource("auswechselung", "png");
		case YELLOW_CARD:
			return Images.getImages().loadImageFromResource("gelbe_karte", "png");
		case YELLOW_RED_CARD:
			return Images.getImages().loadImageFromResource("gelb-rote_karte", "png");
		default:
			return null;
		}
	}
}
