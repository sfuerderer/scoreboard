/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import gui.animation.Animations;
import system.settings.GoalAnimation;
import system.settings.ScalingMode;
import system.settings.Themes;

public class Defaults {
    public static final String S_SETTINGS_FILE_NAME = "configuration/settings.json";
    public static final String S_LANGUAGE_FILE_NAME = "configuration/language.json";
    public static final String S_LEAGUES_FILE_NAME = "leagues/leagues.json";

    public static final boolean FRAME_ACTIVATED = true;
    public static final int FRAME_WIDTH = 514;
    public static final int FRAME_HEIGHT = 280;
    public static final int FRAME_START_X = 0;
    public static final int FRAME_START_Y = 10;
    // Resolution of the LED wall. With ScalingMode.FILL all scoreboard panes are laid out for this size
    // and stretched to the configured window size (the wall's receiver scales the signal back).
    public static final int SCOREBOARD_DESIGN_WIDTH = 514;
    public static final int SCOREBOARD_DESIGN_HEIGHT = 280;

    public static final int HALFTIME_1_LENGTH = 45;
    public static final int HALFTIME_2_LENGTH = 45;
    public static final int HALFTIME_3_LENGTH = 15;
    public static final int HALFTIME_4_LENGTH = 15;
    public static final int TIME_BETWEEN_ADVERTISING_IN_MILISECONDS = 10000;
    public static final int TIME_BETWEEN_REPLACEMENT_IN_MILISECONDS = 5000;
    public static final int TIME_UNTIL_GO_TO_MAIN_IN_MILISECONDS = 5000;
    public static final boolean IS_SNOWING = false;
    public static final Themes THEME = Themes.DEFAULT;
    public static final GoalAnimation GOAL_ANIMATION = GoalAnimation.ANIMATION_2;
    public static final Animations ANIMATION = Animations.FADE;
    public static final String ANIMATION_WIPE_COLOR = "#0057b8";
    public static final ScalingMode SCALING_MODE = ScalingMode.FILL;

    public static final boolean OTHER_FRAME_1_ACTIVATED = true;
    public static final int OTHER_FRAME_1_WIDTH = 65;
    public static final int OTHER_FRAME_1_HEIGHT = 16;
    public static final int OTHER_FRAME_1_START_X = 0;
    public static final int OTHER_FRAME_1_START_Y = 301;

    public static final boolean OTHER_FRAME_2_ACTIVATED = true;
    public static final int OTHER_FRAME_2_WIDTH = 64;
    public static final int OTHER_FRAME_2_HEIGHT = 16;
    public static final int OTHER_FRAME_2_START_X = 129;
    public static final int OTHER_FRAME_2_START_Y = 301;

}
