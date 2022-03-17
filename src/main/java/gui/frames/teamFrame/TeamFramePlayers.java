/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.teamFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import exceptions.FileNotCreatedException;
import football.Team;
import football.person.Player;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import language.Language;
import message.ErrorMsg;
import message.InfoMsg;
import system.Images;
import system.Main;
import system.Resetable;

public class TeamFramePlayers extends GridPane implements Resetable {

	private ImageView ivPlayerImage;
	private TextField tvPlayerPrename, tvPlayerLastname, tvPlayerNumber, tfNewPlayerPrename, tfNewPlayerLastname,
			tfNewPlayerNumber;
	private File playerImage;
	private ObservableList<Player> tableDataPlayers;
	private Team team;

	@SuppressWarnings("unchecked")
	public TeamFramePlayers(Team team) {
		this.team = team;

		List<Player> players = team.getlPlayers();

		for (Player player : players)
			player.callAfterDeserialization();

		tableDataPlayers = FXCollections.observableArrayList(players);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setPadding(new Insets(0, 5, 0, 0));
		scrollPane.setStyle("-fx-background-color:transparent;");
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);

		GridPane gridpaneCurrentPlayer = new GridPane();
		gridpaneCurrentPlayer.setHgap(20);
		gridpaneCurrentPlayer.setVgap(20);

		ColumnConstraints gridpaneCurrentPlayerCol1 = new ColumnConstraints();
		gridpaneCurrentPlayerCol1.setPercentWidth(50);
		ColumnConstraints gridpaneCurrentPlayerCol2 = new ColumnConstraints();
		gridpaneCurrentPlayerCol2.setPercentWidth(50);
		gridpaneCurrentPlayer.getColumnConstraints().addAll(gridpaneCurrentPlayerCol1, gridpaneCurrentPlayerCol2);

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

		TableView<Player> playerTable = new TableView<Player>();

		TableColumn<Player, Integer> playerFirstColum = new TableColumn<Player, Integer>(Language.getText("nummer"));
		playerFirstColum.setMinWidth(100);
		playerFirstColum.setCellValueFactory(new PropertyValueFactory<Player, Integer>("siNumber"));

		TableColumn<Player, String> playerSecondColum = new TableColumn<Player, String>(Language.getText("vorname"));
		playerSecondColum.setMinWidth(100);
		playerSecondColum.setCellValueFactory(new PropertyValueFactory<Player, String>("sFirstname"));

		TableColumn<Player, String> playerThirdColum = new TableColumn<Player, String>(Language.getText("nachname"));
		playerThirdColum.setMinWidth(100);
		playerThirdColum.setCellValueFactory(new PropertyValueFactory<Player, String>("sLastname"));

		playerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		playerFirstColum.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 50% width
		playerSecondColum.setMaxWidth(1f * Integer.MAX_VALUE * 45); // 50% width
		playerThirdColum.setMaxWidth(1f * Integer.MAX_VALUE * 45); // 50% width

		playerTable.setItems(tableDataPlayers);
		playerTable.getColumns().addAll(playerFirstColum, playerSecondColum, playerThirdColum);

