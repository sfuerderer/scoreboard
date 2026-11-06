/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system.settings;

import system.Defaults;

/**
 * How the content of the scoreboards uses the window. FILL lays it out in the resolution of the
 * LED wall (or the small displays) and stretches it to the window, CONTAIN lays it out in the window
 * size without scaling.
 */
public enum ScalingMode {
    CONTAIN("scaling_contain"), FILL("scaling_fill");

    private String sLanguageKey;

    ScalingMode(String sLanguageKey) {
        this.sLanguageKey = sLanguageKey;
    }

    public String getsLanguageKey() {
        return sLanguageKey;
    }

    /** Layout width of the main scoreboard. */
    public int getLayoutWidth(Frame mainFrame) {
        return getLayoutWidth(mainFrame, Defaults.SCOREBOARD_DESIGN_WIDTH);
    }

    /** Layout height of the main scoreboard. */
    public int getLayoutHeight(Frame mainFrame) {
        return getLayoutHeight(mainFrame, Defaults.SCOREBOARD_DESIGN_HEIGHT);
    }

    public int getLayoutWidth(Frame frame, int iDesignWidth) {
        return this == FILL ? iDesignWidth : frame.iWidth;
    }

    public int getLayoutHeight(Frame frame, int iDesignHeight) {
        return this == FILL ? iDesignHeight : frame.iHeight;
    }
}
