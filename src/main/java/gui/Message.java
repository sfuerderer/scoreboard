/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import language.Language;

public class Message{
	
	public static void show(AlertType alertType, String sTitle, String sMessageKey, String sMoreInformation) {
		Alert alert = new Alert(alertType);
		alert.setTitle(sTitle);
		alert.setHeaderText(Language.getText(sMessageKey));
//		alert.setContentText(sMoreInformation==null?"Fehler beim Anzeigen der Meldung":);

		alert.showAndWait();
	}
}
