package engine.nvgui;

import org.lwjgl.nanovg.NVGColor;

import java.awt.*;

import static org.lwjgl.nanovg.NanoVG.*;

public class NVGLine extends AbstractNVGUI {

    private long vg;

    private float scaleX = 1f, scaleY = 1f;
    private Rectangle rect;
    private NVGColor fillColor, strokeColor;
    private int strokeWidth = 0;

    //full constructor of line with all options
    public NVGLine(long vg, Rectangle rect, int strokeWidth, Color strokeColor, Color fillColor) {
        this.vg = vg;
        setRect(rect);
        this.fillColor = rgbaColor(fillColor);
        this.strokeColor = rgbaColor(strokeColor);
        this.strokeWidth = strokeWidth;
    }

    //construct thin line
    public NVGLine(long vg, Rectangle rect, Color fillColor) {
        this.vg = vg;
        setRect(rect);
        this.fillColor = rgbaColor(fillColor);
        this.strokeColor = this.fillColor;
    }

    public void render(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        nvgBeginPath(vg);
        nvgMoveTo(vg, rect.x * scaleX - rect.width/2, rect.y * scaleY - rect.height/2);
        nvgLineTo(vg, rect.x * scaleX - rect.width/2 + rect.width, rect.y * scaleY - rect.height/2 + rect.height);
        nvgClosePath(vg);
        nvgStrokeColor(vg, strokeColor);
        nvgStrokeWidth(vg, strokeWidth);
        nvgFillColor(vg, fillColor);
        nvgFill(vg);
        nvgStroke(vg);
    }

    public void offsetPos(int x, int y) {
        setRect(new Rectangle(rect.x + x, rect.y + y, rect.width, rect.height));
    }

    //setter
    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public void setColor(Color color) {
        strokeColor = rgbaColor(color);
        fillColor = strokeColor;
    }
    //getter
    public Rectangle getRect() {
        return new Rectangle((int)(rect.x * scaleX), (int)(rect.y * scaleY), rect.width, rect.height);
    }

    public Rectangle getUnscaledRect() {
        return rect;
    }

    public Color getStrokeColor() {return new Color(strokeColor.r(), strokeColor.g(), strokeColor.b(), strokeColor.a());}

    public Color getFillColor() {return new Color(fillColor.r(), fillColor.g(), fillColor.b(), fillColor.a());}
}