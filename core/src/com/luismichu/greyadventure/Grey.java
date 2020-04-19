package com.luismichu.greyadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.Physic.PhysicObject;

public class Grey extends PhysicObject {
    private Sprite sprite;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Animation<Texture> animation, animStanding, animRunningLeft, animRunningRight;
    private MyContactListener contactListener;
    private MyAssetManager assetManager;
    private float size = 1.75f * Game.TILE_SIZE;
    private float defAspectRatio;
    private float elapsedTime = 0, factor;
    private boolean jump, onGround, dashing;
    private Array<Sprite> dashSprites;
    private Vector2 linearVelocity;

    public Grey(Vector2 pos, MyAssetManager assetManager, OrthographicCamera camera){
        super();

        this.assetManager = assetManager;

        animStanding = new Animation<>(1 / 2f / Game.WORLD_SPEED, assetManager.getTextures(MyAssetManager.AssetDescriptors.greyStanding));
        animStanding.setPlayMode(Animation.PlayMode.LOOP);

        animRunningLeft = new Animation<>(1 / 6f / Game.WORLD_SPEED, assetManager.getTextures(MyAssetManager.AssetDescriptors.greyRunningL));
        animRunningLeft.setPlayMode(Animation.PlayMode.LOOP);

        animRunningRight = new Animation<>(1 / 6f / Game.WORLD_SPEED, assetManager.getTextures(MyAssetManager.AssetDescriptors.greyRunningR));
        animRunningRight.setPlayMode(Animation.PlayMode.LOOP);

        animation = animStanding;
        sprite = new Sprite(animation.getKeyFrame(0));
        sprite.setBounds(0, 0, size * (sprite.getWidth() / sprite.getHeight()), size);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(pos.x, pos.y);

        setSize(new Vector2(sprite.getWidth() / 2f, sprite.getHeight() / 2f));
        setPosition(pos);
        setUserData(2);
        setFixedRotation(true);
        createObject();

        contactListener = new MyContactListener(this);
        //world.setContactListener(contactListener);

        jump = false;
        onGround = false;
        defAspectRatio = sprite.getWidth() / sprite.getHeight();
        factor = 1;

        this.camera = camera;
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        dashSprites = new Array<>();
        dashing = false;
    }

    public void update(){
        elapsedTime += Gdx.graphics.getDeltaTime();
        sprite = new Sprite(animation.getKeyFrame(elapsedTime));
        defAspectRatio = sprite.getWidth() / sprite.getHeight();
        sprite.setBounds(0, 0, size * defAspectRatio, size / factor);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition((body.getPosition().x) - sprite.getWidth()/2,
                (body.getPosition().y) -sprite.getHeight()/2 * factor);
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));

        if(contactListener.getNumFootContacts() > 0)
            jump = false;

        if(dashing)
            body.setLinearVelocity(linearVelocity);
    }

    public void draw(){
        if(dashSprites.size > 0) {
            sprite.setColor(0.3f, 0.3f, 1, 1);
            Texture t = sprite.getTexture();
            t.getTextureData().prepare();
            Pixmap p = t.getTextureData().consumePixmap();
            p.setColor(1, 1, 1, 1);
            batch.begin();
            sprite.draw(batch);
            batch.end();

            batch.begin();
            for(Sprite s : dashSprites) {
                s.setColor(0, 0, 0, s.getColor().a - 0.015f);
                s.draw(batch);
            }
        }
        else {
            batch.begin();
            batch.setProjectionMatrix(camera.combined);
            sprite.draw(batch);
        }
        batch.end();
    }

    public void jump(){
        if(contactListener.getNumFootContacts() > 0) {
            body.applyLinearImpulse(new Vector2(0, 500000), body.getLocalCenter(), true);
            shift(0.008f);
        }
        else if(!jump){
            jump = true;
            body.setLinearVelocity(body.getLinearVelocity().x, 25);
        }
    }

    public void moveLeft(){
        if(!dashing) {
            animation = animRunningLeft;
            float x = body.getLinearVelocity().x;
            x -= 2;
            body.setLinearVelocity(x < -100 ? -100 : x, body.getLinearVelocity().y);
        }
    }

    public void moveRight(){
        if(!dashing) {
            animation = animRunningRight;
            float x = body.getLinearVelocity().x;
            x += 2;
            body.setLinearVelocity(x > 100 ? 100 : x, body.getLinearVelocity().y);
        }
    }

    public void dash(){
        if(!dashing) {
            dashing = true;
            linearVelocity = new Vector2(body.getLinearVelocity().x > 0 ? 100 : -100, body.getLinearVelocity().y);
            linearVelocity = body.getLinearVelocity().scl(new Vector2(10, 10));
            System.out.println(linearVelocity);
            Timer.schedule(new Timer.Task() {
                               @Override
                               public void run() {
                                   Sprite s = new Sprite(sprite);
                                   s.setColor(0, 0, 0, 1);
                                   dashSprites.add(s);
                               }
                           }, 0f / Game.WORLD_SPEED
                    , 0.05f / Game.WORLD_SPEED
                    , 2);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    body.setLinearVelocity(0, 0);
                    dashing = false;
                }
            }, 0.1f / Game.WORLD_SPEED);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    dashSprites.clear();
                }
            }, 1f / Game.WORLD_SPEED);
        }
    }

    public void stop(){
        animation = animStanding;
    }

    void onGround(){
        if(!onGround) {
            onGround = true;
            System.out.println(body.getLinearVelocity());

            shift(0.0003f * -body.getLinearVelocity().y);
        }
    }

    void shift(final float n){
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               factor += n;
                           }
                       }, 0.01f / Game.WORLD_SPEED
                , 0.03f / Game.WORLD_SPEED
                ,5);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               factor -= n;
                           }
                       }, 0.2f / Game.WORLD_SPEED
                ,0.03f / Game.WORLD_SPEED
                ,5);
    }

    void notOnGround(){
        onGround = false;
    }
}

class MyContactListener implements ContactListener {
    private int numFootContacts = 0;
    private Grey grey;

    MyContactListener(Grey grey){
        this.grey = grey;
    }

    @Override
    public void beginContact(Contact contact) {
        String fixtureUserData = contact.getFixtureA().getUserData().toString();
        int data = Integer.parseInt(fixtureUserData);
        if (data == 3)
            numFootContacts++;
        fixtureUserData = contact.getFixtureB().getUserData().toString();
        data = Integer.parseInt(fixtureUserData);
        if (data == 3)
            numFootContacts++;

        if(numFootContacts > 0)
            grey.onGround();
    }

    @Override
    public void endContact(Contact contact) {
        Object fixtureUserData = contact.getFixtureA().getUserData();
        if ((int)fixtureUserData == 3)
            numFootContacts--;
        fixtureUserData = contact.getFixtureB().getUserData();
        if ((int)fixtureUserData == 3)
            numFootContacts--;

        if(numFootContacts == 0)
            grey.notOnGround();
    }

    public int getNumFootContacts() {
        return numFootContacts;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
