/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui;

import com.sun.javafx.css.StyleManager;
import football.Team;
import football.match.Match;
import gui.frames.ControlFrame;
import gui.frames.LeagueFrame;
import gui.frames.PlaytimeFrame;
import gui.frames.ResultFrame;
import gui.frames.control.ControlFrameStateMatch;
import gui.frames.lineupFrame.LineupFrame;
import gui.frames.panes.PaneLaunchGame;
import gui.frames.panes.PaneMainScoreboard;
import gui.frames.scoreboards.MainScoreboard;
import gui.frames.scoreboards.OtherScoreboards;
import gui.frames.settingsFrame.SettingsFrame;
import gui.menu.Menus;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import message.ErrorMsg;
import message.InfoMsg;
import system.Main;
import system.settings.Frame;
import system.settings.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUIControlling extends Application {

    private static Settings sSettings;
    private MainScoreboard mainScoreboard;
    private ControlFrame controlFrame;
    private List<OtherScoreboards> lOtherFrames = new ArrayList<OtherScoreboards>();
    private Match match;

    @Override
    public void start(Stage primaryStage) throws Exception {


        controlFrame = new ControlFrame(sSettings, this);
        primaryStage = controlFrame;

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.show();
        mainScoreboard = new MainScoreboard(sSettings.mainFrame, sSettings, match);

        for (Frame frame : sSettings.otherFrames) {
            lOtherFrames.add(new OtherScoreboards(frame, frame.otherFrameFunction.getOtherFrameFunction()));
        }

        Application.setUserAgentStylesheet(null);
        switch (sSettings.theme) {

            case DEFAULT:
//                StyleManager.getInstance().setDefaultUserAgentStylesheet();
                break;
            case MODERN:
                StyleManager.getInstance().addUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("/style/customTheme.css")).toExternalForm());
                break;
        }


//        ObservableList<String> stylesheets = primaryStage.getScene().getStylesheets();
//        stylesheets.clear();
//        stylesheets.add(Objects.requireNonNull(getClass().getResource("/style/customTheme.css")).toExternalForm());


        showAllFrames();
        ErrorMsg.setBGuiStarted(true);
        InfoMsg.setBGuiStarted(true);
    }

    public void startNewMatch(Team teamHome, Team teamGuest) {
        match = new Match(teamHome, teamGuest, sSettings.iHalftime1Length, sSettings.iHalftime2Length, sSettings.iHalftime3Length, sSettings.iHalftime4Length);
        Main.setMatch(match);
        controlFrame.changeState(new ControlFrameStateMatch());
        changeMainScoreboardPane(new PaneLaunchGame(getRootPaneMainScoreboard(), controlFrame));
    }

    public void settingsChanged() {
        Application.setUserAgentStylesheet(null);
        switch (sSettings.theme) {

            case DEFAULT:
                StyleManager.getInstance().removeUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("/style/customTheme.css")).toExternalForm());
//                StyleManager.getInstance().setDefaultUserAgentStylesheet();
                break;
            case MODERN:
                StyleManager.getInstance().addUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("/style/customTheme.css")).toExternalForm());
                break;
        }
    }

    public void showAllFrames() {
        mainScoreboard.show();
        for (OtherScoreboards otherScoreboard : lOtherFrames)
            otherScoreboard.show();
    }

    public void setSettings(Settings sSettings) {
        GUIControlling.sSettings = sSettings;
    }

    public static Settings getSettings() {
        return GUIControlling.sSettings;
    }

    public void menuClicked(Menus menu) {
        switch (menu) {
            case FILE_SETTINGS:
                new SettingsFrame(sSettings, this).show();
                break;
            case FOOTBALL_LEAGUES:
                new LeagueFrame(sSettings, this).show();
                break;
            case MATCH_LINEUP:
                new LineupFrame(sSettings, this).show();
                break;
            case MATCH_RESULT:
                new ResultFrame(sSettings, this).show();
                break;
            case MATCH_PLAYTIME:
                new PlaytimeFrame(sSettings, this).show();
                break;
            default:
        }
    }

    public void updateScoreboards() {
        mainScoreboard.updatePosition(sSettings.mainFrame);
        int index = 0;
        for (Frame frame : sSettings.otherFrames) {
            lOtherFrames.get(index).updatePosition(frame);
            index++;
        }

    }

    public void changeMainScoreboardPane(PaneMainScoreboard newPane) {
        mainScoreboard.changePane(newPane);
    }

    public Pane getRootPaneMainScoreboard() {
        return mainScoreboard.getRootPane();
    }

    public ControlFrame getControlFrame() {
        return controlFrame;
    }

    public void start() {
        launch();
    }

}
