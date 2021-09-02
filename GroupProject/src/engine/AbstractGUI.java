package engine;

import engine.nvgui.*;
import engine.nvgui.templates.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

import static engine.nvgui.AbstractNVGUI.backgroundFill;
import static engine.nvgui.AbstractNVGUI.backgroundStroke;
import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * all gui logic done mid-level
 */
public abstract class AbstractGUI extends AbstractWindow {

    protected Engine engine;    //engine used to find time changes, can also be used by Renderer
    protected float sceneRotationY = 0;
    private final float MAX_ROTATION_ACCELERATION = 5f, MIN_ROTATION_ACCELERATION = 0.5f;
    private float rotationAcceleration = MIN_ROTATION_ACCELERATION;

    private ByteBuffer fontBufferRoboto;  //all fontBuffers must avoid garbage collection(which is why it's stored class level) else core dump when called

    private long vg;

    private NVGPanel mainMenu, creatorMenu, creditsMenu;
    private int menuStage = 0;

    /**
     * initialize nanoVG window for gui and menu ui
     */
    public AbstractGUI(Engine engine) throws Exception {
        super();
        this.engine = engine;
        if ((GL11.glGetInteger(GL30.GL_MAJOR_VERSION) > 3) || (GL11.glGetInteger(GL30.GL_MAJOR_VERSION) == 3 && GL11.glGetInteger(GL30.GL_MINOR_VERSION) >= 2))
            vg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_STENCIL_STROKES | NanoVGGL3.NVG_ANTIALIAS);
        else vg = NanoVGGL2.nvgCreate(NanoVGGL2.NVG_STENCIL_STROKES | NanoVGGL2.NVG_ANTIALIAS);

        fontBufferRoboto = Utils.ioResourceToByteBuffer("resources/Roboto.ttf", 40 * 1024);

        /**
         * create menus
         */
        mainMenu = new NVGPanel(vg);
        creatorMenu = new NVGPanel(vg);
        creditsMenu = new NVGPanel(vg);

        /**
         * Buttons take take vg, message, fontBufferRoboto, x-position, y-position as arguments
         * to get new button take NVGButtonTemplate.getButton();
         */

        /**
         * Add buttons to main menu
         */
        NVGButton startGame = new NVGButtonMedium(vg,"Start Game",fontBufferRoboto,getWidth()/2,getHeight()/3).getButton();
        NVGButton options = new NVGButtonSmall(vg,"Credits",fontBufferRoboto,getWidth()/2,getHeight()*2/3).getButton();
        mainMenu.add(startGame);
        mainMenu.add(options);

        /**
         * Add buttons to credits menu
         */
        NVGRect background = new NVGRect(vg, new Rectangle(getWidth()/2, getHeight()/2, 600, (int)(getHeight() * 0.8f)), 2, backgroundStroke, backgroundFill);
        background.setScaling(true);
        NVGButton returnToMainMenu = new NVGButtonMedium(vg,"Back", fontBufferRoboto,getWidth()/2,getHeight()/5 * 4).getButton();
        creditsMenu.add(background);
        creditsMenu.add(returnToMainMenu);
        creditsMenu.add(new NVGText(vg,getWidth()/2, getHeight()/5, "~Credits~", fontBufferRoboto, 80, Color.WHITE));
        creditsMenu.add(new NVGText(vg,getWidth()/2, getHeight()/5 * 2, "Created by Austin Rognes And Matt Koeser", fontBufferRoboto, 30, Color.WHITE));
        creditsMenu.add(new NVGText(vg,getWidth()/2, getHeight()/2, "using Java 13, LWJGL, JOML, and NanoVG", fontBufferRoboto, 30, Color.WHITE));

        /**
         * Add buttons to creator
         */
        NVGButton rotateLeft = new NVGButtonSquare(vg,"<", fontBufferRoboto,getWidth()/16,getHeight()/2).getButton();
        creatorMenu.add(rotateLeft);
        NVGButton rotateRight = new NVGButtonSquare(vg,">", fontBufferRoboto,getWidth()/16 * 15,getHeight()/2).getButton();
        creatorMenu.add(rotateRight);
        NVGButton creatorToMainMenu = new NVGButtonSmall(vg,"Main Menu",fontBufferRoboto,getWidth()/14,getHeight()/16 * 15).getButton();
        creatorMenu.add(creatorToMainMenu);
        NVGButton createNew = new NVGButtonMedium(vg,"Random",fontBufferRoboto,getWidth()/2,getHeight()/8 * 7).getButton();
        creatorMenu.add(createNew);

