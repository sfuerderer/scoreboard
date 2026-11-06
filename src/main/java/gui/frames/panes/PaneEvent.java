/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.frames.panes;

import java.util.ArrayList;
import java.util.List;

import gui.frames.panes.components.BuildIn;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import system.Main;

/**
 * Base of the event views (goal, card, substitution, introduction of a player). The elements are placed at
 * fixed distances from the edges of the wall and slide in one after the other when the view appears.
 */
public abstract class PaneEvent extends PaneMainScoreboard {
	private final StackPane stage = new StackPane();
	private final List<Node> lBuildIn = new ArrayList<Node>();

	protected PaneEvent(Pane rootPane) {
		super(ScoreboardBackground.crestDiagonal(), rootPane, false);
		stage.prefWidthProperty().bind(widthProperty());
		stage.prefHeightProperty().bind(heightProperty());
		getChildren().add(stage);

		// Photos and the large number reach beyond the wall; without a clip they would cover other panes
		// during the transition
		Rectangle clip = new Rectangle();
		clip.widthProperty().bind(widthProperty());
		clip.heightProperty().bind(heightProperty());
		setClip(clip);
	}

	/**
	 * Places the node with its top left corner at the given distance from the top left corner of the wall.
	 */
	protected <T extends Node> T placeTopLeft(T node, double dTop, double dLeft) {
		return place(node, Pos.TOP_LEFT, new Insets(dTop, 0, 0, dLeft));
	}

	/**
	 * Places the node with its top right corner at the given distance from the top right corner of the wall.
	 */
	protected <T extends Node> T placeTopRight(T node, double dTop, double dRight) {
		return place(node, Pos.TOP_RIGHT, new Insets(dTop, dRight, 0, 0));
	}

	private <T extends Node> T place(T node, Pos position, Insets margin) {
		StackPane.setAlignment(node, position);
		StackPane.setMargin(node, margin);
		stage.getChildren().add(node);
		return node;
	}

	/**
	 * Lets the nodes slide in when the view appears, in the order they are given.
	 */
	protected void buildIn(Node... nodes) {
		for (Node node : nodes)
			if (node != null)
				lBuildIn.add(node);
	}

	/**
	 * Large outlined shirt number behind a player, e.g. on the goal view.
	 */
	protected static Text outlineNumber(int iNumber, int iSize) {
		Text number = new Text(iNumber < 0 ? "" : String.valueOf(iNumber));
		number.setFont(Main.getMainFont(FontWeight.EXTRA_BOLD, iSize));
		number.setFill(javafx.scene.paint.Color.TRANSPARENT);
		number.setStroke(ScoreboardColors.NUMBER_OUTLINE);
		number.setStrokeWidth(3);
		return number;
	}

	@Override
	public void paneWillAppear() {
		super.paneWillAppear();
		// Started before the transition, so the elements build up while the view slides or fades in
		BuildIn.play(lBuildIn.toArray(new Node[0]));
	}
}
