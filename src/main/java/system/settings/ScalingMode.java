/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system.settings;

import system.Defaults;

/**
 * How the panes of the main scoreboard use the window. FILL lays them out in the resolution of the
 * LED wall and stretches them to the window, CONTAIN lays them out in the window size without scaling.
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

    public int getLayoutWidth(Frame mainFrame) {
        return this == FILL ? Defaults.SCOREBOARD_DESIGN_WIDTH : mainFrame.iWidth;
    }

    public int getLayoutHeight(Frame mainFrame) {
        return this == FILL ? Defaults.SCOREBOARD_DESIGN_HEIGHT : mainFrame.iHeight;
    }
}
