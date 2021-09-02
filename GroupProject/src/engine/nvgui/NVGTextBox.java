package engine.nvgui;

import java.awt.*;
import java.nio.ByteBuffer;

import static engine.AbstractWindow.isKeyPressed;
import static org.lwjgl.glfw.GLFW.*;

import static engine.AbstractWindow.lastKeyPressed;


/**
 * input text box
 * only stores other GUI elements and handles their communication to the window for keyboard input and string submission
 */
public class NVGTextBox extends NVGButton {

    private NVGText text;
    private NVGLine blinker;
    private boolean ignoreFirstKey = true;
    private String defaultText = "";

    private Listener listener;

    //char widths used for calculating position of blinker
    private final int CHAR_WIDTHS[] = {7,7,7,7,7,7,7,7,7,7,  7,8,9,9,8,7,9,9,3,6,8,7,9,9,9,8,9,9,8,7,9,7,11,7,7,7,  7,7,6,7,7,3,7,7,3,3,6,3,11,7,7,7,7,4,7,3,7,5,9,5,5,5, 3};
    private int textLengthPixels = 0;

    //button with element
    public NVGTextBox(long vg, Rectangle rect, ByteBuffer fontBuffer, String defaultText) throws Exception {
        super(vg, rect);
        this.defaultText = defaultText;
        text = new NVGText(vg, rect.x, rect.y, defaultText, fontBuffer, 20, Color.GRAY);
        element = text;
        blinker = new NVGLine(vg, new Rectangle(rect.x, rect.y, 0, 16), Color.BLACK);
    }

    @Override
    public void input(int cursorX, int cursorY, int clickState, int button) {

        Rectangle rect = background.getRect();

        //if cursor is within button
        if(cursorX >= rect.x - rect.width/2 && cursorX <= rect.x + rect.width/2 && cursorY >= rect.y - rect.height/2 && cursorY <= rect.y + rect.height/2) {
            if(clickState == 1 && button == 0) state = 2;
            else if(state == 2) {
                state = 1;
                if(text.getText().equals(defaultText)) {
                    text.setText("");
                    text.setColor(Color.BLACK);
                }
            }
        } else if(clickState != 0) {
            state = 0;
            ignoreFirstKey = true;
            if (text.getText().length() == 0) {
                resetTextBox();
            }
        }

        //typing
        if(state == 1) {
            int key = lastKeyPressed();
            if(ignoreFirstKey) key = -1;
            ignoreFirstKey = false;

            //special cases
            if(key == GLFW_KEY_BACKSPACE) {
                if(text.getText().length() > 0)  textLengthPixels -= getCharWidth(text.backspace());
            }
            else if(key == GLFW_KEY_SPACE) {
                text.append(' ');
                textLengthPixels += 3;
            }
            else if(key == GLFW_KEY_ENTER) {
                listener.event(1);
                state = 0;
                textLengthPixels = 0;
                resetTextBox();
            }

            //Only letters and numbers!
            if((key >= 48 && key <= 57) || (key >= 65 && key <= 90)) {
                char c;
                if (isKeyPressed(GLFW_KEY_LEFT_SHIFT) || isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) c = (char)key;
                else c = Character.toLowerCase((char)key);
                text.append(c);
                textLengthPixels += getCharWidth(c);
            }

            Rectangle blinkerRect = blinker.getUnscaledRect();
            blinkerRect.setLocation(background.getUnscaledRect().x + (int)(textLengthPixels * 0.64f), blinkerRect.y);
            blinker.setRect(blinkerRect);
        }
    }

    @Override
    public void render(float scaleX, float scaleY) {
        super.render(scaleX, scaleY);
        if(state > 0) {
            if (((int) (System.nanoTime() / 500_000_000.0)) % 2 == 0) blinker.render(scaleX, scaleY);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public NVGText getNVGText() {
        return (NVGText)getElement();
    }

    public String getText() {
        return ((NVGText)getElement()).getText();
    }

    private void resetTextBox() {
        text.setText(defaultText);
        text.setColor(Color.GRAY);
        ignoreFirstKey = true;
        textLengthPixels = 0;
    }

    private int getCharWidth(int c) {
        if(c >= 48 && c <= 57) {
            return CHAR_WIDTHS[c - 48];
        }
        else if(c >= 65 && c <= 90) {
            return CHAR_WIDTHS[c - 55];
        }
        else if(c >= 97 && c <= 122) {
            return CHAR_WIDTHS[c - 61];
        }
        else if(c == 32) return 3;

        return 0;
    }
}
