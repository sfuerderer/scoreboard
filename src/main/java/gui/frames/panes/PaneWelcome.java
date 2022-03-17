/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.net.MalformedURLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import language.Language;
import message.ErrorMsg;
import system.Images;
import system.Main;

public class PaneWelcome extends PaneMainScoreboard{
	
	public PaneWelcome(Pane rootPane) {
		super(Images.getImages().loadImageFromResource("background1", "jpg"),rootPane);
		
		BorderPane paneMain = new BorderPane();
		paneMain.prefWidthProperty().bind(this.widthProperty());
		paneMain.prefHeightProperty().bind(this.heightProperty());
		paneMain.setPadding(new Insets(I_PADDING));
		
		VBox content = new VBox();
		content.setAlignment(Pos.CENTER);
		content.setSpacing(10);
		
		Label lbLine1 = new Label(Language.getText("welcome_line_1"));
		Label lbLine2 = new Label(Language.getText("welcome_line_2"));

		lbLine1.setTextFill(Color.web("#ffffff"));
		lbLine1.setFont(Main.getMainFont(40));
		lbLine2.setTextFill(Color.web("#ffffff"));
		lbLine2.setFont(Main.getMainFont(40));
		

		ImageView ivIcon;
		try {
			ivIcon = new ImageView(Images.getImages().loadImageFromPath(Main.getMatch().gettTeamHome().getsPathToLogo()));
			ivIcon.setFitHeight(100);
			ivIcon.setPreserveRatio(true);
			content.getChildren().addAll(ivIcon);
		} catch (MalformedURLException e) {
			ErrorMsg.showErrorMessage(112);
		}
		
		content.getChildren().addAll(lbLine1,lbLine2);
		paneMain.setCenter(content);
		
		getChildren().add(paneMain);
	}

}
