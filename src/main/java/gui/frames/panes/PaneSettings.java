/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PaneSettings extends PaneMainScoreboard {
	private final int I_STORKE_WIDTH = 4;
	private Canvas canvas;
	private List<Color> firstLine = new ArrayList<Color>(
			Arrays.asList(Color.rgb(197, 197, 197), Color.rgb(255, 255, 0), Color.rgb(0, 255, 255),
					Color.rgb(0, 255, 0), Color.rgb(255, 0, 255), Color.rgb(255, 0, 0), Color.rgb(0, 0, 255)));
	private List<Color> secondLine = new ArrayList<Color>(
			Arrays.asList(Color.rgb(0, 0, 255), Color.rgb(0, 0, 0), Color.rgb(255, 0, 255), Color.rgb(0, 0, 0),
					Color.rgb(0, 255, 255), Color.rgb(0, 0, 0), Color.rgb(197, 197, 197)));

	public PaneSettings(Pane rootPane) {
		super(rootPane, false);

		BorderPane panePlayer = new BorderPane();
		panePlayer.prefWidthProperty().bind(widthProperty());
		panePlayer.prefHeightProperty().bind(heightProperty());
		panePlayer.setStyle("-fx-background-color: #000000;");

		canvas = new Canvas(getPrefWidth(), getPrefHeight());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		int iBarWidth = (int) (canvas.getWidth() / firstLine.size()),
				iBarHeightFirstLine = (int) (canvas.getHeight() * 0.8),iBarHeightSecondLine = (int) (canvas.getHeight() * 0.2);
		int iIndex = 0;
		for (Color color : firstLine)
			fillRect(gc, iBarWidth * iIndex++, 0, iBarWidth, iBarHeightFirstLine, color);
		
		iIndex=0;
		for (Color color : secondLine)
			fillRect(gc, iBarWidth * iIndex++, iBarHeightFirstLine, iBarWidth, iBarHeightSecondLine, color);


		panePlayer.setCenter(canvas);

		getChildren().add(panePlayer);
	}

	private void drawRect(GraphicsContext gc, int iXPos, int iYPos, int iWidth, int iHeight, Color color) {
		gc.setStroke(color);
		gc.setLineWidth(I_STORKE_WIDTH);
		gc.strokeRect(iXPos, iYPos, iWidth, iHeight);
	}

	private void drawRect(GraphicsContext gc, int iXPos, int iYPos, int iWidth, int iHeight) {
		drawRect(gc, iXPos, iYPos, iWidth, iHeight, Color.WHITE);
	}

	private void fillRect(GraphicsContext gc, int iXPos, int iYPos, int iWidth, int iHeight, Color color) {
		gc.setFill(color);
		gc.fillRect(iXPos, iYPos, iWidth, iHeight);
	}

	private void fillRect(GraphicsContext gc, int iXPos, int iYPos, int iWidth, int iHeight) {
		fillRect(gc, iXPos, iYPos, iWidth, iHeight, Color.WHITE);
	}
}
