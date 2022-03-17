package gui.frames.settingsFrame;

import java.util.HashMap;
import java.util.Map;

import gui.datas.FramesTextFields;
import gui.frames.frameFunctions.OtherFramesFunctions;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import language.Language;
import system.settings.Frame;
import system.settings.Settings;

public class SettingsFrameBoards extends SettingsComponent {
	private Map<String, FramesTextFields> dataFramesTextFields;

	public SettingsFrameBoards(Settings sSettings) {
		super(sSettings);

		dataFramesTextFields = new HashMap<String, FramesTextFields>();

		ScrollPane scrollPaneCenter = new ScrollPane();
		scrollPaneCenter.setHbarPolicy(ScrollBarPolicy.NEVER);

		GridPane centerPane = new GridPane();
		centerPane.setPadding(new Insets(20));
		centerPane.setHgap(20);
		centerPane.setVgap(20);

		centerPane.add(generateFrameSettingsArea(sSettings.mainFrame, Language.getText("anzeigetafel"), "frame_id_0"),
				0, 0, 2, 1);
		int row = 1;
		for (Frame frame : sSettings.otherFrames) {
			centerPane.add(generateFrameSettingsArea(frame, Language.getText("andereFenster"), "frame_id_" + row), 0,
					row, 2, 1);
			row++;
		}

		scrollPaneCenter.setContent(centerPane);

		getChildren().addAll(scrollPaneCenter);
	}

	private GridPane generateFrameSettingsArea(Frame frame, String title, String key) {
		GridPane framePane = new GridPane();
		framePane.setHgap(20);
		framePane.setVgap(20);
		framePane.prefWidthProperty().bind(widthProperty());

		Label lbTitleMainFrame = new Label(title);
		lbTitleMainFrame.getStyleClass().add("sf-label-subheadline");

		Label lbTitleWidth = new Label(Language.getText("breite"));
		Label lbTitleHeight = new Label(Language.getText("höhe"));
		Label lbTitleStartX = new Label(Language.getText("startX"));
		Label lbTitleStartY = new Label(Language.getText("startY"));
		Label lbTitleFunction = new Label(Language.getText("funktion"));

		TextField tfWidth = new TextField(frame.iWidth + "");
		TextField tfHeight = new TextField(frame.iHeight + "");
		TextField tfStartX = new TextField(frame.iStartXPosition + "");
		TextField tfStartY = new TextField(frame.iStartYPosition + "");

		ChoiceBox<OtherFramesFunctions> cbFunctionSelection = new ChoiceBox<OtherFramesFunctions>();
		cbFunctionSelection.getItems().setAll(OtherFramesFunctions.values());
		if (frame.getOtherFrameFunction() != null)
			cbFunctionSelection.getSelectionModel().clearAndSelect(frame.getOtherFrameFunction().getIndex());

		dataFramesTextFields.put(key, new FramesTextFields(tfWidth, tfHeight, tfStartX, tfStartY));

		framePane.add(lbTitleMainFrame, 0, 0, 2, 1);
		framePane.add(lbTitleWidth, 0, 1);
		framePane.add(tfWidth, 1, 1);
		framePane.add(lbTitleHeight, 2, 1);
		framePane.add(tfHeight, 3, 1);
		framePane.add(lbTitleStartX, 0, 2);
		framePane.add(tfStartX, 1, 2);
		framePane.add(lbTitleStartY, 2, 2);
		framePane.add(tfStartY, 3, 2);
		framePane.add(lbTitleFunction, 0, 3);
		framePane.add(cbFunctionSelection, 1, 3);

		return framePane;
	}

	@Override
	public void saveToSettings() {
		// Save the main frame.
		FramesTextFields textFields = dataFramesTextFields.get("frame_id_0");
		sSettings.mainFrame.iWidth = Integer.parseInt(textFields.getTfWidth().getText());
		sSettings.mainFrame.iHeight = Integer.parseInt(textFields.getTfHeight().getText());
		sSettings.mainFrame.iStartXPosition = Integer.parseInt(textFields.getTfStartX().getText());
		sSettings.mainFrame.iStartYPosition = Integer.parseInt(textFields.getTfStartY().getText());

		// Save all other frames
		int row = 1;
		for (Frame frame : sSettings.otherFrames) {
			textFields = dataFramesTextFields.get("frame_id_" + row);
			frame.iWidth = Integer.parseInt(textFields.getTfWidth().getText());
			frame.iHeight = Integer.parseInt(textFields.getTfHeight().getText());
			frame.iStartXPosition = Integer.parseInt(textFields.getTfStartX().getText());
			frame.iStartYPosition = Integer.parseInt(textFields.getTfStartY().getText());
			row++;
		}
	}

	@Override
	public void reset() {
		dataFramesTextFields.get("frame_id_0").getTfWidth().setText(sSettings.mainFrame.iWidth + "");
		dataFramesTextFields.get("frame_id_0").getTfHeight().setText(sSettings.mainFrame.iHeight + "");
		dataFramesTextFields.get("frame_id_0").getTfStartX().setText(sSettings.mainFrame.iStartXPosition + "");
		dataFramesTextFields.get("frame_id_0").getTfStartY().setText(sSettings.mainFrame.iStartYPosition + "");
		int row = 1;
		for (Frame frame : sSettings.otherFrames) {
			dataFramesTextFields.get("frame_id_" + row).getTfWidth().setText(frame.iWidth + "");
			dataFramesTextFields.get("frame_id_" + row).getTfHeight().setText(frame.iHeight + "");
			dataFramesTextFields.get("frame_id_" + row).getTfStartX().setText(frame.iStartXPosition + "");
			dataFramesTextFields.get("frame_id_" + row).getTfStartY().setText(frame.iStartYPosition + "");
			row++;
		}
	}

	@Override
	public void restoreDefaultValues() {
		//TODO Restore Default datas
	}

	@Override
	public String getTitleKey() {
		return "boards";
	}
}
