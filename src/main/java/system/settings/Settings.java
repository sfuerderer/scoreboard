/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system.settings;

import com.google.gson.annotations.Expose;
import exceptions.FileNotCreatedException;
import gui.animation.Animations;
import language.Languages;
import message.ErrorMsg;
import system.Defaults;
import system.FileHandling;
import system.Main;

import java.io.IOException;

/**
 * The main Settings class.
 *
 * @author Sebastian Fürderer
 * @version 2.0.0
 */
public class Settings {
    @Expose
    public Frame mainFrame;
    @Expose
    public Frame[] otherFrames;
    @Expose
    public Languages sSystemLanguage;
    @Expose
    public Animations animation;
    @Expose
    public int iHalftime1Length;
    @Expose
    public int iHalftime2Length;
    @Expose
    public int iHalftime3Length;
    @Expose
    public int iHalftime4Length;
    @Expose
    public int iTimeBetweenAdvertisingInMiliseconds;
    @Expose
    public int iTimeBetweenReplacementInMiliseconds;
    @Expose
    public int iTimeUntilGoToMainInMiliseconds;
    @Expose
    public boolean bSnowing;
    @Expose
    public GoalAnimation sGoalAnimation;
    @Expose
    public Themes theme;
    @Expose
    public String version;

    public Settings(String version, Frame mainFrame, Languages eSystemLanguage, Animations animation,
                    int iHalftime1Length, int iHalftime2Length, int iHalftime3Length, int iHalftime4Length,
                    int iTimeBetweenAdvertisingInMiliseconds, int iTimeBetweenReplacementInMiliseconds,
                    int iTimeUntilGoToMainInMiliseconds, boolean bSnowing, Themes theme, GoalAnimation sGoalAnimation) {
        this(version, mainFrame, null, eSystemLanguage, animation, iHalftime1Length, iHalftime2Length, iHalftime3Length,
                iHalftime4Length, iTimeBetweenAdvertisingInMiliseconds, iTimeBetweenReplacementInMiliseconds,
                iTimeUntilGoToMainInMiliseconds, bSnowing, theme, sGoalAnimation);
    }

    public Settings(String version, Frame mainFrame, Frame[] otherFrames, Languages eSystemLanguage,
                    Animations animation, int iHalftime1Length, int iHalftime2Length, int iHalftime3Length,
                    int iHalftime4Length, int iTimeBetweenAdvertisingInMiliseconds, int iTimeBetweenReplacementInMiliseconds,
                    int iTimeUntilGoToMainInMiliseconds, boolean bSnowing, Themes theme, GoalAnimation sGoalAnimation) {
        this.version = version;
        this.mainFrame = mainFrame;
        this.otherFrames = otherFrames;
        this.sSystemLanguage = eSystemLanguage;
        this.animation = animation;
        this.iHalftime1Length = iHalftime1Length;
        this.iHalftime2Length = iHalftime2Length;
        this.iHalftime3Length = iHalftime3Length;
        this.iHalftime4Length = iHalftime4Length;
        this.iTimeBetweenAdvertisingInMiliseconds = iTimeBetweenAdvertisingInMiliseconds;
        this.iTimeBetweenReplacementInMiliseconds = iTimeBetweenReplacementInMiliseconds;
        this.iTimeUntilGoToMainInMiliseconds = iTimeUntilGoToMainInMiliseconds;
        this.bSnowing = bSnowing;
        this.theme = theme;
        this.sGoalAnimation = sGoalAnimation;
    }

    /**
     * This method saves the current Settings.
     */
    public void save() {
        try {
            FileHandling.getFileHandling().saveTToFile(this, Defaults.S_SETTINGS_FILE_NAME);
        } catch (FileNotCreatedException e) {
            ErrorMsg.showErrorMessage(106, e.getMessage());
        } catch (IOException e) {
            ErrorMsg.showErrorMessage(106, e.getMessage());
        }
    }

    /**
     * This Method checks and updates the Values if something dosn't set correctly.
     */
    public void update() {
        boolean update = false;
        if (version == null || !version.equals(Main.VERSION)) {
            version = Main.VERSION;
            update = true;
        }
        if (mainFrame == null) {
            mainFrame = new Frame(Defaults.FRAME_ACTIVATED, Defaults.FRAME_WIDTH, Defaults.FRAME_HEIGHT,
                    Defaults.FRAME_START_X, Defaults.FRAME_START_Y, null);
            update = true;
        }
        if (sSystemLanguage == null) {
            sSystemLanguage = Languages.GERMAN;
            update = true;
        }
        if (sGoalAnimation == null) {
            sGoalAnimation = GoalAnimation.ANIMATION_2;
            update = true;
        }
        if (theme == null) {
            theme = Themes.DEFAULT;
            update = true;
        }
        if (animation == null) {
            animation = Animations.FADE;
            update = true;
        }
        if (iHalftime1Length == 0) {
            iHalftime1Length = Defaults.HALFTIME_1_LENGTH;
            update = true;
        }
        if (iHalftime2Length == 0) {
            iHalftime2Length = Defaults.HALFTIME_2_LENGTH;
            update = true;
        }
        if (iHalftime3Length == 0) {
            iHalftime3Length = Defaults.HALFTIME_3_LENGTH;
            update = true;
        }
        if (iHalftime4Length == 0) {
            iHalftime4Length = Defaults.HALFTIME_4_LENGTH;
            update = true;
        }
        if (iTimeBetweenAdvertisingInMiliseconds == 0) {
            iTimeBetweenAdvertisingInMiliseconds = Defaults.TIME_BETWEEN_ADVERTISING_IN_MILISECONDS;
            update = true;
        }
        if (iTimeBetweenReplacementInMiliseconds == 0) {
            iTimeBetweenReplacementInMiliseconds = Defaults.TIME_BETWEEN_REPLACEMENT_IN_MILISECONDS;
            update = true;
        }
        if (iTimeUntilGoToMainInMiliseconds == 0) {
            iTimeUntilGoToMainInMiliseconds = Defaults.TIME_UNTIL_GO_TO_MAIN_IN_MILISECONDS;
            update = true;
        }

        if (update)
            save();
    }
}
