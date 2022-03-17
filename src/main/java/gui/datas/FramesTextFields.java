/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.datas;

import javafx.scene.control.TextField;

public class FramesTextFields {
	private TextField tfWidth;
	private TextField tfHeight;
	private TextField tfStartX;
	private TextField tfStartY;

	public FramesTextFields(TextField tfWidth, TextField tfHeight, TextField tfStartX, TextField tfStartY) {
		super();
		this.tfWidth = tfWidth;
		this.tfHeight = tfHeight;
		this.tfStartX = tfStartX;
		this.tfStartY = tfStartY;
	}

	/**
	 * Returns the tfWidth.
	 *
	 * @return the tfWidth
	 */
	public TextField getTfWidth() {
		return tfWidth;
	}

	/**
	 * Sets the tfWidth to the new value.
	 *
	 * @param tfWidth the tfWidth to set
	 */
	public void setTfWidth(TextField tfWidth) {
		this.tfWidth = tfWidth;
	}

	/**
	 * Returns the tfHeight.
	 *
	 * @return the tfHeight
	 */
	public TextField getTfHeight() {
		return tfHeight;
	}

	/**
	 * Sets the tfHeight to the new value.
	 *
	 * @param tfHeight the tfHeight to set
	 */
	public void setTfHeight(TextField tfHeight) {
		this.tfHeight = tfHeight;
	}

	/**
	 * Returns the tfStartX.
	 *
	 * @return the tfStartX
	 */
	public TextField getTfStartX() {
		return tfStartX;
	}

	/**
	 * Sets the tfStartX to the new value.
	 *
	 * @param tfStartX the tfStartX to set
	 */
	public void setTfStartX(TextField tfStartX) {
		this.tfStartX = tfStartX;
	}

	/**
	 * Returns the tfStartY.
	 *
	 * @return the tfStartY
	 */
	public TextField getTfStartY() {
		return tfStartY;
	}

	/**
	 * Sets the tfStartY to the new value.
	 *
	 * @param tfStartY the tfStartY to set
	 */
	public void setTfStartY(TextField tfStartY) {
		this.tfStartY = tfStartY;
	}

}