        //TextBox for entering seed
        NVGTextBox seedTextBox = new NVGTextBox(vg, new Rectangle(getWidth()/2, getHeight()/8, 400, 80), fontBufferRoboto, "Enter Seed Value");
        creatorMenu.add(seedTextBox);

        /**
         * Create Listeners
         */
        seedTextBox.setListener((i)->
        {
            try {
                engine.renderer.renderNew(seedTextBox.getNVGText().getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        );
        createNew.setListener((i)->
        {
            if(i==1) {
                String[] arr = {"0", "1", "2"};
                Random rand = new Random();

                int j = rand.nextInt(3);
                try {
                    engine.renderer.renderNew(arr[j]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        rotateLeft.setListener((int i)->{
            sceneRotationY += engine.getDeltaTime() * rotationAcceleration;
            rotationAcceleration += engine.getDeltaTime() * 2f;
            if(rotationAcceleration > MAX_ROTATION_ACCELERATION) rotationAcceleration = MAX_ROTATION_ACCELERATION;
            if(i==1) rotationAcceleration = MIN_ROTATION_ACCELERATION;
        });
        rotateRight.setListener((int i)->{
            sceneRotationY -= engine.getDeltaTime() * rotationAcceleration;
            rotationAcceleration += engine.getDeltaTime() * 2f;
            if(rotationAcceleration > MAX_ROTATION_ACCELERATION) rotationAcceleration = MAX_ROTATION_ACCELERATION;
            if(i==1) rotationAcceleration = MIN_ROTATION_ACCELERATION;
        });
        startGame.setListener((int i)-> {
            if(i==1) renderCreator();
        });
        options.setListener((int i)-> {
            if(i==1) renderOptions();
        });
        creatorToMainMenu.setListener((int i)-> {
            if(i==1) {
                engine.renderer.removeBug();
                renderMainMenu();
            }
        });
        returnToMainMenu.setListener((int i)-> {
            if(i==1) renderMainMenu();
        });

        //splash screen logo
        int w = getWidth(), h = getHeight();
        float scaleX = w/ORIGINAL_WIDTH, scaleY = h/ORIGINAL_HEIGHT;
        nvgBeginFrame(vg, w, h, 1);
            try {
                new NVGRect(vg, new Rectangle(w/2,h/2,800,200), 10, new Color(0,0,100), new Color(0,0,100)).render(scaleX, scaleY);
                new NVGText(vg, w/2, h/2, 1, "~A Group 44 Production~", fontBufferRoboto, 80, Color.WHITE).render(scaleX, scaleY);
            } catch(Exception e) {}
        nvgEndFrame(vg);
        super.render();
    }

    private void renderMainMenu(){
        menuStage=0;
        render();
    }

    private void renderCreator(){
        menuStage=2;
        render();
    }

    private void renderOptions(){
        menuStage=1;
        render();
    }

    /**
     * menu gui input
     */
    public void input() {
        if(menuStage == 0) mainMenu.input(cursorX, cursorY, clickState, button);
        else if(menuStage==2) creatorMenu.input(cursorX,cursorY,clickState,button);
        else if(menuStage==1) creditsMenu.input(cursorX,cursorY,clickState,button);
    }

    /**
     * render gui output mid-level
     */
    public void render() {
        int w = getWidth(), h = getHeight();
        float scaleX = w/ORIGINAL_WIDTH, scaleY = h/ORIGINAL_HEIGHT;
        nvgBeginFrame(vg, w, h, 1);

        if(menuStage == 0) mainMenu.render(scaleX, scaleY);
        else if(menuStage == 1) creditsMenu.render(scaleX, scaleY);
        else if(menuStage == 2) creatorMenu.render(scaleX, scaleY);

        try {
            new NVGText(vg, 5, 18, 0, "T-" + Math.round(engine.getTime() * 100) / 100f, fontBufferRoboto, 20, Color.YELLOW).render(scaleX, scaleY);
        } catch(Exception e) {}

        nvgEndFrame(vg);

        super.render();
    }
}
