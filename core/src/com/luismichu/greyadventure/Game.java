package com.luismichu.greyadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
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



    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);


    }

    @Override
    public void resize(int width, int height) {

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
