package engine.nvgui.templates;

import engine.nvgui.*;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * Small button for menus in gui
 */
public class NVGButtonSmall extends AbstractNVGUI {

    private NVGButton button;

    private final int WIDTH = 120, HEIGHT = 40;

    //send vg, your text, your fontbuffer, and positions to the Object
    public NVGButtonSmall(long vg, String message, ByteBuffer fontBuffer, int x, int y) throws Exception {
        NVGText text = new NVGText(vg, 0, 0, message, fontBuffer, 20, Color.BLACK);
        setButton(new NVGButton(vg, new Rectangle(x, y, WIDTH, HEIGHT), text));
    }

    public NVGButtonSmall(long vg, AbstractNVGUI element, int x, int y) throws Exception {
        setButton(new NVGButton(vg, new Rectangle(x, y, WIDTH, HEIGHT), element));
    }

    public NVGButton getButton(){
        return button;
    }

    public void setButton(NVGButton newButton){
        button = newButton;
    }

    @Override
    public void render(float scaleX, float scaleY) {
        button.render(scaleX, scaleY);
    }

    @Override
    public void offsetPos(int x, int y) {
        button.offsetPos(x, y);
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}
