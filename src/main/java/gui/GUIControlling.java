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
import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.event.EventHandler;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import message.ErrorMsg;
import message.InfoMsg;
import system.AppIcon;
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

        AppIcon.applyToAllWindows();
        keepDialogsInFront();

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
            lOtherFrames.add(new OtherScoreboards(frame, sSettings, frame.otherFrameFunction.getOtherFrameFunction()));
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

    /**
     * Dialogs without an owner open centered on the primary screen, which is
     * often the LED wall, and end up behind the always-on-top scoreboards.
     * Every dialog (alerts, goal/card selection, ...) is therefore moved to the
     * window it was opened from (usually the control frame) and kept in front.
     */
    private void keepDialogsInFront() {
        Window.getWindows().addListener((ListChangeListener<Window>) change -> {
            while (change.next()) {
                for (Window window : change.getAddedSubList()) {
                    if (window instanceof Stage && window.getScene() != null
                            && window.getScene().getRoot() instanceof DialogPane) {
                        Stage stage = (Stage) window;
                        Window target = getDialogTarget(stage);
                        stage.setAlwaysOnTop(true);
                        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> {
                            centerOn(stage, target);
                            stage.toFront();
                        });
                    }
                }
            }
        });
    }

    /** The focused operator window, or the control frame if none (never a scoreboard). */
    private Window getDialogTarget(Stage dialog) {
        for (Window window : Window.getWindows()) {
            if (window != dialog && window.isShowing() && window.isFocused()
                    && !(window instanceof MainScoreboard) && !(window instanceof OtherScoreboards)
                    && !(window.getScene() != null && window.getScene().getRoot() instanceof DialogPane)) {
                return window;
            }
        }
        return controlFrame;
    }

    private static void centerOn(Stage dialog, Window target) {
        if (target == null || !target.isShowing()) {
            return;
        }
        double dX = target.getX() + (target.getWidth() - dialog.getWidth()) / 2;
        double dY = target.getY() + (target.getHeight() - dialog.getHeight()) / 3;

        // keep the dialog completely on the screen of the target window
        List<Screen> lScreens = Screen.getScreensForRectangle(target.getX(), target.getY(),
                target.getWidth(), target.getHeight());
        Rectangle2D bounds = (lScreens.isEmpty() ? Screen.getPrimary() : lScreens.get(0)).getVisualBounds();
        dX = Math.max(bounds.getMinX(), Math.min(dX, bounds.getMaxX() - dialog.getWidth()));
        dY = Math.max(bounds.getMinY(), Math.min(dY, bounds.getMaxY() - dialog.getHeight()));

        dialog.setX(dX);
        dialog.setY(dY);
    }

    public void startNewMatch(Team teamHome, Team teamGuest) {
        match = new Match(teamHome, teamGuest, sSettings.iHalftime1Length, sSettings.iHalftime2Length, sSettings.iHalftime3Length, sSettings.iHalftime4Length);
        Main.setMatch(match, controlFrame::halftimeEnded);
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
        AppIcon.setTaskbarIcon();
        launch();
    }

}
