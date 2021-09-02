package engine.nvgui;

import org.lwjgl.nanovg.NVGColor;

import java.awt.*;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * basic rectangle gui
 * stores a rectangle, stroke size, and stroke and fill colors
 */
public class NVGRect extends AbstractNVGUI {

    private final int ROUNDED_CORNERS = 4;

    private long vg;
    private int strokeWidth;

    private float scaleX = 1f, scaleY = 1f;
    private boolean scaling = false;
    private Rectangle rect;
    private NVGColor strokeColor, fillColor;
    private AbstractNVGUI element;

    public NVGRect(long vg, Rectangle rect, int strokeWidth, Color strokeColor, Color fillColor) {
        this.vg = vg;
        setRect(rect);
        this.strokeWidth = strokeWidth;
        this.strokeColor = rgbaColor(strokeColor);
        this.fillColor = rgbaColor(fillColor);
    }

    public NVGRect(long vg, Rectangle rect, int strokeWidth, Color strokeColor, Color fillColor, AbstractNVGUI element) {
        this.vg = vg;
        setRect(rect);
        this.strokeWidth = strokeWidth;
        this.strokeColor = rgbaColor(strokeColor);
        this.fillColor = rgbaColor(fillColor);
        this.element = element;
    }

    @Override
    public void render(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        nvgBeginPath(vg);
        if(!scaling) nvgRoundedRect(vg, rect.x * scaleX - rect.width/2, rect.y * scaleY - rect.height/2, rect.width, rect.height, ROUNDED_CORNERS);
        else nvgRoundedRect(vg, (rect.x - rect.width/2) * scaleX, (rect.y - rect.height/2) * scaleY, rect.width * scaleX, rect.height * scaleY, ROUNDED_CORNERS);
        nvgClosePath(vg);
        nvgFillColor(vg, fillColor);
        nvgStrokeColor(vg, strokeColor);
        nvgStrokeWidth(vg, strokeWidth);
        nvgStroke(vg);
        nvgFill(vg);
        if(element != null) element.render(scaleX, scaleY);
    }

    //rectangle needs to be drawn from the center, so the original is stored for scaling purposes
    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public void offsetPos(int x, int y) {
        setRect(new Rectangle(rect.x + x, rect.y + y, rect.width, rect.height));
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = rgbaColor(strokeColor);
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = rgbaColor(fillColor);
    }

    public void setScaling(boolean scaling) {this.scaling = scaling;}

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

