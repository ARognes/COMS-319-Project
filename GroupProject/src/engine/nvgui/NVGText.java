package engine.nvgui;

import java.awt.*;
import java.nio.ByteBuffer;
import org.lwjgl.nanovg.NVGColor;
import static org.lwjgl.nanovg.NanoVG.*;

/**
 * basic text gui
 * stores a string with text settings
 */
public class NVGText extends AbstractNVGUI {

    private long vg;
    private String text;
    private int fontSize, x, y, align = NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE;
    private NVGColor color;

    //default constructor
    public NVGText(long vg, int x, int y, String text, ByteBuffer fontBuffer, int fontSize, Color color) throws Exception {
        this.vg = vg;
        this.x = x;
        this.y = y;
        this.text = text;
        this.fontSize = fontSize;
        this.color = rgbaColor(color);

        int font = nvgCreateFontMem(vg, "font", fontBuffer, 0);
        if (font == -1) throw new Exception("font not loaded");
    }

    //constructor adds align option
    public NVGText(long vg, int x, int y, int align, String text, ByteBuffer fontBuffer, int fontSize, Color color) throws Exception {
        this.vg = vg;
        this.x = x;
        this.y = y;
        this.text = text;
        this.fontSize = fontSize;
        this.color = rgbaColor(color);
        setAlign(align);
        int font = nvgCreateFontMem(vg, "font", fontBuffer, 0);
        if (font == -1) throw new Exception("font not loaded");
    }

    @Override
    public void render(float scaleX, float scaleY) {
        nvgFontSize(vg, fontSize);
        nvgFontFace(vg, "font");
        nvgFillColor(vg, color);
        nvgTextAlign(vg, align);
        nvgText(vg, x * scaleX, y * scaleY, text);
    }

    public void setAlign(int a) {
        if(a == 0) align = NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE;
        else if(a == 1) align = NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE;
        else align = NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE;
    }

    public void offsetPos(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void setColor(Color color) {
        this.color = rgbaColor(color);
    }

    public void setText(String text) {
        this.text = text;
    }

    public char backspace() {
        if(text.length() > 0) {
            char c = text.charAt(text.length()-1);
            text = text.substring(0, text.length() - 1);
            return c;
        }
        return (char)0;
    }

    public void append(char c) {
        text += c;
    }

    //getter
    public String getText() {
        return text;
    }

    public int getFontSize() {return fontSize;}
}