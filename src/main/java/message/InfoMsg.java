/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package message;

import javax.swing.JOptionPane;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InfoMsg {
	private static boolean bGuiStarted;

	public static void init() {
		bGuiStarted = false;
	}

	public static void setBGuiStarted(boolean bGuiStarted) {
		InfoMsg.bGuiStarted = bGuiStarted;
	}

	public static void showMessage(String sInfoMessage) {
		showMessage(sInfoMessage, null);
	}

	
	public static void showMessage(String sInfoMessage, String sMoreInformation) {
		if (!bGuiStarted) {
			showMessageSwing(sInfoMessage);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Fehler");
			alert.setHeaderText(sInfoMessage == null ? "" : sInfoMessage);
			alert.setContentText(sMoreInformation);

			alert.showAndWait();
		}
	}

	private static void showMessageSwing(String sMessage) {
		JOptionPane.showMessageDialog(null, sMessage == null ? "" : sMessage, "Information",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
