/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.menu;

import gui.frames.ControlFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import language.Language;

public class ControlMenuBar extends MenuBar implements EventHandler<ActionEvent> {
	private Menu mFile, mFootball, mMatch;
	private MenuItem menuItemSettings, menuItemLeagues, menuItemLineup, menuItemResult, menuItemTime;
	private ControlFrame controlFrame;

	public ControlMenuBar(ControlFrame controlFrame) {
		this.controlFrame = controlFrame;

		mFile = new Menu(Language.getText("datei"));
		mFootball = new Menu(Language.getText("fussball"));
		mMatch = new Menu(Language.getText("spiel"));

		menuItemSettings = new MenuItem(Language.getText("einstellungen"));
		menuItemSettings.setOnAction(this);

		menuItemLeagues = new MenuItem(Language.getText("ligen"));
		menuItemLeagues.setOnAction(this);

		menuItemLineup = new MenuItem(Language.getText("aufstellung"));
		menuItemLineup.setOnAction(this);

		menuItemResult = new MenuItem(Language.getText("ergebnis"));
		menuItemResult.setOnAction(this);

		menuItemTime = new MenuItem(Language.getText("zeit"));
		menuItemTime.setOnAction(this);

		mFile.getItems().addAll(menuItemSettings);
		mFootball.getItems().addAll(menuItemLeagues);
		mMatch.getItems().addAll(menuItemLineup, menuItemResult, menuItemTime);

		getMenus().addAll(mFile, mFootball, mMatch);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == menuItemSettings) {
			controlFrame.menuClicked(Menus.FILE_SETTINGS);
		} else if (event.getSource() == menuItemLeagues) {
			controlFrame.menuClicked(Menus.FOOTBALL_LEAGUES);
		} else if (event.getSource() == menuItemLineup) {
			controlFrame.menuClicked(Menus.MATCH_LINEUP);
		} else if (event.getSource() == menuItemResult) {
			controlFrame.menuClicked(Menus.MATCH_RESULT);
		} else if (event.getSource() == menuItemTime) {
			controlFrame.menuClicked(Menus.MATCH_PLAYTIME);
		}

	}

}
