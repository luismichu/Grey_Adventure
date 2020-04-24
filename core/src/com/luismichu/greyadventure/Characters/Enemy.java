package com.luismichu.greyadventure.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.luismichu.greyadventure.Game;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.Physic.Physic;
import com.luismichu.greyadventure.Manager.Physic.PhysicObject;

public class Enemy extends PhysicObject {
    private Sprite sprite;
    private Animation<Texture> animation, animAttack;
    private float size = 1.75f, defAspectRatio;
    private float elapsedTime = 0;

    public Enemy(Vector2 pos, Array<Texture> arrayAttack){
        super();

        animAttack = new Animation<Texture>(1 / 5f / Game.WORLD_SPEED,arrayAttack);
        animAttack.setPlayMode(Animation.PlayMode.LOOP);

        animation = animAttack;
        sprite = new Sprite(animation.getKeyFrame(0));
        sprite.setBounds(0, 0, size * (sprite.getWidth() / sprite.getHeight()), size);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(pos.x, pos.y);

        setDensity(3);
        setFriction(1);
        setRestitution(0.1f);
        setGroupIndex((short) -1);
        setPosition(pos);
        setSize(new Vector2(sprite.getWidth() / 2.5f, sprite.getHeight() / 2));
        setUserData(Physic.DATA_ENEMY);
        createObject(false);
    }

    public void update(){
        elapsedTime += Gdx.graphics.getDeltaTime();
        sprite = new Sprite(animation.getKeyFrame(elapsedTime));
        defAspectRatio = sprite.getWidth() / sprite.getHeight();
        sprite.setBounds(0, 0, size * defAspectRatio, size);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition((body.getPosition().x) - sprite.getWidth()/2,
                (body.getPosition().y) -sprite.getHeight()/2 - 0.1f);
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));
    }

    public void draw(SpriteBatch batch){
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }
}
