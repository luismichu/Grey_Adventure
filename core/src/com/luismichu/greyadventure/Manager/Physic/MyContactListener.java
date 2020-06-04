package com.luismichu.greyadventure.Manager.Physic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.luismichu.greyadventure.Levels.Level;

public abstract class MyContactListener implements ContactListener {
    protected Level level;
    protected boolean footOnGround;
    public boolean dead;
    public boolean damage;

    public abstract void solve(Object data1, Object data2);

    @Override
    public void beginContact(Contact contact) {
        Object fixtureUserData1 = contact.getFixtureA().getUserData();
        Object fixtureUserData2 = contact.getFixtureB().getUserData();

        solve(fixtureUserData1, fixtureUserData2);
    }

    @Override
    public void endContact(Contact contact) {
        Object fixtureUserData1 = contact.getFixtureA().getUserData();
        Object fixtureUserData2 = contact.getFixtureB().getUserData();

        solve(fixtureUserData1, fixtureUserData2);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    protected boolean check(short data1, short data2, short data3, short data4){
        return ((data1 == data3 || data2 == data3) && (data1 == data4 || data2 == data4));
    }

    protected boolean check(short data1, short data2, short data3){
        return (data1 == data3 || data2 == data3);
    }

    protected boolean check(short data1, short data2){
        return data1 == data2;
    }

    public boolean isFootOnGround() {
        return footOnGround;
    }

    public boolean isDead(){ return dead; }

    public boolean isDamaged() {
        return damage;
    }
}