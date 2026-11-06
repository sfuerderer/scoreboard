/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.replacementFrame;

import java.util.List;

import football.Team;
import football.match.MatchMinute;
import football.match.Teams;
import football.person.Player;
import gui.GUIControlling;
import gui.frames.ControlFrame;
import gui.frames.panes.PaneMain;
import gui.frames.panes.PaneSubstitution;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import language.Language;
import message.ErrorMsg;
import system.Main;
import system.settings.Settings;

public class ReplacementFrameTeam extends GridPane implements Runnable {
	private ObservableList<Player> tableDataReplacementIn, tableDataReplacementOut;
	private Settings sSettings;
	private GUIControlling guiControlling;
	private ReplacementFrame replacementFrame;
	private ControlFrame controlFrame;
	// Set on the JavaFX thread before the substitution thread starts
	private volatile Team replacementTeam;
	private volatile String sReplacementMinute;
	private boolean bRunning = false;

	@SuppressWarnings("unchecked")
	public ReplacementFrameTeam(Teams selectedTeam, Settings sSettings, GUIControlling guiControlling, ReplacementFrame replacementFrame, ControlFrame controlFrame) {
		this.sSettings = sSettings;
		this.guiControlling = guiControlling;
		this.replacementFrame = replacementFrame;
		this.controlFrame = controlFrame;
		if (Main.getMatch() == null) {
			ErrorMsg.showErrorMessage(206);
			return;
		}
		
		replacementFrame.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent e) {
		    	//TODO Stoppen funktioniert nicht
		    	bRunning=false;
		    }
		  });

		tableDataReplacementIn = FXCollections.observableArrayList();
		tableDataReplacementOut = FXCollections.observableArrayList();

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
		
		// Title and button rows use their natural height, the two list rows share the remaining space.
		// Percent heights would turn the fixed label/button heights into a minimum height of the whole grid
		// (e.g. 25px / 5% = 500px) and push the button out of the window with larger fonts (Windows).
		RowConstraints paneRow1 = new RowConstraints();
		RowConstraints paneRow2 = buildGrowingRow();
		RowConstraints paneRow3 = new RowConstraints();
		RowConstraints paneRow4 = buildGrowingRow();
		RowConstraints paneRow5 = new RowConstraints();
		getRowConstraints().addAll(paneRow1,paneRow2,paneRow3,paneRow4,paneRow5);

		ScrollPane spAllPlayers = new ScrollPane();
		spAllPlayers.setHbarPolicy(ScrollBarPolicy.NEVER);
		spAllPlayers.setFitToWidth(true);
		spAllPlayers.setFitToHeight(true);
		spAllPlayers.setContent(buildAllPlayers(selectedTeam));

		TableView<Player> tableOnField = new TableView<Player>();

		TableColumn<Player, String> tableOnFieldFirstCol = new TableColumn<Player, String>(Language.getText("vorname"));
		tableOnFieldFirstCol.setCellValueFactory(new PropertyValueFactory<Player, String>("sFirstname"));

		TableColumn<Player, String> tableOnFieldSecondCol = new TableColumn<Player, String>(
				Language.getText("nachname"));
		tableOnFieldSecondCol.setCellValueFactory(new PropertyValueFactory<Player, String>("sLastname"));

		tableOnField.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableOnFieldFirstCol.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width
		tableOnFieldSecondCol.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width

		tableOnField.setItems(tableDataReplacementIn);
		tableOnField.getColumns().addAll(tableOnFieldFirstCol, tableOnFieldSecondCol);

		TableView<Player> tableOnChange = new TableView<Player>();

		TableColumn<Player, String> tableOnChangeFirstCol = new TableColumn<Player, String>(
				Language.getText("vorname"));
		tableOnChangeFirstCol.setCellValueFactory(new PropertyValueFactory<Player, String>("sFirstname"));

		TableColumn<Player, String> tableOnChangeSecondCol = new TableColumn<Player, String>(
				Language.getText("nachname"));
		tableOnChangeSecondCol.setCellValueFactory(new PropertyValueFactory<Player, String>("sLastname"));

		tableOnChange.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableOnChangeFirstCol.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width
		tableOnChangeSecondCol.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width

		tableOnChange.setItems(tableDataReplacementOut);
		tableOnChange.getColumns().addAll(tableOnChangeFirstCol, tableOnChangeSecondCol);
		
		Button btnShow = new Button(Language.getText("anzeigen"));
		btnShow.setMaxWidth(Double.MAX_VALUE);
		btnShow.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (tableDataReplacementIn.size() == tableDataReplacementOut.size()) {
					showReplacement(selectedTeam);
					btnShow.setDisable(true);
				} else {
					ErrorMsg.showErrorMessage(204);
				}
			}
		});
		
		Label lbTitleLineup = new Label(Language.getText("startaufstellung"));
		lbTitleLineup.getStyleClass().add("sf-label-title");
		Label lbTitleReplacementBank = new Label(Language.getText("ersatzbank"));
		lbTitleReplacementBank.getStyleClass().add("sf-label-title");
		Label lbTitleOnField = new Label(Language.getText("einwecheln"));
		lbTitleOnField.getStyleClass().add("sf-label-title");
		Label lbTitleReplacement = new Label(Language.getText("auswechseln"));
		lbTitleReplacement.getStyleClass().add("sf-label-title");
		
		add(lbTitleLineup,0,0,2,1);
		add(lbTitleReplacementBank,2,0,2,1);
		add(spAllPlayers, 0, 1, 4, 1);
		add(lbTitleOnField,0,2,2,1);
		add(lbTitleReplacement,2,2,2,1);
		add(tableOnField, 0, 3,2,1);
		add(tableOnChange, 2, 3,2,1);
		add(btnShow,0,4,4,1);