		tfNewPlayerPrename = new TextField();
		tfNewPlayerPrename.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addNewPlayer(tableDataPlayers);
			}
		});
		tfNewPlayerLastname = new TextField();
		tfNewPlayerLastname.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addNewPlayer(tableDataPlayers);
			}
		});
		tfNewPlayerNumber = new TextField();
		tfNewPlayerNumber.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addNewPlayer(tableDataPlayers);
			}
		});

		ivPlayerImage = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
		ivPlayerImage.setFitHeight(100);
		ivPlayerImage.setPreserveRatio(true);

		Label lbTitlePrename1 = new Label(Language.getText("vorname"));
		Label lbTitleLastname1 = new Label(Language.getText("nachname"));
		Label lbTitlePrename2 = new Label(Language.getText("vorname"));
		Label lbTitleLastname2 = new Label(Language.getText("nachname"));
		Label lbTitleNumber = new Label(Language.getText("nummer"));
		tvPlayerPrename = new TextField();
		tvPlayerLastname = new TextField();
		tvPlayerNumber = new TextField();

		Button btnAddPlayer = new Button(Language.getText("hinzufuegen"));
		btnAddPlayer.setMaxWidth(Double.MAX_VALUE);
		btnAddPlayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addNewPlayer(tableDataPlayers);
			}
		});

		final FileChooser fileChooserPlayerImage = new FileChooser();
		Button btnLoadImage = new Button(Language.getText("lade_bild"));
		btnLoadImage.setMaxWidth(Double.MAX_VALUE);
		btnLoadImage.setDisable(true);
		btnLoadImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playerImage = fileChooserPlayerImage.showOpenDialog(null);
				try {
					ivPlayerImage.setImage(new Image(new FileInputStream(playerImage.getAbsolutePath())));
				} catch (FileNotFoundException e) {
					System.out.println("Fehler beim direkten laden des Bildes");
				}
			}
		});
		
		Button btnResetImage = new Button(Language.getText("reset_image"));
		btnResetImage.setMaxWidth(Double.MAX_VALUE);
		btnResetImage.setDisable(true);
		btnResetImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playerImage = null;
				ivPlayerImage.setImage(Images.getImages().loadImageFromResource("no-image", "png"));
			}
		});

		Button btnSavePlayer = new Button(Language.getText("speichern"));
		btnSavePlayer.setDisable(true);
		btnSavePlayer.setMaxWidth(Double.MAX_VALUE);
		btnSavePlayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Player currentPlayer = playerTable.getSelectionModel().getSelectedItem();
					if (playerImage != null) {
						try {
							String sPlayerImagePath = Images.getImages().movePlayerImage(team.getPathToFolder(),
									playerImage, currentPlayer);
							currentPlayer.setsImagePath(sPlayerImagePath);
						} catch (FileNotCreatedException | IOException e) {
							ErrorMsg.showErrorMessage(110);
						}
					}else {
						currentPlayer.setsImagePath(null);
					}

					currentPlayer.setsFirstname(tvPlayerPrename.getText());
					currentPlayer.setsLastname(tvPlayerLastname.getText());
					currentPlayer.setiNumber(Integer.parseInt(tvPlayerNumber.getText()));
					team.save();
					updatePlayerInformations(currentPlayer);
					Main.teamsChanged();
					InfoMsg.showMessage(Language.getText("spieler_gespeichert"));
				} catch (NumberFormatException nfe) {
					ErrorMsg.showErrorMessage(201);
				}
			}
		});

		Button btnDeletePlayer = new Button(Language.getText("loeschen_spieler"));
		btnDeletePlayer.setDisable(true);
		btnDeletePlayer.setMaxWidth(Double.MAX_VALUE);
		btnDeletePlayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Player currentPlayer = playerTable.getSelectionModel().getSelectedItem();
				team.removePlayer(currentPlayer);
				team.save();
				tableDataPlayers.remove(currentPlayer);
			}
		});

		playerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				updatePlayerInformations(newSelection);
				btnSavePlayer.setDisable(false);
				btnLoadImage.setDisable(false);
				btnResetImage.setDisable(false);
				btnDeletePlayer.setDisable(false);
			} else {
				btnSavePlayer.setDisable(true);
				btnLoadImage.setDisable(true);
				btnResetImage.setDisable(true);
				btnDeletePlayer.setDisable(true);
			}
		});

		gridpaneCurrentPlayer.add(ivPlayerImage, 0, 0, 2, 1);
		gridpaneCurrentPlayer.add(btnResetImage, 0, 1);
		gridpaneCurrentPlayer.add(btnLoadImage, 1, 1);
		gridpaneCurrentPlayer.add(lbTitlePrename2, 0, 2);
		gridpaneCurrentPlayer.add(tvPlayerPrename, 0, 3);
		gridpaneCurrentPlayer.add(lbTitleLastname2, 1, 2);
		gridpaneCurrentPlayer.add(tvPlayerLastname, 1, 3);
		gridpaneCurrentPlayer.add(lbTitleNumber, 0, 4, 2, 1);
		gridpaneCurrentPlayer.add(tvPlayerNumber, 0, 5, 2, 1);
		gridpaneCurrentPlayer.add(btnDeletePlayer, 0, 6, 2, 1);
		gridpaneCurrentPlayer.add(btnSavePlayer, 0, 7, 2, 1);

		scrollPane.setContent(gridpaneCurrentPlayer);

		add(lbTitlePrename1, 0, 0);
		add(lbTitleLastname1, 1, 0);
		add(tfNewPlayerPrename, 0, 1);
		add(tfNewPlayerLastname, 1, 1);
		add(tfNewPlayerNumber, 2, 1);
		add(btnAddPlayer, 3, 1, 1, 1);
		add(playerTable, 0, 2, 2, 1);
		add(scrollPane, 2, 2, 2, 1);
	}

	private void addNewPlayer(ObservableList<Player> tableDataPlayers) {
		int number;
		try {
			number = Integer.parseInt(tfNewPlayerNumber.getText());
		} catch (NumberFormatException nfe) {
			number = -1;
		}
		Player newPlayer = new Player(tfNewPlayerPrename.getText(), tfNewPlayerLastname.getText(), number, null);
		team.addPlayer(newPlayer);
		team.save();
		tableDataPlayers.add(newPlayer);
		tfNewPlayerPrename.clear();
		tfNewPlayerLastname.clear();
		tfNewPlayerNumber.clear();
		tfNewPlayerPrename.requestFocus();
		Main.teamsChanged();
	}

	private void updatePlayerInformations(Player currentPlayer) {
		if (currentPlayer == null) {
			tvPlayerPrename.setText("");
			tvPlayerLastname.setText("");
			tvPlayerNumber.setText("");
			ivPlayerImage.setImage(Images.getImages().loadImageFromResource("no-image", "png"));
			return;
		}
		if (currentPlayer.getsImagePath() != null) {
			try {
				playerImage = new File(currentPlayer.getsImagePath());
				ivPlayerImage.setImage(Images.getImages().loadImageFromPath(currentPlayer.getsImagePath()));
			} catch (MalformedURLException e) {
				ErrorMsg.showErrorMessage(112);
			}
		} else {
			playerImage = null;
			ivPlayerImage.setImage(Images.getImages().loadImageFromResource("no-image", "png"));
		}
		tvPlayerPrename.setText(currentPlayer.getsFirstname());
		tvPlayerLastname.setText(currentPlayer.getsLastname());
		tvPlayerNumber.setText(currentPlayer.getiNumber() + "");
	}

	@Override
	public void reset() {

		tableDataPlayers.setAll(team.getlPlayers());

		updatePlayerInformations(null);
	}

}
