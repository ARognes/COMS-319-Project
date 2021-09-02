package engine.nvgui.templates;

import engine.nvgui.*;

import java.awt.*;
import java.nio.ByteBuffer;

public class NVGButtonSquare extends AbstractNVGUI {

    private final int WIDTH = 120, HEIGHT = 40;

    private NVGButton button;

    public NVGButtonSquare(long vg, String message, ByteBuffer fontBuffer, int x, int y) throws Exception {

        //create 3 rectangles
        Rectangle rect = new Rectangle(x, y, 45, 45);

        NVGText text = new NVGText(vg, 0, 0, message, fontBuffer, 40, Color.WHITE);

        setButton(new NVGButton(vg, new Rectangle(x, y, 45, 45), text));
    }

    public NVGButtonSquare(long vg, int x, int y) throws Exception {
        setButton(new NVGButton(vg, new Rectangle(x, y, 45, 45)));
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