//		add(spAllPlayers, 0, 0, 2, 1);
//		add(tableOnField, 2, 0);
//		add(tableOnChange, 3, 0);
//		add(btnShow,0,1,4,1);
	}
	
	private RowConstraints buildGrowingRow() {
		RowConstraints row = new RowConstraints();
		row.setPrefHeight(0);
		row.setVgrow(Priority.ALWAYS);
		return row;
	}

	private void showReplacement(Teams selectedTeam) {
		for (Player player : tableDataReplacementIn) {
			Main.getMatch().getlLineup().removePlayerOnExchange(selectedTeam, player);
			Main.getMatch().getlLineup().addPlayerOnField(selectedTeam, player);
		}
		for (Player player : tableDataReplacementOut) {
			Main.getMatch().getlLineup().removePlayerOnField(selectedTeam, player);
			Main.getMatch().getlLineup().addPlayerOnExchange(selectedTeam, player);
		}
		// Read here on the JavaFX thread: the playtime service must not be used from the substitution thread
		replacementTeam = selectedTeam == Teams.HOME ? Main.getMatch().gettTeamHome() : Main.getMatch().gettTeamGuest();
		sReplacementMinute = MatchMinute.format(Main.getServicePlaytime().getMessage());
		bRunning = true;
		new Thread(this).start();
	}

	private GridPane buildAllPlayers(Teams selectedTeam) {
		VBox allPlayer1 = new VBox();
		VBox allPlayer2 = new VBox();
		List<Player> lineup;
		switch (selectedTeam) {
		case HOME:
			lineup = Main.getMatch().getlLineup().getlHomePlayersOnField();
			break;
		case GUEST:
			lineup = Main.getMatch().getlLineup().getlGuestPlayersOnField();
			break;
		default:
			return null;
		}

		List<Player> replacement;
		switch (selectedTeam) {
		case HOME:
			replacement = Main.getMatch().getlLineup().getlHomePlayersOnExchange();
			break;
		case GUEST:
			replacement = Main.getMatch().getlLineup().getlGuestPlayersOnExchange();
			break;
		default:
			return null;
		}

		// BUG Auch hier der selbe bug Wenn Spieler hinzugefügt wird dann wird die
		// Mannschaft hier nicht korrekt gelistet. Erst nach einem Restart.
		for (Player player : lineup) {
			GridPane gpPlayerLine = new GridPane();
			gpPlayerLine.setPadding(new Insets(5));
			gpPlayerLine.setHgap(5);
			gpPlayerLine.setVgap(10);

			ColumnConstraints gpPlayerLineCol1 = new ColumnConstraints();
			gpPlayerLineCol1.setPercentWidth(30);
			ColumnConstraints gpPlayerLineCol2 = new ColumnConstraints();
			gpPlayerLineCol2.setPercentWidth(35);
			ColumnConstraints gpPlayerLineCol3 = new ColumnConstraints();
			gpPlayerLineCol3.setPercentWidth(35);

			gpPlayerLine.getColumnConstraints().addAll(gpPlayerLineCol1, gpPlayerLineCol2, gpPlayerLineCol3);

			Label lbPlayerName = new Label(player.getiNumber()+" - "+player.getsFirstname() + " " + player.getsLastname());

			Button btnReplacementOut = new Button(Language.getText("auswechseln"));
			btnReplacementOut.setMaxWidth(Double.MAX_VALUE);
			btnReplacementOut.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					addPlayerToReplacementOut(player);
				}
			});

			gpPlayerLine.add(lbPlayerName, 0, 0,2,1);
			gpPlayerLine.add(btnReplacementOut, 2, 0);

			allPlayer1.getChildren().add(gpPlayerLine);
		}

		for (Player player : replacement) {
			GridPane gpPlayerLine = new GridPane();
			gpPlayerLine.setPadding(new Insets(5));
			gpPlayerLine.setHgap(5);
			gpPlayerLine.setVgap(10);

			ColumnConstraints gpPlayerLineCol1 = new ColumnConstraints();
			gpPlayerLineCol1.setPercentWidth(30);
			ColumnConstraints gpPlayerLineCol2 = new ColumnConstraints();
			gpPlayerLineCol2.setPercentWidth(35);
			ColumnConstraints gpPlayerLineCol3 = new ColumnConstraints();
			gpPlayerLineCol3.setPercentWidth(35);

			gpPlayerLine.getColumnConstraints().addAll(gpPlayerLineCol1, gpPlayerLineCol2, gpPlayerLineCol3);

			Label lbPlayerName = new Label(player.getiNumber()+" - "+player.getsFirstname() + " " + player.getsLastname());

			Button btnReplacementIn = new Button(Language.getText("einwecheln"));
			btnReplacementIn.setMaxWidth(Double.MAX_VALUE);
			btnReplacementIn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					addPlayerToReplacementIn(player);
				}
			});

			gpPlayerLine.add(lbPlayerName, 0, 0,2,1);
			gpPlayerLine.add(btnReplacementIn, 2, 0);

			allPlayer2.getChildren().add(gpPlayerLine);
		}

		GridPane gpResult = new GridPane();
		gpResult.setPadding(new Insets(5));
		gpResult.setHgap(5);
		gpResult.setVgap(10);

		ColumnConstraints gpPlayerLineCol1 = new ColumnConstraints();
		gpPlayerLineCol1.setPercentWidth(50);
		ColumnConstraints gpPlayerLineCol2 = new ColumnConstraints();
		gpPlayerLineCol2.setPercentWidth(50);

		gpResult.getColumnConstraints().addAll(gpPlayerLineCol1, gpPlayerLineCol2);

		gpResult.add(allPlayer1, 0, 0);
		gpResult.add(allPlayer2, 1, 0);
		
		return gpResult;
	}

	private void addPlayerToReplacementIn(Player player) {
		if (tableDataReplacementIn.contains(player))
			tableDataReplacementIn.remove(player);
		else
			tableDataReplacementIn.add(player);

	}

	private void addPlayerToReplacementOut(Player player) {
		if (tableDataReplacementOut.contains(player))
			tableDataReplacementOut.remove(player);
		else
			tableDataReplacementOut.add(player);
	}

	@Override
	public void run() {
		try {
			// Player coming on and going off are shown together, as long as both were shown one after the other before
			for (int i = 0; i < tableDataReplacementIn.size() && bRunning ; i++) {
				Platform.runLater(new ChangeRunnable(guiControlling, replacementTeam, tableDataReplacementIn.get(i),
						tableDataReplacementOut.get(i), sReplacementMinute));
				Thread.sleep(2L * sSettings.iTimeBetweenReplacementInMiliseconds);
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}finally {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					guiControlling.changeMainScoreboardPane(new PaneMain(guiControlling.getRootPaneMainScoreboard(),controlFrame));
					replacementFrame.closeStage();
				}
			});
		}
	}

}

class ChangeRunnable implements Runnable{
	private GUIControlling guiControlling;
	private Team team;
	private Player playerIn;
	private Player playerOut;
	private String sMinute;

	public ChangeRunnable(GUIControlling guiControlling, Team team, Player playerIn, Player playerOut, String sMinute) {
		this.guiControlling = guiControlling;
		this.team = team;
		this.playerIn = playerIn;
		this.playerOut = playerOut;
		this.sMinute = sMinute;
	}

	@Override
	public void run() {
		guiControlling.changeMainScoreboardPane(new PaneSubstitution(guiControlling.getRootPaneMainScoreboard(),
				team, playerIn, playerOut, sMinute));
	}

}
