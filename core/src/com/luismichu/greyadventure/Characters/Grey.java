package com.luismichu.greyadventure.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.luismichu.greyadventure.Game;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.Physic.MyContactListener;
import com.luismichu.greyadventure.Manager.Physic.Physic;
import com.luismichu.greyadventure.Manager.Physic.PhysicObject;

public class Grey extends PhysicObject {
    private Sprite sprite;
    private Animation<Texture> animation, animStanding, animRunningLeft, animRunningRight;
    private MyContactListener contactListener;
    private MyAssetManager assetManager;
    private float size = 1.75f;
    private float defAspectRatio;
    private float elapsedTime = 0, factor;
    private boolean canJump, canDash, onGround, dashing;
    private Array<Sprite> dashSprites;
    private Vector2 linearVelocity;
    private float speed, jump, dashX, dashY;
    private Array<Integer> keys;

    public Grey(Vector2 pos, MyAssetManager assetManager, MyContactListener contactListener, Array<Integer> keys){
        super();

        this.assetManager = assetManager;
        this.keys = keys;

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

        speed = 5;
        jump = 6;

        setDensity(3);
        setFriction(0);
        setRestitution(0);
        setSize(new Vector2(sprite.getWidth() / 2.5f, sprite.getHeight() / 2 - 0.1f));
        setPosition(pos);
        setUserData(Physic.DATA_GREY);
        setFixedRotation(true);
        setGroupIndex((short) -1);
        createObject(false);

        setSize(new Vector2(sprite.getWidth() / 3.5f, 0.2f));
        setUserData(Physic.DATA_FOOT);
        createFixture(new Vector2(0, -sprite.getHeight() / 2f - 0.2f), true);

        setSize(new Vector2(sprite.getWidth() / 3.5f, 0.1f));
        setUserData(Physic.DATA_GREY);
        setDensity(100);
        setFriction(5);
        setRestitution(0);
        createFixture(new Vector2(0, -sprite.getHeight() / 2f), false);

        this.contactListener = contactListener;

        canJump = false;
        onGround = false;
        defAspectRatio = sprite.getWidth() / sprite.getHeight();
        factor = 1;

        dashSprites = new Array<>();
        dashing = false;
        canDash = true;
    }

    public void update(){
        elapsedTime += Gdx.graphics.getDeltaTime();
        sprite = new Sprite(animation.getKeyFrame(elapsedTime));
        defAspectRatio = sprite.getWidth() / sprite.getHeight();
        sprite.setBounds(0, 0, size * defAspectRatio, size / factor);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition((body.getPosition().x) - sprite.getWidth()/2,
                (body.getPosition().y) -sprite.getHeight()/2 * factor - 0.1f);
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));

        if(contactListener.isFootOnGround())
            onGround();
        else
            notOnGround();

        if(dashing)
            body.setLinearVelocity(dashX, dashY);
    }

    public void draw(SpriteBatch batch){
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
            batch.draw(sprite.getTexture(), sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        }
        batch.end();
    }

    public void jump(){
        if(onGround) {
            body.setLinearVelocity(body.getLinearVelocity().x, jump);
            shift(0.04f);
            canJump = false;
        }
        else if(canJump){
            canJump = false;
            body.setLinearVelocity(body.getLinearVelocity().x, jump);
            shift(0.04f);
        }
    }

    public void moveLeft(){
        if(!dashing) {
            animation = animRunningLeft;
            float x = body.getLinearVelocity().x;
            x -= speed / 5;
            body.setLinearVelocity(Math.max(x, -speed), body.getLinearVelocity().y);
        }
    }

    public void moveRight(){
        if(!dashing) {
            animation = animRunningRight;
            float x = body.getLinearVelocity().x;
            x += speed / 5;
            body.setLinearVelocity(Math.min(x, speed), body.getLinearVelocity().y);
        }
    }

    public void dash(){
        if(!dashing && canDash) {
            dashing = true;
            canDash = false;
            dashX = 0;
            dashY = 0;
            float factor = 3.5f;
            if(keys.size > 0) {
                if (keys.contains(Input.Keys.W, false)) {
                    dashY = speed * factor;
                    if (keys.contains(Input.Keys.A, false))
                        dashX = -speed * factor;
                    else if (keys.contains(Input.Keys.D, false))
                        dashX = speed * factor;
                } else if (keys.peek().equals(Input.Keys.A))
                    dashX = -speed * factor;
                else if (keys.peek().equals(Input.Keys.D))
                    dashX = speed * factor;
            }

            body.setLinearVelocity(dashX, dashY);
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
                    canDash = true;
                }
            }, 1f / Game.WORLD_SPEED);

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

    public void onGround(){
        if(!onGround) {
            onGround = true;
            canJump = true;

            shift(Math.max(0, 0.01f * -body.getLinearVelocity().y));
        }
    }

    public void shift(final float n){
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

    public void notOnGround(){
        onGround = false;
    }

    public void setPos(Vector2 pos){
        body.setTransform(pos, 0);
    }

    public Vector2 getPos(){
        return body.getPosition();
    }
}