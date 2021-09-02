package engine.nvgui;

import org.lwjgl.nanovg.NVGColor;

import java.awt.*;

/**
 * standardizes all nvgui to be included in arraylist of NVGPanel
 */
public abstract class AbstractNVGUI {

    protected Color[] colorsNormal = {new Color(255,255,255, 90),
                                new Color(128,128,128, 255),
                                new Color(80,80,80, 255)},
                    colorsClear = {new Color(255,255,255, 20),
                                    new Color(255,255,255, 40),
                                    new Color(255,255,255, 60)};

    public static Color backgroundStroke = new Color(0, 0, 255, 200),
                        backgroundFill = new Color(0, 0, 255, 80);


    public abstract void render(float scaleX, float scaleY);

    public abstract void offsetPos(int x, int y);

    //turn Color class into NVGColor
    public static NVGColor rgbaColor(Color c) {
        NVGColor color = NVGColor.create();
        color.r(c.getRed() / 255.0f);
        color.g(c.getGreen() / 255.0f);
        color.b(c.getBlue() / 255.0f);
        color.a(c.getAlpha() / 255.0f);

        return color;
    }
}
