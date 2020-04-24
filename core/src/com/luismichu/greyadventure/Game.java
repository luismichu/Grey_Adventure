package com.luismichu.greyadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.luismichu.greyadventure.Levels.Level;
import com.luismichu.greyadventure.Levels.Level1;
import com.luismichu.greyadventure.Manager.Dialog;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.MyPreferenceManager;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class Game implements Screen {
    private GreyAdventure greyAdventure;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private MyAssetManager assetManager;
    private MyPreferenceManager preferenceManager;
    private MyPhysicManager physicManager;
    private OrthographicCamera camera, cameraUI;
    private FitViewport viewport, viewportUI;
    private Level level;

    public final static int TILE_SIZE = 32;
    public static float WORLD_SPEED = 1f;

    public Game(GreyAdventure greyAdventure){
        this.greyAdventure = greyAdventure;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        assetManager = new MyAssetManager();
        preferenceManager = new MyPreferenceManager();
        physicManager = MyPhysicManager.getInstance();

        assetManager.loadGame();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        int totalMapWidth = 16;
        int totalMapHeight = 9;
        viewport = new FitViewport(totalMapWidth, totalMapHeight, camera);
        camera.position.set(totalMapWidth / 2f,8,0);
        camera.zoom = 1f;

        cameraUI = new OrthographicCamera();
        cameraUI.setToOrtho(false);
        int[] resolution = preferenceManager.getResolution();
        viewportUI = new FitViewport(resolution[0], resolution[1], cameraUI);
        cameraUI.position.set(resolution[0] / 2f,resolution[1] / 2f,0);
        cameraUI.zoom = 1f;

        level = Level1.create(assetManager, preferenceManager, physicManager, camera, cameraUI);
        Gdx.input.setInputProcessor(level);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        level.update();
        level.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewportUI.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
