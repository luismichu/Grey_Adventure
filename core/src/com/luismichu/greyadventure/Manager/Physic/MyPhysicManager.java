package com.luismichu.greyadventure.Manager.Physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class MyPhysicManager {
    private static MyPhysicManager physicManager;
    private static World world;

    public static MyPhysicManager getInstance(){
        if(physicManager == null)
            physicManager = new MyPhysicManager();

        return physicManager;
    }
    private MyPhysicManager(){
        world = new World(Physic.gravity,true);
    }

    public void setContactListener(MyContactListener contactListener){
        world.setContactListener(contactListener);
    }

    public void update(){
        world.step(Physic.TIME_STEP, Physic.VELOCITY_ITERATIONS, Physic.POSITION_ITERATIONS);
    }

    public void update(float worldSpeed){
        world.step(Physic.TIME_STEP * worldSpeed * Gdx.graphics.getDeltaTime(), Physic.VELOCITY_ITERATIONS, Physic.POSITION_ITERATIONS);
    }

    public World getWorld(){
        return world;
    }

    public Body createBody(BodyDef bodyDef){
        return world.createBody(bodyDef);
    }
}