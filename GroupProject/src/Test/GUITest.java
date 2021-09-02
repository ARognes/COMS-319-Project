package Test;

import engine.*;
import org.junit.Before;
import org.junit.Test;

public class GUITest {


    Engine engine = new Engine();
    GameLogic gameLogic = new GameLogic();
    AbstractGUI gui;

    //setup all values needed to create buttons for testing
    @Before
    public void setUp() throws Exception {
        //ByteBuffer fontBufferRoboto;
        float vg = (float) 1.00300302;


        gui = new Renderer(engine, gameLogic);

    }

    @Test
    public void input() {
    }

    @Test
    public void render() {
    }
}