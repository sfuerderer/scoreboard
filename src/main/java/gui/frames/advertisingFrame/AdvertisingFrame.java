/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.advertisingFrame;

import java.io.IOException;

import advertising.Advertising;
import exceptions.FileNotCreatedException;
import gui.GUIControlling;
import gui.frames.BasicFrame;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import language.Language;
import message.ErrorMsg;
import system.settings.Settings;

/**
 * Manages sponsors and the places they are shown on. Every change is saved at once, like in the team window.
 */
public class AdvertisingFrame extends BasicFrame {

	public AdvertisingFrame(Settings sSettings, GUIControlling guiControlling) {
		super(sSettings, guiControlling, "werbung", null, 820, 640);
	}

	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		Advertising advertising = Advertising.getAdvertising();
		// Shows files that were copied into the folder while the program was running
		advertising.syncWithFolder();

		Runnable onChange = AdvertisingFrame::save;
		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		for (AdvertisingTab tab : new AdvertisingTab[] { new AdvertisingFrameSponsors(advertising, onChange),
				new AdvertisingFrameFullscreen(advertising, onChange, sSettings) })
			tabPane.getTabs().add(new Tab(Language.getText(tab.getTitleKey()), tab));
		tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
			if (newTab != null)
				((AdvertisingTab) newTab.getContent()).refresh();
		});

		return new BorderPane(tabPane);
	}

	@Override
	protected Pane buildBottomPane() {
		return null;
	}

	private static void save() {
		try {
			Advertising.getAdvertising().save();
		} catch (FileNotCreatedException | IOException e) {
			ErrorMsg.showErrorMessage(120, e.getMessage());
		}
	}
}
