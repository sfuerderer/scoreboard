/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package message;

import java.util.HashMap;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ErrorMsg {
	/**
	 * 1xx = System Fehler 2xx = Eingabe Fehler 3xx =
	 */
	private static final HashMap<Integer, String> ERRORS = new HashMap<Integer, String>();
	private static boolean bGuiStarted;

	public static void init() {
		bGuiStarted = false;
		ERRORS.put(100, "Die Fehlercode existiert nicht.");
		ERRORS.put(101, "Die Datei konnte nicht geladen werden.");
		ERRORS.put(102, "Die Datei konnte nicht erstellt werden.");
		ERRORS.put(103, "Die Sprachen Datei konnte nicht geladen werden.");
		ERRORS.put(104, "Der Text wurde nicht gefunden.");
		ERRORS.put(105, "Der Text wurde nicht in der Sprache gefunden.");
		ERRORS.put(106, "Die Einstellungen konnten nicht gespeichert werden");
		ERRORS.put(107, "Die Ligen Datei konnte nicht geladen werden.");
		ERRORS.put(108, "Die Ligen konnte nicht erstellt werden.");
		ERRORS.put(109, "Das Team konnte nicht geladen werden.");
		ERRORS.put(110, "Das Bild konnte nicht gespeichert werden.");
		ERRORS.put(111, "Ein oder beide Logos konnten nicht geladen werden.");
		ERRORS.put(112, "Ein Bild konnte nicht geladen werden.");
		ERRORS.put(113, "Bei einem Task ist ein problem aufgetreten.");
		ERRORS.put(114, "Das Video konnte nicht geladen werden.");
		ERRORS.put(115, "Beim starten der Software sind schwerwiegende Fehler aufgetreten. Falls ein neustart nicht hilft, die jar Datei in einen neuen Ordner ziehen und versuchen erneut zu starten.");
		ERRORS.put(116, "Ein Thread wurde unerwartet unterbrochen. Bitte die Software neu starten.");
		ERRORS.put(200, "Es muss eine Liga ausgewählt sein.");
		ERRORS.put(201, "Es muss eine Zahl als Rückenummer angegeben werden.");
		ERRORS.put(202, "Es wurde keine Mannschaft ausgewählt.");
		ERRORS.put(203, "Es dürfen maximal nur 11 Spieler auf dem Feld sein.");
		ERRORS.put(204, "Es müssen gleich viele Spieler eingewechselt wie ausgewechselt werden.");
		ERRORS.put(205, "Es muss eine Zahl angegeben werden.");
		ERRORS.put(206, "Es wurde noch kein Spiel gestartet.");
		ERRORS.put(207, "Es werden gerade noch die Auswechlungen angezeigt. Du kannst das Fenster nicht schließen.");
		ERRORS.put(208, "Es können nicht mehr als 60 Sekunden angegeben werden");
		ERRORS.put(209, "Es wurde kein Spieler oder keine Karte ausgewählt.");
		ERRORS.put(210, "Zu dieser Mannschaft wurde noch kein Trainer angelegt.");
		ERRORS.put(211, "Du kannst nur einen Haupttrainer anlegen.");
		ERRORS.put(212, "Im Ordner \"advertisment\" wurden keine Bilder (jpg, png) oder Videos (mp4) gefunden.");
	}

	public static void setBGuiStarted(boolean bGuiStarted) {
		ErrorMsg.bGuiStarted = bGuiStarted;
	}

	public static void showErrorMessage(int iErrorCode) {
		showErrorMessage(iErrorCode, null, false);
	}

	public static void showErrorMessage(int iErrorCode,boolean exit) {
		showErrorMessage(iErrorCode, null, exit);
	}
	public static void showErrorMessage(int iErrorCode, String sMoreInformation) {
		showErrorMessage(iErrorCode, sMoreInformation, false);
	}
	
	public static void showErrorMessage(int iErrorCode, String sMoreInformation,boolean exit) {
		String sMessage = ERRORS.get(iErrorCode);

		if (!bGuiStarted) {
			showErrorMessageSwing(sMessage);
			if (exit) {
				System.exit(0);
			}
		} else if (!Platform.isFxApplicationThread()) {
			// Dialogs may only be created on the JavaFX thread (e.g. errors from background tasks)
			Platform.runLater(() -> showErrorMessage(iErrorCode, sMoreInformation, exit));
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fehler");
			alert.setHeaderText(sMessage == null ? ERRORS.get(101) : sMessage);
			alert.setContentText(sMoreInformation);

			alert.showAndWait();
			if(exit) {
				System.exit(0);
			}
		}
	}

	private static void showErrorMessageSwing(String sMessage) {
		JOptionPane.showMessageDialog(null, sMessage == null ? ERRORS.get(101) : sMessage, "Fehler",
				JOptionPane.ERROR_MESSAGE);
	}
}
