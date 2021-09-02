package engine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;

/**
 * This is not much
 */
public class GameLogic {

    private int direction = 0;

    private float red = 0.0f;
    private float blue = 0.0f;

    public void input(Renderer renderer) {

        if (renderer.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (renderer.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = 2;
        } else if (renderer.isKeyPressed(GLFW_KEY_RIGHT)) {
            direction = 3;
        } else if (renderer.isKeyPressed(GLFW_KEY_LEFT)) {
            direction = 4;
        } else {
            direction = 0;
        }
    }

    public void update(float interval) {
        if(direction == 1) red += interval;
        else if(direction == 2) red -= interval;
        else if(direction == 3) blue += interval;
        else if(direction == 4) blue -= interval;
        if(red > 1f) red = 1f;
        else if(red < 0f) red = 0f;
        if(blue > 1f) blue = 1f;
        else if(blue < 0f) blue = 0f;
    }

    public float getRed() {
        return red;
    }
    public float getBlue() {
        return blue;
    }
}