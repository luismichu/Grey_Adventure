package com.luismichu.greyadventure.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.luismichu.greyadventure.Manager.MyPreferenceManager;
import com.luismichu.greyadventure.Manager.Physic.Data;
import com.luismichu.greyadventure.Manager.Physic.MyContactListener;
import com.luismichu.greyadventure.Manager.Physic.Physic;
import com.luismichu.greyadventure.Manager.Physic.PhysicObject;

public class Grey extends PhysicObject {
    private Sprite sprite;
    private Texture heart, heart_empty;
    private Animation<Texture> animation, animStanding, animRunningLeft, animRunningRight;
    private MyContactListener contactListener;
    private MyAssetManager assetManager;
    private MyPreferenceManager preferenceManager;
    private float size = 1.75f;
    private float defAspectRatio, heartAspectRatio;
    private float elapsedTime = 0, elapsedTimeInvincible = 0, factor;
    private boolean canJump, canDash, onGround, dashing, invincible, dead, heartBlink, kill;
    private Array<Sprite> dashSprites;
    private float speed, jump, dashX, dashY;
    private int lifePoints;
    private Array<Integer> keys;
    private Sound landSound, damageSound;
    private Color dashColor, red, green, blue;

    public Grey(Vector2 pos, MyAssetManager assetManager, MyPreferenceManager preferenceManager, MyContactListener contactListener, Array<Integer> keys){
        super();

        this.assetManager = assetManager;
        this.preferenceManager = preferenceManager;
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

        heart = assetManager.getTexture(MyAssetManager.AssetDescriptors.heart);
        heart_empty = assetManager.getTexture(MyAssetManager.AssetDescriptors.heart_empty);
        heartAspectRatio = (float)heart.getWidth() / heart.getHeight();

        speed = 5;
        jump = 6;

        setDensity(3);
        setFriction(0);
        setRestitution(0);
        setSize(new Vector2(sprite.getWidth() / 2.5f, sprite.getHeight() / 2f));
        setPosition(pos);
        setUserData(new Data(Physic.DATA_GREY));
        setFixedRotation(true);
        setGroupIndex(Physic.GROUP_GROUND);
        createObject(false);

        setSize(new Vector2(sprite.getWidth() / 2.6f, 0.1f));
        setUserData(new Data(Physic.DATA_FOOT));
        createFixture(new Vector2(0, -sprite.getHeight() / 2f - 0.1f), true);

        setSize(new Vector2(sprite.getWidth() / 2.5f, 0.1f));
        setUserData(new Data(Physic.DATA_GREY));
        setDensity(1);
        setFriction(20);
        setRestitution(0);
        createCircleFixture(new Vector2(0, -sprite.getHeight() / 3.1f), false);

        this.contactListener = contactListener;

        canJump = false;
        onGround = false;
        defAspectRatio = sprite.getWidth() / sprite.getHeight();
        factor = 1;

        dashSprites = new Array<>();
        dashing = false;
        canDash = true;

        lifePoints = 3;
        invincible = false;
        dead = false;
        heartBlink = false;
        kill = false;

        landSound = assetManager.getSound(MyAssetManager.AssetDescriptors.landSound);
        damageSound = assetManager.getSound(MyAssetManager.AssetDescriptors.damageSound);

        red = new Color(1, 0.3f, 0.3f, 1);
        green = new Color(0.3f, 1, 0.3f, 1);
        blue = new Color(0.3f, 0.3f, 1, 1);
    }

    public void update(){
        elapsedTime += Gdx.graphics.getDeltaTime();
        sprite = new Sprite(animation.getKeyFrame(elapsedTime));
        defAspectRatio = sprite.getWidth() / sprite.getHeight();
        sprite.setBounds(0, 0, size * defAspectRatio, size / factor);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition((body.getPosition().x) - sprite.getWidth()/2,
                (body.getPosition().y) -sprite.getHeight()/2 * factor - 0.01f);
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
            sprite.setColor(dashColor);
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

    public void drawHearts(SpriteBatch batch, OrthographicCamera camera){
        batch.begin();
        for(int i=0;i<getLifePoints();i++)
            batch.draw(heart, (0.055f  * i + 0.015f) * camera.viewportWidth, 0.85f * camera.viewportHeight, 0.05f * camera.viewportWidth, 0.05f * camera.viewportWidth * heartAspectRatio);
        if(heartBlink) {
            elapsedTimeInvincible += Gdx.graphics.getDeltaTime();
            if ((int)(elapsedTimeInvincible % 0.2 * 10) == 1) {
                batch.draw(heart, (0.055f * getLifePoints() + 0.015f) * camera.viewportWidth, 0.85f * camera.viewportHeight, 0.05f * camera.viewportWidth, 0.05f * camera.viewportWidth * heartAspectRatio);
                for(int i=getLifePoints()+1;i<3;i++) {
                    if(i >= 0)
                        batch.draw(heart_empty, (0.055f * i + 0.015f) * camera.viewportWidth, 0.85f * camera.viewportHeight, 0.05f * camera.viewportWidth, 0.05f * camera.viewportWidth * heartAspectRatio);
                }
            }
        }

            for (int i = getLifePoints(); i < 3; i++) {
                if (i >= 0)
                    batch.draw(heart_empty, (0.055f * i + 0.015f) * camera.viewportWidth, 0.85f * camera.viewportHeight, 0.05f * camera.viewportWidth, 0.05f * camera.viewportWidth * heartAspectRatio);
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
            x -= speed / 10;
            body.setLinearVelocity(Math.max(x, -speed), body.getLinearVelocity().y);
        }
    }

    public void moveRight(){
        if(!dashing) {
            animation = animRunningRight;
            float x = body.getLinearVelocity().x;
            x += speed / 10;
            body.setLinearVelocity(Math.min(x, speed), body.getLinearVelocity().y);
        }
    }

    public void dash(float factor, Color color){
        dashColor = color;
        if(!dashing && canDash) {
            dashing = true;
            canDash = false;
            dashX = 0;
            dashY = 0;
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

    public void justDash(){
        dash(3.5f, red);
    }

    public void dashInvencible(){
        invincible = true;
        dash(5f, green);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                invincible = false;
            }
        }, 1f / Game.WORLD_SPEED);
    }

    public void dashAndKill(){
        invincible = true;
        kill = true;
        dash(5f, blue);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                invincible = false;
                kill = false;
            }
        }, 1f / Game.WORLD_SPEED);
    }

    public void stop(){
        animation = animStanding;
    }

    public void onGround(){
        if(!onGround) {
            onGround = true;
            canJump = true;

            if(preferenceManager.isMusicOn())
                landSound.play(preferenceManager.getVolume() / 100f / 7);
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

    public void setInvincible(boolean invincible){
        this.invincible = invincible;
    }

    public boolean isDead(){
        return dead;
    }

    public boolean kill(){ return kill; }

    public int getLifePoints(){ return lifePoints; }

    public void damage(){
        if(!invincible) {
            if(lifePoints == 0)
                dead = true;
            else {
                if(preferenceManager.isMusicOn())
                    damageSound.play(preferenceManager.getVolume() / 100f / 7);
                invincible = true;
                lifePoints--;
                heartBlink = true;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       invincible = false;
                                       heartBlink = false;
                                       elapsedTimeInvincible = 0;
                                   }
                               }, 0.4f / Game.WORLD_SPEED
                        , 0
                        , 0);
            }
        }
    }

    public void regenerate(){
        dead = false;
        lifePoints = 3;
    }
}