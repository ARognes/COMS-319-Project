package engine;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Platform;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Handles window settings and initializes the lwjgl and openGl library
 */
public abstract class AbstractWindow {

    private final String title = "Bug Bonanza";

    private int width = 960, height = 640;
    protected final float ORIGINAL_WIDTH = width, ORIGINAL_HEIGHT = height;

    private static int lastPressed = -1;
    private final float KEYPRESS_PAUSE = 0.5f;
    private float keypressPause = 0f;

    private static long windowHandle;

    private boolean vSync = true;

    //ints used by extended class
    protected int cursorX, cursorY, button, clickState;

    /**
     * basic window creation and input callbacks
     */
    public AbstractWindow() {
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        if (Platform.get() == Platform.MACOSX) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        }
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

        //create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }
        glfwSetWindowSizeLimits(windowHandle, 960, 640, 4000, 4000);

        //setup resize callback, render even when being resized
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            glViewport(0, 0, width, height);

            //this will be passed down to Renderer.render(), and then back up
            render();
        });

        //setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); //we will detect this in the rendering loop
            }

            if(action == 1) {
                lastPressed = key;
                keypressPause = KEYPRESS_PAUSE;
            } else if(action == 2 && keypressPause <= 0f) lastPressed = key;
        });

        //setup a mouse position callback
        glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
            cursorX = (int)xpos;
            cursorY = (int)ypos;
        });

        glfwSetMouseButtonCallback(windowHandle, (window, button, held, idkLookituplater) -> {
            this.button = button;
            this.clickState = held;
        });

        //get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        //center our window
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        //make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if(isvSync()) glfwSwapInterval(1);

        //make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        //set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    /**
     * top-level render() called last
     */
    public void render() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);

        //glEnable(GL_CULL_FACE);
        //glCullFace(GL_BACK);
    }

    public void update(float interval) {
        if(keypressPause > 0f) keypressPause -= interval;
    }

    /**
     * getters
     */
    public static boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public static int lastKeyPressed() {
        int last = lastPressed;
        lastPressed = -1;
        return last;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }
}