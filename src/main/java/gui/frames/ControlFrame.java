/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames;

import football.Team;
import gui.GUIControlling;
import gui.frames.control.ControlFrameState;
import gui.frames.control.ControlFrameStateMatch;
import gui.frames.control.ControlFrameStateStart;
import gui.frames.control.ControlFrameStateTimeRunning;
import gui.frames.panes.PaneMainScoreboard;
import gui.menu.ControlMenuBar;
import gui.menu.Menus;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import language.Language;
import system.Main;
import system.settings.Settings;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ControlFrame extends Stage {

    private Settings sSettings;
    private ControlFrameState cfsCurrentState = new ControlFrameStateStart();
    private GUIControlling guiControlling;
    private BorderPane mainPane;

    public ControlFrame(Settings sSettings, GUIControlling guiControlling) {
        this.sSettings = sSettings;
        this.guiControlling = guiControlling;
        setX(sSettings.mainFrame.iStartXPosition + sSettings.mainFrame.iWidth + 10);
        setY(0);
        setTitle(Language.getText("anzeigetafel"));

        MenuBar menuBar = new ControlMenuBar(this);

        Label tCopyright = new Label("© " + new GregorianCalendar().get(Calendar.YEAR) + " Sebastian Fürderer (v." + Main.VERSION + ")");
        tCopyright.getStyleClass().add("text--white");

        // Create a box for the bottom of the panel
        HBox bottomHBox = new HBox();
        bottomHBox.setPadding(new Insets(5));
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.getChildren().add(tCopyright);

        mainPane = new BorderPane();

        mainPane.setTop(menuBar);
        mainPane.setCenter(cfsCurrentState.buildButtons(this));
        mainPane.setBottom(bottomHBox);

        Scene main = new Scene(mainPane, 300, 150);
        setScene(main);

    }

    public void changeState(ControlFrameState newState) {
        cfsCurrentState = newState;
        mainPane.setCenter(cfsCurrentState.buildButtons(this));
        if (cfsCurrentState instanceof ControlFrameStateMatch || cfsCurrentState instanceof ControlFrameStateTimeRunning)
            setHeight(650);
        else
            setHeight(150);
    }

    public void menuClicked(Menus menu) {
        guiControlling.menuClicked(menu);
    }

    public void changeMainScoreboardPane(PaneMainScoreboard newPane) {
        guiControlling.changeMainScoreboardPane(newPane);
    }

    public void startMatch(Team teamHome, Team teamGuest) {
        guiControlling.startNewMatch(teamHome, teamGuest);
    }

    public Pane getRootPaneMainScoreboard() {
        return guiControlling.getRootPaneMainScoreboard();
    }

    public GUIControlling getGuiControlling() {
        return guiControlling;
    }

    public Settings getSettings() {
        return sSettings;
    }
}
