package com.luismichu.greyadventure.Levels;

import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.luismichu.greyadventure.Characters.Enemy;
import com.luismichu.greyadventure.Game;
import com.luismichu.greyadventure.Characters.Grey;
import com.luismichu.greyadventure.Manager.*;
import com.luismichu.greyadventure.Manager.Physic.MyContactListener;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;
import com.luismichu.greyadventure.Manager.Physic.Physic;
import sun.jvm.hotspot.opto.PhiNode;

public class Level1 extends Level {
    private static Level1 level1;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera cameraTiled;
    private MyCustomContactListener contactListener;
    private Sound deathSound;
    private Array<Sound> deathPlayerSound;
    private RayHandler rayHandler;

    public static Level1 create(MyAssetManager assetManager, MyPreferenceManager preferenceManager, MyPhysicManager physicManager,
                         OrthographicCamera camera, OrthographicCamera cameraUI, GameState gameState){
        level1 = new Level1(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
        return getInstance();
    }

    public static Level1 getInstance(){
        return level1;
    }

    private Level1(MyAssetManager assetManager, MyPreferenceManager preferenceManager, MyPhysicManager physicManager,
                   OrthographicCamera camera, OrthographicCamera cameraUI, GameState gameState){
        super(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);

        assetManager.addToQueue(MyAssetManager.AssetDescriptors.greyRunningL);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.greyRunningR);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.greyStanding);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.heart);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.heart_empty);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.map1);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.enemyRedAttack);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.deathSound);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.deathPlayerSound);

        assetManager.loadQueue();

        Vector2 pos = new Vector2(2, 7);

        map = (TiledMap) assetManager.get(MyAssetManager.AssetDescriptors.map1);
        mapRenderer = new OrthogonalTiledMapRenderer(map, batch);
        cameraTiled = new OrthographicCamera(16 * Physic.P2M, 9 * Physic.P2M);
        cameraTiled.position.set(8 * Physic.P2M, pos.y * Physic.P2M, 0);
        camera.position.set(8, pos.y, 0);
        cameraTiled.update();
        camera.update();
        mapRenderer.setView(cameraTiled);

        contactListener = new MyCustomContactListener(this);
        physicManager.setContactListener(contactListener);

        grey = new Grey(pos, assetManager, contactListener, keys);

        createObjectsFromLayer(Physic.OBJECT_LAYER, false, Physic.DATA_GROUND, Physic.MASK_GROUND, Physic.GROUP_GROUND);
        createObjectsFromLayer(Physic.DEATH_LAYER, true, Physic.DATA_DEATH, Physic.MASK_GROUND, Physic.GROUP_GROUND);
        createObjectsFromLayer(Physic.HIDDEN_LAYER, false, Physic.CATEGORY_HIDDEN, Physic.MASK_HIDDEN, Physic.GROUP_HIDDEN);

        createEnemies();
        createDialogs();

        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        deathSound = assetManager.getSound(MyAssetManager.AssetDescriptors.deathSound);
        deathPlayerSound = assetManager.getSounds(MyAssetManager.AssetDescriptors.deathPlayerSound);

        rayHandler = new RayHandler(physicManager.getWorld());
        rayHandler.resizeFBO(640, 360);
        rayHandler.setAmbientLight(0.01f, 0.01f, 0.01f, 0.3f);
        rayHandler.setBlurNum(30);

        PointLight light = new PointLight(rayHandler, 50);
        light.setDistance(8);
        light.setColor(new Color(0.01f,0.01f,0.1f,0.9f));
        light.setSoft(false);
        light.setXray(true);
        light.attachToBody(grey.getBody());

        DirectionalLight sun = new DirectionalLight(rayHandler, 500, new Color(0.05f,0.05f,0.1f,1), -91);
        sun.setSoft(true);
        sun.setSoftnessLength(2);
        Light.setGlobalContactFilter(Physic.CATEGORY_HIDDEN, Physic.MASK_HIDDEN, Physic.GROUP_HIDDEN);
    }

    @Override
    protected void loadDialog(String name) {
        if((!name.equals("l1d3") && !name.equals("l1d4")) || deathCount >= 1) {
            System.out.println(name);
            Array<String> newDialog = Dialogs.get(name);
            if (newDialog != null) {
                if(!newDialog.isEmpty()) {
                    dialog.setMessages(newDialog);
                    dialoging = true;
                    boolean found = false;
                    int size = dialogBodies.size;
                    do{
                        size--;
                        found = dialogBodies.get(size).getUserData().toString().equals(name);
                    } while(!found && size > 0);
                    if(found) {
                        garbageBodies.add(dialogBodies.removeIndex(size));
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        if(!paused) {
            if (dialoging) {
                dialog.update();
            }
            else {
                physicManager.update(Game.WORLD_SPEED);
                if (contactListener.isDead() && canDie || grey.isDead()) {
                    radius = Math.min(radius + Gdx.graphics.getWidth() * Gdx.graphics.getDeltaTime(), Gdx.graphics.getWidth());
                    die();
                } else if (reviving) {
                    radius -= 2 * Gdx.graphics.getWidth() * Gdx.graphics.getDeltaTime();
                    if (radius < 0)
                        reviving = false;
                } else {
                    if (contactListener.isDamaged())
                        grey.damage();

                    int size = keys.size - 1;
                    boolean flag = true;
                    if (keys.size > 0) {
                        do {
                            switch (keys.get(size)) {
                                case Input.Keys.LEFT:
                                case Input.Keys.A:
                                    grey.moveLeft();
                                    flag = false;
                                    break;

                                case Input.Keys.RIGHT:
                                case Input.Keys.D:
                                    grey.moveRight();
                                    flag = false;
                                    break;

                                default:
                                    size--;
                            }
                        } while (flag && size >= 0);
                    }
                }

                contactListener.damage = false;
            }

            grey.update();
            for(Enemy enemy : enemies)
                enemy.update();
            cameraTiled.position.set(cameraTiled.position.x + (grey.getPos().x * Physic.P2M - cameraTiled.position.x) * CAMERA_SPEED_X,
                    cameraTiled.position.y + (grey.getPos().y * Physic.P2M - cameraTiled.position.y) * CAMERA_SPEED_Y, 0);
            camera.position.set(camera.position.x + (grey.getPos().x - camera.position.x) * CAMERA_SPEED_X,
                    camera.position.y + (grey.getPos().y - camera.position.y) * CAMERA_SPEED_Y, 0);

            cameraTiled.position.x = MathUtils.clamp(cameraTiled.position.x, 8 * Physic.P2M, cameraTiled.position.x);
            camera.position.x = MathUtils.clamp(camera.position.x, 8, cameraTiled.position.x);
            cameraTiled.position.y = MathUtils.clamp(cameraTiled.position.y, 4.5f * Physic.P2M, 15 * Physic.P2M);
            camera.position.y = MathUtils.clamp(camera.position.y, 4.5f, 15);

            camera.update();
            cameraUI.update();
            cameraTiled.update();

            for(Body gBody : garbageBodies)
                physicManager.getWorld().destroyBody(gBody);

            garbageBodies.clear();
        }
    }

    @Override
    public void draw() {
        mapRenderer.setView(cameraTiled);
        mapRenderer.render();
        batch.setProjectionMatrix(camera.combined);
        for(Enemy enemy : enemies)
            enemy.draw(batch);
        grey.draw(batch);

        //debugRenderer.render(physicManager.getWorld(), camera.combined); //TODO

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        batch.setProjectionMatrix(cameraUI.combined);
        grey.drawHearts(batch, cameraUI);

        if(dialoging)
            dialog.draw();

        if(contactListener.isDead() || reviving || grey.isDead()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 1f);
            shapeRenderer.circle(((grey.getPos().x - camera.position.x) / 16 + 0.5f) * Gdx.graphics.getWidth(),
                    ((grey.getPos().y - camera.position.y) / 9 + 0.5f) * Gdx.graphics.getHeight(), radius);
            shapeRenderer.end();
        }

        if(paused){
            if(alpha < 0.4)
                alpha += 0.8 * Gdx.graphics.getDeltaTime();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(0, 0, 0, alpha);
            shapeRenderer.setProjectionMatrix(cameraUI.combined);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, cameraUI.viewportWidth, cameraUI.viewportHeight);
            shapeRenderer.setColor(0, 0, 0, alpha + 0.4f);
            shapeRenderer.rect(0, 0, cameraUI.viewportWidth, 200);
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);

            stage.act();
            stage.draw();
        }
        else if(alpha > 0){
            alpha -= 0.8 * Gdx.graphics.getDeltaTime();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(0, 0, 0, alpha);
            shapeRenderer.setProjectionMatrix(cameraUI.combined);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, cameraUI.viewportWidth, cameraUI.viewportHeight);
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void die(){
        if(!dying){
            dying = true;
            grey.setInvincible(true);
            grey.setStatic();
            deathCount++;
            deathPlayerSound.random().play();
            physicManager.getWorld().clearForces();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    grey.setPos(new Vector2(2, 7f));
                    cameraTiled.position.set(grey.getPos(), 0);
                    camera.position.set(grey.getPos(), 0);
                    physicManager.getWorld().clearForces();
                }
            }, 1f);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    radius = Gdx.graphics.getWidth();
                    reviving = true;
                    contactListener.dead = false;
                    canDie = false;
                    grey.regenerate();
                    physicManager.getWorld().clearForces();
                }
            }, 1.5f);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    contactListener.dead = false;
                    dying = false;
                    canDie = true;
                    grey.setInvincible(false);
                }
            }, 1.75f);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    grey.setDynamic();
                }
            }, 2f);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.ENTER:
                if(dialoging) {
                    if(dialog.hasFinished()) {
                        if (!dialog.next())
                            dialoging = false;
                    }
                    else dialog.finish();
                }

            break;

            case Input.Keys.SPACE:
                if(!dialoging)
                    grey.jump();
            break;

            case Input.Keys.SHIFT_LEFT:
                if(!dialoging)
                    grey.dash();
            break;

            case Input.Keys.ESCAPE:
                if(paused)
                    resume();
                else
                    pause();
                break;

            default:
                keys.add(keycode);
            break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.removeValue(keycode, true);
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                grey.stop();
            break;
        }
        return true;
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