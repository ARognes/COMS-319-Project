package engine.nvgui;

import java.util.ArrayList;

/**
 * a panel holds nvg nvgui elements in a group for organization, use this as a menu system
 * in the future panels will change a lot
 */

public class NVGPanel extends AbstractNVGUI {

    private long vg;

    private int x, y;

    private ArrayList<AbstractNVGUI> elements = new ArrayList<AbstractNVGUI>();

    public NVGPanel(long vg) {
        this.vg = vg;
        x = 0;
        y = 0;
    }

    //if panel is being used as a sub-menu
    public NVGPanel(long vg, int x, int y) {
        this.vg = vg;
        this.x = x;
        this.x = y;
    }

    public void add(AbstractNVGUI element) {
        element.offsetPos(x, y);
        elements.add(element);
    }

    public void remove(AbstractNVGUI element) {
        elements.remove(element);
    }

    public void input(int cursorX, int cursorY, int clickState, int button) {
        for(int i=0; i<elements.size(); i++) {
            if(elements.get(i) instanceof NVGButton) ((NVGButton)elements.get(i)).input(cursorX - x, cursorY - y, clickState, button);
        }
    }

    //render all nvgui elements held
    public void render(float scaleX, float scaleY) {
        for(int i=0; i<elements.size(); i++) elements.get(i).render(scaleX, scaleY);
    }

    public void offsetPos(int x, int y) {
        this.x += x;
        this.y += y;

        for(int i=0; i<elements.size(); i++) elements.get(i).offsetPos(x, y);
    }
}