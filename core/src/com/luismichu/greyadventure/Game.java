package com.luismichu.greyadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.luismichu.greyadventure.Levels.Level;
import com.luismichu.greyadventure.Levels.Level1;
import com.luismichu.greyadventure.Manager.*;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class Game implements Screen {
    private GreyAdventure greyAdventure;
    private MyAssetManager assetManager;
    private MyPreferenceManager preferenceManager;
    private MyPhysicManager physicManager;
    private OrthographicCamera camera, cameraUI;
    private FitViewport viewport, viewportUI;
    private GameState gameState;
    private Level level;

    public final static int TILE_SIZE = 32;
    public static float WORLD_SPEED = 1f;

    public Game(GreyAdventure greyAdventure, GameState gameState){
        this.greyAdventure = greyAdventure;
        this.gameState = gameState;
    }

    @Override
    public void show() {
        assetManager = new MyAssetManager();
        preferenceManager = new MyPreferenceManager();
        physicManager = MyPhysicManager.getInstance();

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

        if(gameState == null)
            level = Level1.create(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
        else {
            switch (gameState.level) {
                case 1:
                    level = Level1.create(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
                    break;

                case 2:
                    //level = Level2.create(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
                    break;

                case 3:
                    //level = Level3.create(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
                    break;
            }
        }
        Gdx.input.setInputProcessor(level);
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        level.update();
        level.draw();

        if(level.hasFinished()) {
            greyAdventure.setScreen(new MainMenu(greyAdventure, false));
            dispose();
        }
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
