package com.luismichu.greyadventure.Levels;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.luismichu.greyadventure.Manager.Dialog;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.MyPreferenceManager;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;

public abstract class Level implements InputProcessor {
    protected TiledMap map;
    protected TiledMapRenderer mapRenderer;
    protected Dialog dialog;
    protected MyAssetManager assetManager;
    protected MyPreferenceManager preferenceManager;
    protected MyPhysicManager physicManager;
    protected OrthographicCamera camera;
    protected OrthographicCamera cameraUI;

    protected Level(MyAssetManager assetManager, MyPreferenceManager preferenceManager, MyPhysicManager physicManager,
                    OrthographicCamera camera, OrthographicCamera cameraUI){
        this.assetManager = assetManager;
        this.preferenceManager = preferenceManager;
        this.physicManager = physicManager;
        this.camera = camera;
        this.cameraUI = cameraUI;
    }

    public abstract void update();
    public abstract void draw();

    public void pause(){

    }
}
