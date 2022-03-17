/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.mediaFrame;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gui.GUIControlling;
import gui.frames.BasicFrame;
import gui.frames.ControlFrame;
import gui.frames.panes.PaneImage;
import gui.frames.panes.PaneVideo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import language.Language;
import message.ErrorMsg;
import system.Main;
import system.Medias;
import system.settings.Settings;

public class MediaFrame extends BasicFrame {
	public MediaFrame(Settings sSettings, GUIControlling guiControlling, ControlFrame controlFrame,
			MediaType mediaType) {
		super(sSettings, guiControlling, "media_anzeigen", new ArrayList<Object>() {
			/**
			* 
			*/
			private static final long serialVersionUID = -1926678140774600772L;

			{
				add(controlFrame);
				add(mediaType);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Pane buildCenterPane(Object defaultParams) {
		if (Main.getMatch() == null) {
			ErrorMsg.showErrorMessage(206);
			return null;
		}
		
		ArrayList<Object> params = (ArrayList<Object>) defaultParams;

		BorderPane centerPane = new BorderPane();

		ScrollPane spAllMedias = new ScrollPane();
		spAllMedias.setHbarPolicy(ScrollBarPolicy.NEVER);
		spAllMedias.setFitToWidth(true);
		spAllMedias.setFitToHeight(true);

		spAllMedias.setContent(buildAllMedias((MediaType)params.get(1), (ControlFrame)params.get(0)));

		centerPane.setCenter(spAllMedias);

		return centerPane;
	}

	@Override
	protected Pane buildBottomPane() {
		return null;
	}

	private VBox buildAllMedias(MediaType mediaType, ControlFrame controlFrame) {
		VBox allMedias = new VBox();

		File mediaFolder = new File("media");
		File[] allFiles = mediaFolder.listFiles();

		for (File file : allFiles) {
			if (!checkFiletypeCorrect(mediaType, file))
				continue;

			GridPane gpMediaLine = new GridPane();
			gpMediaLine.setPadding(new Insets(5));
			gpMediaLine.setHgap(5);
			gpMediaLine.setVgap(10);

			ColumnConstraints gpPlayerLineCol1 = new ColumnConstraints();
			gpPlayerLineCol1.setPercentWidth(50);
			ColumnConstraints gpPlayerLineCol2 = new ColumnConstraints();
			gpPlayerLineCol2.setPercentWidth(50);

			gpMediaLine.getColumnConstraints().addAll(gpPlayerLineCol1, gpPlayerLineCol2);

			Label lbMediaName = new Label(file.getName());

			Button btnShow = new Button(Language.getText("anzeigen"));
			btnShow.setMaxWidth(Double.MAX_VALUE);
			btnShow.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						switch (mediaType) {
						case PICTURE:
							controlFrame.changeMainScoreboardPane(new PaneImage(controlFrame.getRootPaneMainScoreboard(),file.getAbsolutePath()));
							break;
						case VIDEO:
							controlFrame.changeMainScoreboardPane(new PaneVideo(controlFrame.getRootPaneMainScoreboard(),
									Medias.loadMediaFromPath(file.getAbsolutePath()), false));
							break;
						}
						
					} catch (MalformedURLException e) {
						ErrorMsg.showErrorMessage(114);
					}
				}
			});

			gpMediaLine.add(lbMediaName, 0, 0);
			gpMediaLine.add(btnShow, 1, 0);

			allMedias.getChildren().add(gpMediaLine);
		}

		return allMedias;
	}

	private boolean checkFiletypeCorrect(MediaType mediaType, File file) {
		String fileExtension = getFileExtension(file);
		List<String> supportedExtensions = new ArrayList<String>();
	
		switch (mediaType) {
		case PICTURE:
			supportedExtensions.addAll(Arrays.asList(".jpg",".png"));
			break;
		case VIDEO:
			supportedExtensions.addAll(Arrays.asList(".mp4"));
			break;
		default:
			return false;
		}

		return supportedExtensions.contains(fileExtension);
	}

	private String getFileExtension(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1)
			return "";
		return name.substring(lastIndexOf);
	}
}
