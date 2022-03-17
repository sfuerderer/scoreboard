/************************************************
 * Copyright (c) 2021 Sebastian Fürderer       **
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
import football.person.Coach;
import football.person.CoachType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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

public class TeamFrameCoaches extends GridPane implements Resetable {

	private ImageView ivCoachImage;
	private TextField tvCoachPrename, tvCoachLastname, tfNewCoachPrename, tfNewCoachLastname;
	private ChoiceBox<CoachType> cbCoachType;
	private File coachImage;
	private ObservableList<Coach> tableDataCoaches;
	private Team team;

	@SuppressWarnings("unchecked")
	public TeamFrameCoaches(Team team) {
		this.team = team;

		List<Coach> coaches = team.getlCoaches();

		for (Coach coach : coaches)
			coach.callAfterDeserialization();

		tableDataCoaches = FXCollections.observableArrayList(coaches);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setPadding(new Insets(0, 5, 0, 0));
		scrollPane.setStyle("-fx-background-color:transparent;");
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);

		GridPane gridpaneCurrentCoach = new GridPane();
		gridpaneCurrentCoach.setHgap(20);
		gridpaneCurrentCoach.setVgap(20);

		ColumnConstraints gridpaneCurrentCoachCol1 = new ColumnConstraints();
		gridpaneCurrentCoachCol1.setPercentWidth(50);
		ColumnConstraints gridpaneCurrentCoachCol2 = new ColumnConstraints();
		gridpaneCurrentCoachCol2.setPercentWidth(50);
		gridpaneCurrentCoach.getColumnConstraints().addAll(gridpaneCurrentCoachCol1, gridpaneCurrentCoachCol2);

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

		TableView<Coach> coachTable = new TableView<Coach>();

		TableColumn<Coach, String> coachFirstColum = new TableColumn<Coach, String>(Language.getText("vorname"));
		coachFirstColum.setMinWidth(100);
		coachFirstColum.setCellValueFactory(new PropertyValueFactory<Coach, String>("sFirstname"));

		TableColumn<Coach, String> coachSecondColum = new TableColumn<Coach, String>(Language.getText("nachname"));
		coachSecondColum.setMinWidth(100);
		coachSecondColum.setCellValueFactory(new PropertyValueFactory<Coach, String>("sLastname"));

		coachTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		coachFirstColum.setMaxWidth(1f * Integer.MAX_VALUE * 45);
		coachSecondColum.setMaxWidth(1f * Integer.MAX_VALUE * 45);

		coachTable.setItems(tableDataCoaches);
		coachTable.getColumns().addAll(coachFirstColum, coachSecondColum);

		tfNewCoachPrename = new TextField();
		tfNewCoachPrename.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addNewCoach(tableDataCoaches);
			}
		});
		tfNewCoachLastname = new TextField();
		tfNewCoachLastname.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addNewCoach(tableDataCoaches);
			}
		});

		ivCoachImage = new ImageView(Images.getImages().loadImageFromResource("no-image", "png"));
		ivCoachImage.setFitHeight(100);
		ivCoachImage.setPreserveRatio(true);

		Label lbTitlePrename1 = new Label(Language.getText("vorname"));
		Label lbTitleLastname1 = new Label(Language.getText("nachname"));
		Label lbTitlePrename2 = new Label(Language.getText("vorname"));
		Label lbTitleLastname2 = new Label(Language.getText("nachname"));
		tvCoachPrename = new TextField();
		tvCoachLastname = new TextField();

		Button btnAddCoach = new Button(Language.getText("hinzufuegen"));
		btnAddCoach.setMaxWidth(Double.MAX_VALUE);
		btnAddCoach.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addNewCoach(tableDataCoaches);
			}
		});

		final FileChooser fileChooserPlayerImage = new FileChooser();
		Button btnLoadImage = new Button(Language.getText("lade_bild"));
		btnLoadImage.setMaxWidth(Double.MAX_VALUE);
		btnLoadImage.setDisable(true);
		btnLoadImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				coachImage = fileChooserPlayerImage.showOpenDialog(null);
				try {
					ivCoachImage.setImage(new Image(new FileInputStream(coachImage.getAbsolutePath())));
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
				coachImage = null;
				ivCoachImage.setImage(Images.getImages().loadImageFromResource("no-image", "png"));
			}
		});

		Button btnSaveCoach = new Button(Language.getText("speichern"));
		btnSaveCoach.setDisable(true);
		btnSaveCoach.setMaxWidth(Double.MAX_VALUE);
		btnSaveCoach.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Coach currentCoach = coachTable.getSelectionModel().getSelectedItem();
					Coach headCoach = team.getHeadCoach();
					if (headCoach == null || (headCoach != null && cbCoachType.getSelectionModel().getSelectedItem() != CoachType.HEADCOACH  ) || headCoach == currentCoach) {
						if (coachImage != null) {
							try {
								String sCoachImagePath = Images.getImages().moveCoachImage(team.getPathToFolder(),
										coachImage, currentCoach);
								currentCoach.setsImagePath(sCoachImagePath);
							} catch (FileNotCreatedException | IOException e) {
								ErrorMsg.showErrorMessage(110);
							}
						} else {
							currentCoach.setsImagePath(null);
						}

						currentCoach.setsFirstname(tvCoachPrename.getText());
						currentCoach.setsLastname(tvCoachLastname.getText());
						currentCoach.seteCoachType(cbCoachType.getSelectionModel().getSelectedItem());
						team.save();
						updateCoachInformations(currentCoach);
						Main.teamsChanged();
						InfoMsg.showMessage(Language.getText("trainer_gespeichert"));
					} else {
						ErrorMsg.showErrorMessage(211);
					}
				} catch (NumberFormatException nfe) {
					ErrorMsg.showErrorMessage(201);
				}
			}
		});

		Button btnDeletePlayer = new Button(Language.getText("loeschen_trainer"));
		btnDeletePlayer.setDisable(true);
		btnDeletePlayer.setMaxWidth(Double.MAX_VALUE);
		btnDeletePlayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Coach currentCoach = coachTable.getSelectionModel().getSelectedItem();
				team.removeCoach(currentCoach);
				team.save();
				tableDataCoaches.remove(currentCoach);
			}
		});

		coachTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				updateCoachInformations(newSelection);
				btnSaveCoach.setDisable(false);
				btnLoadImage.setDisable(false);
				btnResetImage.setDisable(false);
				btnDeletePlayer.setDisable(false);
				cbCoachType.setDisable(false);
			} else {
				btnSaveCoach.setDisable(true);
				btnLoadImage.setDisable(true);
				btnResetImage.setDisable(true);
				btnDeletePlayer.setDisable(true);
				cbCoachType.setDisable(true);
			}
		});

		Label lbCoachType = new Label(Language.getText("trainer_art"));

		cbCoachType = new ChoiceBox<CoachType>();
		cbCoachType.getItems().setAll(CoachType.values());
		cbCoachType.setDisable(true);

		gridpaneCurrentCoach.add(ivCoachImage, 0, 0, 2, 1);
		gridpaneCurrentCoach.add(btnResetImage, 0, 1);
		gridpaneCurrentCoach.add(btnLoadImage, 1, 1);
		gridpaneCurrentCoach.add(lbTitlePrename2, 0, 2);
		gridpaneCurrentCoach.add(tvCoachPrename, 0, 3);
		gridpaneCurrentCoach.add(lbTitleLastname2, 1, 2);
		gridpaneCurrentCoach.add(tvCoachLastname, 1, 3);
		gridpaneCurrentCoach.add(lbCoachType, 0, 4);
		gridpaneCurrentCoach.add(cbCoachType, 1, 4);
		gridpaneCurrentCoach.add(btnDeletePlayer, 0, 5, 2, 1);
		gridpaneCurrentCoach.add(btnSaveCoach, 0, 6, 2, 1);

		scrollPane.setContent(gridpaneCurrentCoach);

		add(lbTitlePrename1, 0, 0);
		add(lbTitleLastname1, 1, 0);
		add(tfNewCoachPrename, 0, 1);
		add(tfNewCoachLastname, 1, 1);
		add(btnAddCoach, 3, 1, 1, 1);
		add(coachTable, 0, 2, 2, 8);
		add(scrollPane, 2, 2, 2, 8);
	}

	private void addNewCoach(ObservableList<Coach> tableDataCoaches) {
		Coach newCoach = new Coach(tfNewCoachPrename.getText(), tfNewCoachLastname.getText(), null);
		team.addCoach(newCoach);
		team.save();
		tableDataCoaches.add(newCoach);
		tfNewCoachPrename.clear();
		tfNewCoachLastname.clear();
		tfNewCoachPrename.requestFocus();
		Main.teamsChanged();
	}

	private void updateCoachInformations(Coach currentCoach) {
		if (currentCoach == null) {
			tvCoachPrename.setText("");
			tvCoachLastname.setText("");
			ivCoachImage.setImage(Images.getImages().loadImageFromResource("no-image", "png"));
			return;
		}
		if (currentCoach.getsImagePath() != null) {
			try {
				coachImage = new File(currentCoach.getsImagePath());
				ivCoachImage.setImage(Images.getImages().loadImageFromPath(currentCoach.getsImagePath()));
			} catch (MalformedURLException e) {
				ErrorMsg.showErrorMessage(112);
			}
		} else {
			coachImage = null;
			ivCoachImage.setImage(Images.getImages().loadImageFromResource("no-image", "png"));
		}
		tvCoachPrename.setText(currentCoach.getsFirstname());
		tvCoachLastname.setText(currentCoach.getsLastname());
		cbCoachType.getSelectionModel()
				.clearAndSelect(currentCoach.geteCoachType() == null ? -1 : currentCoach.geteCoachType().getIId());
	}

	@Override
	public void reset() {

		tableDataCoaches.setAll(team.getlCoaches());

		updateCoachInformations(null);
	}

}
