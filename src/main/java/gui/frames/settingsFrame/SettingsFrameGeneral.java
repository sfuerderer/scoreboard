package gui.frames.settingsFrame;

import gui.animation.Animations;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import language.Language;
import language.Languages;
import message.ErrorMsg;
import system.Defaults;
import system.settings.Settings;
import system.settings.Themes;

public class SettingsFrameGeneral extends SettingsComponent {
    private ChoiceBox<Languages> cbLangSelection;
    private ChoiceBox<Themes> cbThemeSelection;
    private ChoiceBox<Animations> cbAnimationSelection;
    private TextField tfHalftime1Length, tfHalftime2Length, tfHalftime3Length, tfHalftime4Length, tfTimeBetweenAd, tfTimeBetweenReplacement, tfTimeUntilBackMain;
    private CheckBox cbIsSnowing;

    public SettingsFrameGeneral(Settings sSettings) {
        super(sSettings);

        ScrollPane scrollPaneCenter = new ScrollPane();
        scrollPaneCenter.setHbarPolicy(ScrollBarPolicy.NEVER);

        GridPane centerPane = new GridPane();
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(20);
        centerPane.setVgap(20);

        Label lbTitleLanguage = new Label(Language.getText("sprache"));
        Label lbHalftime1Length = new Label(Language.getText("halbzeit_1_laenge") + " (in min)");
        Label lbHalftime2Length = new Label(Language.getText("halbzeit_2_laenge") + " (in min)");
        Label lbHalftime3Length = new Label(Language.getText("halbzeit_3_laenge") + " (in min)");
        Label lbHalftime4Length = new Label(Language.getText("halbzeit_4_laenge") + " (in min)");
        Label lbTimeBetweenAd = new Label(Language.getText("time_between_ad") + " (in ms)");
        Label lbTimeBetweenReplacement = new Label(Language.getText("time_between_replacement") + " (in ms)");
        Label lbTimeUntilBackMain = new Label(Language.getText("time_go_back_to_main") + " (in ms)");
        Label lbIsSnowing = new Label(Language.getText("is_snowing"));
        Label lbTheme = new Label(Language.getText("theme") + " (Beta)");
        Label lbAnimation = new Label(Language.getText("animation"));

        // Language selection
        cbLangSelection = new ChoiceBox<Languages>();
        cbLangSelection.getItems().setAll(Languages.values());
        cbLangSelection.getSelectionModel().clearAndSelect(sSettings.sSystemLanguage.getIId());

        cbThemeSelection = new ChoiceBox<Themes>();
        cbThemeSelection.getItems().setAll(Themes.values());
        cbThemeSelection.getSelectionModel().clearAndSelect(sSettings.theme.getIId());

        cbAnimationSelection = new ChoiceBox<Animations>();
        cbAnimationSelection.getItems().setAll(Animations.values());
        cbAnimationSelection.setConverter(new StringConverter<Animations>() {
            @Override
            public String toString(Animations animation) {
                return animation == null ? "" : Language.getText(animation.getsLanguageKey());
            }

            @Override
            public Animations fromString(String sText) {
                return null;
            }
        });
        cbAnimationSelection.getSelectionModel().select(sSettings.animation);

        tfHalftime1Length = new TextField(sSettings.iHalftime1Length + "");
        tfHalftime2Length = new TextField(sSettings.iHalftime2Length + "");
        tfHalftime3Length = new TextField(sSettings.iHalftime3Length + "");
        tfHalftime4Length = new TextField(sSettings.iHalftime4Length + "");
        tfTimeBetweenAd = new TextField(sSettings.iTimeBetweenAdvertisingInMiliseconds + "");
        tfTimeBetweenReplacement = new TextField(sSettings.iTimeBetweenReplacementInMiliseconds + "");
        tfTimeUntilBackMain = new TextField(sSettings.iTimeUntilGoToMainInMiliseconds + "");

        cbIsSnowing = new CheckBox();
        cbIsSnowing.setSelected(sSettings.bSnowing);


        centerPane.add(lbTitleLanguage, 0, 0);
        centerPane.add(cbLangSelection, 1, 0);

        centerPane.add(lbHalftime1Length, 0, 2);
        centerPane.add(tfHalftime1Length, 1, 2);

        centerPane.add(lbHalftime2Length, 0, 3);
        centerPane.add(tfHalftime2Length, 1, 3);

        centerPane.add(lbHalftime3Length, 0, 4);
        centerPane.add(tfHalftime3Length, 1, 4);

        centerPane.add(lbHalftime4Length, 0, 5);
        centerPane.add(tfHalftime4Length, 1, 5);

        centerPane.add(lbTimeBetweenAd, 0, 6);
        centerPane.add(tfTimeBetweenAd, 1, 6);

        centerPane.add(lbTimeBetweenReplacement, 0, 7);
        centerPane.add(tfTimeBetweenReplacement, 1, 7);

        centerPane.add(lbTimeUntilBackMain, 0, 8);
        centerPane.add(tfTimeUntilBackMain, 1, 8);

        centerPane.add(lbIsSnowing, 0, 9);
        centerPane.add(cbIsSnowing, 1, 9);

        centerPane.add(lbTheme, 0, 10);
        centerPane.add(cbThemeSelection, 1, 10);

        centerPane.add(lbAnimation, 0, 11);
        centerPane.add(cbAnimationSelection, 1, 11);

        scrollPaneCenter.setContent(centerPane);

        getChildren().addAll(scrollPaneCenter);
    }

