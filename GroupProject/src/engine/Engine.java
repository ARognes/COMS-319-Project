package engine;

/**
 *
 * The engine is a runnable thread
 *
 * I am reading a book on java game engines, this is where I got a lot of the information on how vsync should work
 * https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/chapter02/chapter2.html
 *
 */
public class Engine implements Runnable {

    private final int FPS = 60;    //ideal frames per second
    private final int UPS = 60;    //ideal updates per second

    public Renderer renderer;
    private GameLogic gameLogic;

    private double lastLoopTime, deltaTime = 0f, startTime = 0f;

    //initialize window, then gameLogic, which initializes Renderer, then begin gameloop
    @Override
    public void run() {
        try {
            gameLogic = new GameLogic();
            renderer = new Renderer(this, gameLogic);

            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            renderer.cleanup();
        }
    }

    /**
     * input()
     * update() [0 - multiple times]
     * render()
     * sync()
     */
    private void gameLoop() {
        lastLoopTime = System.nanoTime() / 1000_000_000.0;
        startTime = lastLoopTime;
        float accumulator = 0f;     //makes sure update frames don't fall behind or speed past renderer
        float interval = 1f / UPS;

        //begin updates
        boolean running = true;
        while (running && !renderer.windowShouldClose()) {

            double timeNow = System.nanoTime() / 1000_000_000.0;
            deltaTime = (timeNow - lastLoopTime);
            lastLoopTime = timeNow;
            accumulator += deltaTime;

            //if splash screen logo hasn't been displayed for a second don't do anything
            if(timeNow - startTime < 1f) continue;

            input();

            //multiple or no updates() may happen per render frame. Higher UPS means more updates per frame.
            //updates can happen at less than fps because it is behind the scenes game logic
            //do not interact with any controls with this or input will be set by the UPS
            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            //render();
            renderer.render();


            //pause to sync loopTime
            if (!renderer.isvSync()) sync();
        }
    }

    private void input() {
        renderer.input();
        gameLogic.input(renderer);
    }

    private void update(float interval) {
        renderer.update(interval);
        gameLogic.update(interval);
    }

    //if not using vsync, this function is called to manually set framerate
    private void sync() {
        float timePerFrame = 1f / FPS;
        double endTime = lastLoopTime + timePerFrame;

        //sleep until endTime has been reached with a while loop to constantly check time
        while (System.nanoTime() / 1000_000_000.0 < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {}
        }
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public double getTime() {
        return (System.nanoTime()/1000_000_000.0 - startTime);
    }
}