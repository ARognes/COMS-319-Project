package engine;

import engine.rendering.Mesh;
import engine.rendering.OBJLoader;
import engine.rendering.ShaderProgram;
import engine.rendering.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * last line of rendering is done here for 3d meshes.
 */
public class Renderer extends AbstractGUI {

    private GameLogic gameLogic;

    private ShaderProgram shaderProgram;
    private ArrayList<Mesh> meshes = new ArrayList<>();

    private final float FOV = (float)Math.toRadians(60.0f);
    private final float Z_NEAR = 0.01f, Z_FAR = 1000.f;
    private Matrix4f projectionMatrix, worldMatrix;

    private int texture = glGenTextures();

    public Renderer(Engine engine, GameLogic gameLogic) throws Exception {
        super(engine);
        this.gameLogic = gameLogic;

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("color");
        shaderProgram.createUniform("useColor");

        Mesh mesh = OBJLoader.loadMesh("/cube.obj");
        mesh.setTexture(new Texture("textures/texture.png"));
        mesh.setPosition(new Vector3f(0, -4, -10));
        mesh.setScale(3);
        meshes.add(mesh);
    }

    public void removeBug() {
        if(meshes.size() > 1) meshes.remove(1);
    }

    public void renderNew(String seed) throws Exception {
        removeBug();

        if(seed.equals("0") || seed.equals("bee")){
            Mesh mesh = OBJLoader.loadMesh("/beebro.obj");
            mesh.setPosition(new Vector3f(0.8f, -2, -10));
            mesh.setColor(new Vector3f(1, 1, 0));
            meshes.add(mesh);
            //mesh.setTexture(new Texture("textures/texture.png"));
        }
        else if(seed.equals("1") || seed.equals("ladybug")){
            Mesh mesh = OBJLoader.loadMesh("/ladybugbro.obj");
            mesh.setPosition(new Vector3f(0, 0, -10));
            mesh.setColor(new Vector3f(1, 0, 0));
            meshes.add(mesh);

            //mesh.setTexture(new Texture("textures/texture.png"));
        }
        else{
            Mesh mesh = OBJLoader.loadMesh("/spiderbro.obj");
            mesh.setPosition(new Vector3f(0, 0, -10));
            mesh.setColor(new Vector3f(0.2f, 0, 0));
            meshes.add(mesh);

            //mesh.setTexture(new Texture("textures/texture.png"));
        }


    }

    @Override
    public void render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        shaderProgram.bind();

        projectionMatrix = new Matrix4f().perspective(FOV, getWidth() / (float)getHeight(), Z_NEAR, Z_FAR).rotateX((cursorY - getHeight()/2f)/getHeight() * 0.2f).rotateY((cursorX - getWidth()/2f)/getWidth() * 0.2f);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        for(Mesh mesh : meshes) {
            mesh.setRotation(new Vector3f(0.4f, sceneRotationY, 0));
            Vector3f rot = mesh.getRotation();
            worldMatrix = new Matrix4f().identity().translate(mesh.getPosition()).rotateX(rot.x).rotateY(rot.y).rotateZ(rot.z).scale(mesh.getScale());

            shaderProgram.setUniform("worldMatrix", worldMatrix);
            shaderProgram.setUniform("color", mesh.getColor());
            shaderProgram.setUniform("useColor", mesh.isTextured() ? 0 : 1);

            mesh.render();
        }

        shaderProgram.unbind();

        //gameLogic, GUI gets variables and information from GameLogic, not the other way around
        glClearColor(0.53f, 0.81f, 1, 1);
        //gameLogic

        super.render();
    }

    public void cleanup() {
        shaderProgram.cleanup();
        for(Mesh mesh : meshes) mesh.cleanUp();
    }
}