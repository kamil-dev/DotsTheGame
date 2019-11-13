package main.java.model;

import java.awt.*;
import java.io.Serializable;

public class Theme implements Serializable {
    private Color backgroundColor;
    private Color foregroundColor;
    private Font fontLarge;
    private Font fontNormal;

    public Theme() {
        this(new Color(204, 204, 255), new Color(0, 51, 153), new Font("Arial",Font.BOLD,20),
                new Font("Arial",Font.PLAIN,16));
    }

    public Theme(Color backgroundColor, Color foregroundColor, Font fontLarge, Font fontNormal) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.fontLarge = fontLarge;
        this.fontNormal = fontNormal;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Font getFontLarge() {
        return fontLarge;
    }

    public Font getFontNormal() {
        return fontNormal;
    }
}
