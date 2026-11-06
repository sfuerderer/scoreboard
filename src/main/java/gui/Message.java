/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import language.Language;

public class Message{
	
	public static void show(AlertType alertType, String sTitle, String sMessageKey, String sMoreInformation) {
		Alert alert = new Alert(alertType);
		alert.setTitle(sTitle);
		alert.setHeaderText(Language.getText(sMessageKey));
//		alert.setContentText(sMoreInformation==null?"Fehler beim Anzeigen der Meldung":);

		alert.showAndWait();
	}

	/**
	 * Asks the user to confirm a deletion. Returns true only if the delete button
	 * was pressed.
	 */
	public static boolean confirmDelete(String sMessageKey, String sMoreInformation) {
		ButtonType btnDelete = new ButtonType(Language.getText("loeschen"), ButtonData.OK_DONE);
		ButtonType btnCancel = new ButtonType(Language.getText("abbrechen"), ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(AlertType.CONFIRMATION, sMoreInformation == null ? "" : sMoreInformation,
				btnDelete, btnCancel);
		alert.setTitle(Language.getText("loeschen"));
		alert.setHeaderText(Language.getText(sMessageKey));

		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == btnDelete;
	}
}
