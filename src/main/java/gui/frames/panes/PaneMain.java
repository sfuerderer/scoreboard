/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import football.match.Match;
import gui.frames.ControlFrame;
import gui.frames.panes.components.FixedDigitsLabel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import message.ErrorMsg;
import system.Images;
import system.Main;

import java.net.MalformedURLException;

public class PaneMain extends PaneMainScoreboard {

    public PaneMain(Pane rootPane, ControlFrame controlFrame) {
        super(ScoreboardBackground.crestDiagonal(), rootPane);
        Match match = Main.getMatch();
//		ServicePlaytime servicePlaytime = new ServicePlaytime(controlFrame);

        GridPane paneMain = new GridPane();
        paneMain.prefWidthProperty().bind(this.widthProperty());
        paneMain.prefHeightProperty().bind(this.heightProperty());
        paneMain.setPadding(new Insets(20));
        paneMain.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints bottomPaneCol1 = new ColumnConstraints();
        bottomPaneCol1.setPercentWidth(25);
        ColumnConstraints bottomPaneCol2 = new ColumnConstraints();
        bottomPaneCol2.setPercentWidth(50);
        bottomPaneCol2.setHalignment(HPos.CENTER);
        ColumnConstraints bottomPaneCol3 = new ColumnConstraints();
        bottomPaneCol3.setPercentWidth(25);
        bottomPaneCol3.setHalignment(HPos.RIGHT);
        paneMain.getColumnConstraints().addAll(bottomPaneCol1, bottomPaneCol2, bottomPaneCol3);

        ImageView ivLogoHome;
        ImageView ivLogoGuest;
        try {
            ivLogoHome = new ImageView(Images.getImages().loadImageFromPath(match.gettTeamHome().getsPathToLogo()));
            ivLogoGuest = new ImageView(Images.getImages().loadImageFromPath(match.gettTeamGuest().getsPathToLogo()));
        } catch (MalformedURLException e) {
            ErrorMsg.showErrorMessage(111);
            ivLogoHome = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
            ivLogoGuest = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
        }

        ivLogoHome.setFitHeight(100);
        ivLogoHome.setPreserveRatio(true);
        ivLogoGuest.setFitHeight(100);
        ivLogoGuest.setPreserveRatio(true);

        FixedDigitsLabel lbTime = new FixedDigitsLabel("00:00", Main.getMainFont(), Color.web("#ffffff"));
        lbTime.textProperty().bind(Main.getServicePlaytime().messageProperty());

        FixedDigitsLabel lbResult = new FixedDigitsLabel("0:0", Main.getMainFont(150), Color.web("#ffffff"));
        GridPane.setHalignment(lbResult, HPos.CENTER);
        lbResult.textProperty().bind(Main.getMatch().sResult);

        paneMain.add(ivLogoHome, 0, 0);
        paneMain.add(lbTime, 1, 0);
        paneMain.add(ivLogoGuest, 2, 0);
        paneMain.add(lbResult, 0, 1, 3, 1);


        getChildren().add(paneMain);
    }
}
