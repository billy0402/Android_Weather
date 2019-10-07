package edu.ntub.weather.set;

import android.graphics.Color;

public enum ColorSet {
    BLACK(Color.BLACK),
    BLUE(Color.BLUE),
    CYAN(Color.CYAN),
    DKGRAY(Color.DKGRAY),
    GRAY(Color.GRAY),
    GREEN(Color.GREEN),
    LTGRAY(Color.LTGRAY),
    MAGENTA(Color.MAGENTA),
    RED(Color.RED),
    CLEAR(Color.TRANSPARENT),
    WHITE(Color.WHITE),
    YELLOW(Color.YELLOW);

    private int color;

    ColorSet(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    private void setColor(int color) {
        this.color = color;
    }
}
