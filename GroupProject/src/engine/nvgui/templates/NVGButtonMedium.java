package engine.nvgui.templates;

import engine.nvgui.*;

import java.awt.*;
import java.nio.ByteBuffer;

public class NVGButtonMedium extends AbstractNVGUI {

    private final int WIDTH = 240, HEIGHT = 80;

    private NVGButton button;

    public NVGButtonMedium(long vg, AbstractNVGUI element, int x, int y) throws Exception {
        setButton(new NVGButton(vg, new Rectangle(x, y, WIDTH, HEIGHT), colorsClear));
    }

    public NVGButtonMedium(long vg, String message, ByteBuffer fontBuffer, int x, int y) throws Exception {
        NVGText text = new NVGText(vg, 0, 0, message, fontBuffer, 40, Color.WHITE);
        setButton(new NVGButton(vg, new Rectangle(x, y, WIDTH, HEIGHT), text, colorsClear));
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
