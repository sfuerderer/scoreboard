/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames;

import gui.GUIControlling;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import language.Language;
import system.settings.Settings;

public abstract class BasicFrame extends Stage {
	protected Settings sSettings;
	protected GUIControlling guiControlling;

	/**
	 * Creates the center pane.
	 * 
	 * @return The build center Pane or null if no one is used.
	 */
	abstract protected Pane buildCenterPane(Object defaultParams);

	/**
	 * Creates the bottom pane.
	 * 
	 * @return The build bottom Pane or null if no one is used.
	 */
	abstract protected Pane buildBottomPane();
	
	public BasicFrame(Settings sSettings, GUIControlling guiControlling, String sTitleKey, Object defaultParams) {
		this(sSettings,guiControlling,sTitleKey,defaultParams,700,600);
	}

	public BasicFrame(Settings sSettings, GUIControlling guiControlling, String sTitleKey, Object defaultParams,int iWidth,int iHeight) {
		this.sSettings = sSettings;
		this.guiControlling = guiControlling;

		BorderPane mainPane = new BorderPane();

		setX(sSettings.mainFrame.iStartXPosition + sSettings.mainFrame.iWidth + 10);
		setY(0);

		// Create a box for the top of the panel
		HBox topHBox = new HBox();
		topHBox.setPadding(new Insets(10, 5, 10, 5));
		topHBox.setAlignment(Pos.CENTER);

		// Creates the Headline
		Label lbHeadline = new Label(Language.getText(sTitleKey));
		lbHeadline.getStyleClass().add("sf-label-headline");

		//Add all correct to the parent stage
		topHBox.getChildren().addAll(lbHeadline);
		
		mainPane.setTop(topHBox);
		
		Pane centerPane = buildCenterPane(defaultParams);
		Pane bottomPane = buildBottomPane();
		if (centerPane != null)
			mainPane.setCenter(centerPane);
		if (bottomPane != null)
			mainPane.setBottom(bottomPane);

		// Create scene and set the stylesheet
		Scene mainScene = new Scene(mainPane, iWidth, iHeight);
		mainScene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

		setScene(mainScene);
	}
}
