package com.luismichu.greyadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.luismichu.greyadventure.Levels.*;
import com.luismichu.greyadventure.Manager.GameState;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.MyPreferenceManager;
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
    private Stage stage;
    private Skin skin;

    public static float WORLD_SPEED = 1f;

    public Game(GreyAdventure greyAdventure, GameState gameState){
        this.greyAdventure = greyAdventure;
        this.gameState = gameState;
    }

    @Override
    public void show() {
        assetManager = new MyAssetManager();
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.skin);
        assetManager.loadQueue();
        skin = assetManager.getSkin(MyAssetManager.AssetDescriptors.skin);
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

        stage = new Stage(viewportUI);

        if(gameState == null)
            level = Level0.create(assetManager, preferenceManager, physicManager, camera, cameraUI, null);
        else {
            createLevel(gameState.level);
        }
        Gdx.input.setInputProcessor(level);
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        if(level == null) {
            stage.act();
            stage.draw();
        }else{
            level.update();
            level.draw();

            if (level.hasFinished()) {
                greyAdventure.setScreen(new MainMenu(greyAdventure, false));
                dispose();
            } else if (level.next()) {
                nextLevel(!level.end());
            }
        }
    }

    private void createLevel(int levelNum){
        switch (levelNum) {
            case 0:
                level = Level0.create(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
                break;

            case 1:
                level = Level1.create(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
                break;

            case 2:
                level = Level2.create(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
                break;

            case 3:
                level = Level3.create(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
                break;
        }
    }

    private void nextLevel(boolean next){
        stage.clear();
        gameState = level.getGameState();
        level = null;
        physicManager = MyPhysicManager.getInstance();
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        Label lbl = new Label("Fin del nivel", skin);
        TextButton btNext = new TextButton("Siguiente", skin);
        TextButton btMainMenu = new TextButton("Menu principal", skin);

        lbl.setFontScale(1.8f);
        lbl.setWidth(cameraUI.viewportWidth * 0.5f);
        lbl.setAlignment(Align.center);

        btNext.setWidth(cameraUI.viewportWidth * 0.3f);
        btMainMenu.setWidth(cameraUI.viewportWidth * 0.3f);

        lbl.setPosition(cameraUI.viewportWidth * 0.25f, cameraUI.viewportHeight * 0.7f);
        btNext.setPosition(cameraUI.viewportWidth * 0.35f, cameraUI.viewportHeight * 0.38f);
        btMainMenu.setPosition(cameraUI.viewportWidth * 0.35f, cameraUI.viewportHeight * 0.25f);

        stage.addActor(lbl);
        if(next)
            stage.addActor(btNext);
        stage.addActor(btMainMenu);

        btNext.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                createLevel(gameState.level);
                Gdx.input.setCursorCatched(true);
                Gdx.input.setInputProcessor(level);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        btMainMenu.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                greyAdventure.setScreen(new MainMenu(greyAdventure, false));

                return super.touchDown(event, x, y, pointer, button);
            }
        });
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
        stage.dispose();
    }
}