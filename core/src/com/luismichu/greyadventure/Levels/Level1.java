package com.luismichu.greyadventure.Levels;

import box2dLight.DirectionalLight;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.luismichu.greyadventure.Characters.Enemy;
import com.luismichu.greyadventure.Game;
import com.luismichu.greyadventure.Characters.Grey;
import com.luismichu.greyadventure.Manager.Dialog;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.MyPreferenceManager;
import com.luismichu.greyadventure.Manager.Physic.MyContactListener;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;
import com.luismichu.greyadventure.Manager.Physic.Physic;

public class Level1 extends Level {
    private static Level1 level1;
    private boolean dialoging;
    private Grey grey;
    private Enemy enemy;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;
    private OrthographicCamera cameraTiled;
    private MyCustomContactListener contactListener;
    private final float CAMERA_SPEED_X = 0.3f;
    private final float CAMERA_SPEED_Y = 0.1f;
    private ShapeRenderer shapeRenderer;
    private float radius;
    private boolean dying, canDie, reviving;
    private Sound deathSound;
    private Array<Sound> deathPlayerSound;
    private RayHandler rayHandler;

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
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.enemyRedAttack);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.deathSound);
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.deathPlayerSound);

        assetManager.loadQueue();

        Vector2 pos = new Vector2(2, 7);

        batch = new SpriteBatch();

        map = (TiledMap) assetManager.get(MyAssetManager.AssetDescriptors.map1);
        mapRenderer = new OrthogonalTiledMapRenderer(map, batch);
        cameraTiled = new OrthographicCamera(16 * Physic.P2M, 9 * Physic.P2M);
        cameraTiled.position.set(8 * Physic.P2M, pos.y * Physic.P2M, 0);
        camera.position.set(8, pos.y, 0);
        cameraTiled.update();
        camera.update();
        mapRenderer.setView(cameraTiled);

        dialog = new Dialog(MyAssetManager.AssetDescriptors.dialogMap1, cameraUI);
        dialoging = false;

        contactListener = new MyCustomContactListener();
        physicManager.setContactListener(contactListener);

        keys = new Array<>();

        grey = new Grey(pos, assetManager, contactListener, keys);
        pos = new Vector2(9, 7f);
        enemy = new Enemy(pos, assetManager.getTextures(MyAssetManager.AssetDescriptors.enemyRedAttack));

        createObjectsFromLayer(Physic.OBJECT_LAYER, false, Physic.DATA_GROUND);
        createObjectsFromLayer(Physic.DEATH_LAYER, true, Physic.DATA_DEATH);

        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        deathSound = assetManager.getSound(MyAssetManager.AssetDescriptors.deathSound);
        deathPlayerSound = assetManager.getSounds(MyAssetManager.AssetDescriptors.deathPlayerSound);

        shapeRenderer = new ShapeRenderer();
        radius = 0;
        dying = false;
        canDie = true;
        reviving = false;

        rayHandler = new RayHandler(physicManager.getWorld());
        rayHandler.resizeFBO(640, 360);
        rayHandler.setAmbientLight(0.01f, 0.01f, 0.01f, 0.1f);
        rayHandler.setBlurNum(3);

        PointLight light = new PointLight(rayHandler, 50);
        light.setDistance(5);
        light.setColor(new Color(0.01f,0.01f,0.1f,1));
        light.setSoft(false);
        light.setXray(true);
        light.attachToBody(grey.getBody());

        DirectionalLight sun = new DirectionalLight(rayHandler, 500, new Color(0.05f,0.05f,0.15f,1), -91);
        sun.setSoft(true);
        sun.setSoftnessLength(3);
    }

    private void createObjectsFromLayer(String layer, boolean sensor, short data){
        MapObjects objects = map.getLayers().get(layer).getObjects();
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle r = rectangleObject.getRectangle();
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((r.x + r.width / 2) / Physic.P2M, (r.y + r.height / 2) / Physic.P2M);

            Body body = physicManager.getWorld().createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(r.width / 2 / Physic.P2M, r.height / 2 / Physic.P2M);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = sensor;

            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(data);
            shape.dispose();
        }
    }

    @Override
    public void update() {
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
        mapRenderer.setView(cameraTiled);
        if (dialoging)
            dialog.update();
        else if (!contactListener.isDead())
            physicManager.update(Game.WORLD_SPEED);
        grey.update();
        enemy.update();
        cameraTiled.position.set(cameraTiled.position.x + (grey.getPos().x * Physic.P2M - cameraTiled.position.x) * CAMERA_SPEED_X * 1.5f,
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

        if (contactListener.isDead() && canDie) {
            radius = Math.min(radius + Gdx.graphics.getWidth() * Gdx.graphics.getDeltaTime(), Gdx.graphics.getWidth());
            die();
        }
        else if(reviving){
            radius -= 2 * Gdx.graphics.getWidth() * Gdx.graphics.getDeltaTime();
            if(radius < 0)
                reviving = false;
        }
    }

    @Override
    public void draw() {
        mapRenderer.render();
        batch.setProjectionMatrix(camera.combined);
        enemy.draw(batch);
        grey.draw(batch);

        //debugRenderer.render(physicManager.getWorld(), camera.combined);

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        if(dialoging)
            dialog.draw();

        if(contactListener.isDead() || reviving) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 1f);
            shapeRenderer.circle(((grey.getPos().x - camera.position.x) / 16 + 0.5f) * Gdx.graphics.getWidth(),
                    ((grey.getPos().y - camera.position.y) / 9 + 0.5f) * Gdx.graphics.getHeight(), radius);
            shapeRenderer.end();
        }
    }

    public void die(){
        if(!dying){
            dying = true;
            deathPlayerSound.random().play();
            physicManager.getWorld().clearForces();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    grey.setPos(new Vector2(2, 7f));
                    cameraTiled.position.set(grey.getPos(), 0);
                    camera.position.set(grey.getPos(), 0);
                }
            }, 1f);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    radius = Gdx.graphics.getWidth();
                    reviving = true;
                    contactListener.dead = false;
                    canDie = false;
                }
            }, 1.5f);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    contactListener.dead = false;
                    dying = false;
                    canDie = true;
                }
            }, 1.75f);
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
                grey.jump();
            break;

            case Input.Keys.SHIFT_LEFT:
                grey.dash();
            break;

            case Input.Keys.CONTROL_LEFT:
                contactListener.dead = true;
                //deathSound.play(1, (float)(Math.random() * 0.25 + 0.75), 0);
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

class MyCustomContactListener extends MyContactListener {
    public MyCustomContactListener(){
        footOnGround = false;
        dead = false;
        damage = false;
    }

    @Override
    public void solve(short data1, short data2) {
        if(check(data1, data2, Physic.DATA_FOOT))
            footOnGround = !footOnGround;

        if(check(data1, data2, Physic.DATA_GREY, Physic.DATA_DEATH))
            dead = true;

        if(check(data1, data2, Physic.DATA_GREY, Physic.DATA_ENEMY))
            damage = true;
    }
}