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
    public boolean end;
    public int enemy;

    public abstract void solve(Data data1, Data data2);

    @Override
    public void beginContact(Contact contact) {
        Data fixtureUserData1 = (Data) contact.getFixtureA().getUserData();
        Data fixtureUserData2 = (Data) contact.getFixtureB().getUserData();

        solve(fixtureUserData1, fixtureUserData2);

        if(check(fixtureUserData1.data, fixtureUserData2.data, Physic.DATA_FOOT))
            footOnGround = true;
    }

    @Override
    public void endContact(Contact contact) {
        Data fixtureUserData1 = (Data) contact.getFixtureA().getUserData();
        Data fixtureUserData2 = (Data) contact.getFixtureB().getUserData();

        solve(fixtureUserData1, fixtureUserData2);

        if(check(fixtureUserData1.data, fixtureUserData2.data, Physic.DATA_FOOT))
            footOnGround = false;
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

    protected String getDialog(Data data1, Data data2){
        return data1.data == Physic.DATA_DIALOG ? data1.dialog : (data2.data == Physic.DATA_DIALOG ? data2.dialog : null);
    }

    protected int getEnemy(Data data1, Data data2){
        return data1.data == Physic.DATA_ENEMY ? data1.enemy : (data2.data == Physic.DATA_ENEMY ? data2.enemy : -1);
    }

    public boolean isFootOnGround() {
        return footOnGround;
    }

    public boolean isDead(){ return dead; }

    public boolean isDamaged() {
        return damage;
    }
}