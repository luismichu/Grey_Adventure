package com.luismichu.greyadventure.Levels;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.luismichu.greyadventure.Game;
import com.luismichu.greyadventure.Grey;
import com.luismichu.greyadventure.Manager.Dialog;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.MyPreferenceManager;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;
import com.luismichu.greyadventure.Manager.Physic.Physic;

public class Level1 extends Level {
    private static Level1 level1;
    private boolean dialoging;
    private Grey grey;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;

    public static Level1 create(MyAssetManager assetManager, MyPreferenceManager preferenceManager, MyPhysicManager physicManager,
                         OrthographicCamera camera, OrthographicCamera cameraUI){
        level1 = new Level1(assetManager, preferenceManager, physicManager, camera, cameraUI);
        return getInstance();
    }

    public static Level1 getInstance(){
        return level1;
    }

    private Level1(MyAssetManager assetManager, MyPreferenceManager preferenceManager, MyPhysicManager physicManager,
                   OrthographicCamera camera, OrthographicCamera cameraUI){
        super(assetManager, preferenceManager, physicManager, camera, cameraUI);

        assetManager.addToQueue(MyAssetManager.AssetDescriptors.greyRunningL);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.greyRunningR);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.greyStanding);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.map1);

        assetManager.loadQueue();

        map = (TiledMap) assetManager.get(MyAssetManager.AssetDescriptors.map1);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(camera);

        dialog = new Dialog(MyAssetManager.AssetDescriptors.dialogMap1, cameraUI);
        dialoging = false;
        camera.translate(0, 4 * Game.TILE_SIZE);

        Vector2 pos = new Vector2(Game.TILE_SIZE * 2, Game.TILE_SIZE * 7f);
        grey = new Grey(pos, assetManager, camera);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        createCollisionObjects();

        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
    }

    private void createCollisionObjects(){
        MapObjects objects = map.getLayers().get(Physic.OBJECT_LAYER).getObjects();
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle r = rectangleObject.getRectangle();
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(r.x + r.width / 2, r.y + r.height / 2);

            Body body = physicManager.getWorld().createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(r.width / 2, r.height / 2);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;

            body.createFixture(fixtureDef).setUserData(2);
            shape.dispose();
        }
    }

    @Override
    public void update() {
        camera.update();
        cameraUI.update();
        mapRenderer.setView(camera);
        if(dialoging)
            dialog.update();
        else
            physicManager.update(Game.WORLD_SPEED);
        grey.update();
    }

    @Override
    public void draw() {
        mapRenderer.render();
        grey.draw();

        if(dialoging)
            dialog.draw();

        debugRenderer.render(physicManager.getWorld(), camera.combined);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys
                    .ENTER:
                if(dialoging) {
                    if(dialog.hasFinished()) {
                        if (!dialog.next())
                            dialoging = false;
                    }
                    else dialog.finish();
                }

            break;

            case Input.Keys
                    .LEFT:
                //grey.moveLeft();
            break;

            case Input.Keys
                    .RIGHT:
                //grey.moveRight();
            break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys
                    .LEFT:
                camera.translate(-10, 0);
            break;

            case Input.Keys
                    .RIGHT:
                camera.translate(10, 0);
            break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
