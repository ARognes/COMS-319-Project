package engine.nvgui;

import java.awt.*;

public class NVGButton extends AbstractNVGUI {

    protected NVGRect background;
    protected AbstractNVGUI element;

    protected int state = 0;
    protected long vg;

    private Listener listener;
    private Color[] colors = colorsNormal;

    //empty button
    public NVGButton(long vg, Rectangle rect) {
        this.vg = vg;
        this.background = new NVGRect(vg, rect, 2, Color.BLACK, colors[0]);
    }

    //button with colors
    public NVGButton(long vg, Rectangle rect, Color[] colors) {
        this.vg = vg;
        this.colors = colors;
        this.background = new NVGRect(vg, rect, 2, Color.BLACK, colors[0]);
    }

    //button with element
    public NVGButton(long vg, Rectangle rect, AbstractNVGUI element) {
        this.vg = vg;
        this.element = element;
        element.offsetPos(rect.x, rect.y);
        this.background = new NVGRect(vg, rect, 2, Color.BLACK, colors[0]);
    }

    //button with element and colors
    public NVGButton(long vg, Rectangle rect, AbstractNVGUI element, Color[] colors) {
        this.vg = vg;
        this.colors = colors;
        this.element = element;
        element.offsetPos(rect.x, rect.y);
        this.background = new NVGRect(vg, rect, 2, Color.BLACK, colors[0]);
    }

    public void render(float scaleX, float scaleY) {
        background.render(scaleX, scaleY);
        if(element != null) element.render(scaleX, scaleY);
    }

    public void input(int cursorX, int cursorY, int clickState, int button) {

        //get the rectangle every time as it may change
        Rectangle rect = background.getRect();

        //if cursor is within button
        if(cursorX >= rect.x - rect.width/2 && cursorX <= rect.x + rect.width/2 && cursorY >= rect.y - rect.height/2 && cursorY <= rect.y + rect.height/2) {
            if(clickState == 1 && button == 0) {
                if(listener != null) listener.event(0); //clicked down on
                setState(2);
            }
            else if(state == 2) {
                if(listener != null) listener.event(1); //clicked and released while over
                setState(1);
            }
            else   setState(1); //hovering over
        } else  setState(0);
    }

    private void setState(int state) {
        this.state = state;
        background.setFillColor(colors[state]);
    }

    public void setColors(Color[] colors) {this.colors = colors;}

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void offsetPos(int x, int y) {
        background.offsetPos(x, y);
        if(element != null) element.offsetPos(x, y);
    }

    public AbstractNVGUI getElement() {
        return element;
    }
}