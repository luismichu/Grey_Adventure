package com.luismichu.greyadventure.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.luismichu.greyadventure.Characters.Enemy;
import com.luismichu.greyadventure.Characters.Grey;
import com.luismichu.greyadventure.Manager.*;
import com.luismichu.greyadventure.Manager.Physic.Data;
import com.luismichu.greyadventure.Manager.Physic.MyContactListener;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;
import com.luismichu.greyadventure.Manager.Physic.Physic;

public abstract class Level implements InputProcessor {
    protected TiledMap map;
    protected TiledMapRenderer mapRenderer;
    protected Dialog dialog;
    protected Array<Body> dialogBodies, garbageBodies;
    protected MyAssetManager assetManager;
    protected MyPreferenceManager preferenceManager;
    protected MyPhysicManager physicManager;
    protected OrthographicCamera camera;
    protected OrthographicCamera cameraUI;
    protected GameState gameState;
    protected Array<Integer> keys;
    protected Skin skin;
    protected Label pauseText;
    protected TextButton btResume, btMainMenu;
    protected boolean paused, finished;
    protected Stage stage;
    protected boolean dialoging;
    protected Grey grey;
    protected Array<Enemy> enemies;
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;
    protected float radius, alpha, alpha2;
    protected boolean dying, canDie, reviving, fading, next, end;
    protected final float CAMERA_SPEED_X = 0.07f;
    protected final float CAMERA_SPEED_Y = 0.05f;
    protected int deathCount;
    protected Music music;

    protected Level(MyAssetManager assetManager, final MyPreferenceManager preferenceManager, MyPhysicManager physicManager,
                    OrthographicCamera camera, OrthographicCamera cameraUI, GameState gameState){
        this.assetManager = assetManager;
        this.preferenceManager = preferenceManager;
        this.physicManager = physicManager;
        this.camera = camera;
        this.cameraUI = cameraUI;
        this.gameState = gameState;
        assetManager.addToQueue(MyAssetManager.AssetDescriptors.skin);
        assetManager.loadQueue();
        skin = assetManager.getSkin(MyAssetManager.AssetDescriptors.skin);

        dialog = new Dialog(cameraUI);
        dialoging = false;

        batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();
        radius = 0;
        dying = false;
        canDie = true;
        reviving = false;
        alpha = 0;

        keys = new Array<>();
        enemies = new Array<>();

        pauseText = new Label("Pausa", skin);
        pauseText.setFontScale(3f);
        pauseText.setPosition(0.1f * Gdx.graphics.getWidth(), 0.1f * Gdx.graphics.getHeight());

        btResume = new TextButton("Continuar", skin);
        btResume.setTransform(true);
        btResume.setScale(0.8f);
        btResume.setPosition(0.7f * Gdx.graphics.getWidth(), 0.15f * Gdx.graphics.getHeight());

        btMainMenu = new TextButton("Menu Principal", skin);
        btMainMenu.setTransform(true);
        btMainMenu.setScale(0.8f);
        btMainMenu.setPosition(0.7f * Gdx.graphics.getWidth(), 0.05f * Gdx.graphics.getHeight());

        btResume.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resume();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        btMainMenu.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                finished = true;
                dispose();
                preferenceManager.setVolume(preferenceManager.getVolume() * 2);
                if(music != null)
                    music.stop();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        stage = new Stage();
        stage.addActor(pauseText);
        stage.addActor(btResume);
        stage.addActor(btMainMenu);

        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode){
                if(keycode == Input.Keys.ESCAPE)
                    resume();
                return true;
            }
        });

        paused = false;
        finished = false;
        fading = true;
        next = false;
        end = false;

        deathCount = 0;

        garbageBodies = new Array<>();
    }

    protected void createObjectsFromLayer(String layer, boolean sensor, short category, short mask, short group){
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
            fixture.setUserData(new Data(category));
            Filter filter = new Filter();
            filter.categoryBits = category;
            filter.maskBits = mask;
            filter.groupIndex = group;
            fixture.setFilterData(filter);
            shape.dispose();
        }
    }

    protected void createEnemies(Array<Texture> textureArray){
        MapObjects objects = map.getLayers().get(Physic.ENEMIES_LAYER).getObjects();
        for (int i = 0; i < objects.getByType(RectangleMapObject.class).size; i++) {
            Rectangle r = objects.getByType(RectangleMapObject.class).get(i).getRectangle();
            enemies.add(new Enemy(new Vector2(r.x / Physic.P2M, r.y / Physic.P2M), textureArray, i));
        }
    }

    protected void createDialogs(){
        MapObjects objects = map.getLayers().get(Physic.DIALOGS_LAYER).getObjects();
        dialogBodies = new Array<>();
        for(MapObject o : objects){
            RectangleMapObject rectangleObject = (RectangleMapObject) o;
            Rectangle r = rectangleObject.getRectangle();
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((r.x + r.width / 2) / Physic.P2M, (r.y + r.height / 2) / Physic.P2M);

            Body body = physicManager.getWorld().createBody(bodyDef);
            body.setUserData(new Data(Physic.DATA_DIALOG, o.getName()));
            dialogBodies.add(body);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(r.width / 2 / Physic.P2M, r.height / 2 / Physic.P2M);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;

            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(new Data(Physic.DATA_DIALOG, o.getName()));
            Filter filter = new Filter();
            filter.categoryBits = Physic.CATEGORY_HIDDEN;
            filter.maskBits = Physic.MASK_HIDDEN;
            filter.groupIndex = Physic.GROUP_HIDDEN;
            fixture.setFilterData(filter);
            shape.dispose();
        }
    }

    protected abstract void loadDialog(String name);
    public abstract void update();
    public abstract void draw();
    public abstract void dispose();

    protected void pause(){
        paused = true;
        preferenceManager.setVolume(preferenceManager.getVolume() / 2);
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
    }

    protected void resume(){
        paused = false;
        preferenceManager.setVolume(preferenceManager.getVolume() * 2);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(this);
    }

    public boolean hasFinished(){
        return finished;
    }

    public boolean next(){
        return next;
    }

    public boolean end(){
        return end;
    }

    public GameState getGameState() {
        return gameState;
    }

    static class MyCustomContactListener extends MyContactListener {
        public MyCustomContactListener(Level level){
            this.level = level;
            footOnGround = false;
            dead = false;
            damage = false;
            end = false;
        }

        @Override
        public void solve(Data data1, Data data2) {
            if(check(data1.data, data2.data, Physic.DATA_GREY, Physic.DATA_DEATH))
                dead = true;

            else if(check(data1.data, data2.data, Physic.DATA_GREY, Physic.DATA_END))
                end = true;

            else if(check(data1.data, data2.data, Physic.DATA_GREY, Physic.DATA_DIALOG))
                level.loadDialog(getDialog(data1, data2));

            else if(check(data1.data, data2.data, Physic.DATA_GREY, Physic.DATA_ENEMY)) {
                damage = true;
                enemy = getEnemy(data1, data2);
            }
        }
    }
}