    @Override
    public void saveToSettings() {
//		boolean langChanged = sSettings.sSystemLanguage != cbLangSelection.getSelectionModel().getSelectedItem();
        sSettings.sSystemLanguage = cbLangSelection.getSelectionModel().getSelectedItem();
        sSettings.bSnowing = cbIsSnowing.isSelected();
        sSettings.theme = cbThemeSelection.getSelectionModel().getSelectedItem();
        sSettings.animation = cbAnimationSelection.getSelectionModel().getSelectedItem();
        try {
            sSettings.iHalftime1Length = Integer.parseInt(tfHalftime1Length.getText());
            sSettings.iHalftime2Length = Integer.parseInt(tfHalftime2Length.getText());
            sSettings.iHalftime3Length = Integer.parseInt(tfHalftime3Length.getText());
            sSettings.iHalftime4Length = Integer.parseInt(tfHalftime4Length.getText());
            sSettings.iTimeBetweenAdvertisingInMiliseconds = Integer.parseInt(tfTimeBetweenAd.getText());
            sSettings.iTimeBetweenReplacementInMiliseconds = Integer.parseInt(tfTimeBetweenReplacement.getText());
            sSettings.iTimeUntilGoToMainInMiliseconds = Integer.parseInt(tfTimeUntilBackMain.getText());
        } catch (NumberFormatException nfe) {
            ErrorMsg.showErrorMessage(205, "Es wurden daher nur gültige Werte gespeichert");
        }
    }

    @Override
    public void reset() {
        tfHalftime1Length.setText(sSettings.iHalftime1Length + "");
        tfHalftime2Length.setText(sSettings.iHalftime2Length + "");
        tfHalftime3Length.setText(sSettings.iHalftime3Length + "");
        tfHalftime4Length.setText(sSettings.iHalftime4Length + "");
        tfTimeBetweenAd.setText(sSettings.iTimeBetweenAdvertisingInMiliseconds + "");
        tfTimeBetweenReplacement.setText(sSettings.iTimeBetweenReplacementInMiliseconds + "");
        tfTimeUntilBackMain.setText(sSettings.iTimeUntilGoToMainInMiliseconds + "");
        cbAnimationSelection.getSelectionModel().select(sSettings.animation);
    }

    @Override
    public void restoreDefaultValues() {
        tfHalftime1Length.setText(Defaults.HALFTIME_1_LENGTH + "");
        tfHalftime2Length.setText(Defaults.HALFTIME_2_LENGTH + "");
        tfHalftime3Length.setText(Defaults.HALFTIME_3_LENGTH + "");
        tfHalftime4Length.setText(Defaults.HALFTIME_4_LENGTH + "");
        tfTimeBetweenAd.setText(Defaults.TIME_BETWEEN_ADVERTISING_IN_MILISECONDS + "");
        tfTimeBetweenReplacement.setText(Defaults.TIME_BETWEEN_REPLACEMENT_IN_MILISECONDS + "");
        tfTimeUntilBackMain.setText(Defaults.TIME_UNTIL_GO_TO_MAIN_IN_MILISECONDS + "");
        cbAnimationSelection.getSelectionModel().select(Defaults.ANIMATION);
    }

    @Override
    public String getTitleKey() {
        return "allgemein";
    }
}
