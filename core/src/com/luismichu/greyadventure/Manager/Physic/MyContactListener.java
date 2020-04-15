package com.luismichu.greyadventure.Manager.Physic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {
    private int numFootContacts = 0;
    private MyContactListener contactListener;

    public MyContactListener getInstance(){
        if(contactListener == null)
            contactListener = new MyContactListener();

        return contactListener;
    }

    MyContactListener(){
    }

    @Override
    public void beginContact(Contact contact) {
        //TODO switch
        Object fixtureUserData = contact.getFixtureA().getUserData();
        if ((int)fixtureUserData == Physic.DATA_FOOT)
            numFootContacts++;

        fixtureUserData = contact.getFixtureB().getUserData();
        if ((int)fixtureUserData == Physic.DATA_FOOT)
            numFootContacts++;

        //if(numFootContacts > 0)
    }

    @Override
    public void endContact(Contact contact) {
        Object fixtureUserData = contact.getFixtureA().getUserData();
        if ((int)fixtureUserData == Physic.DATA_FOOT)
            numFootContacts--;

        fixtureUserData = contact.getFixtureB().getUserData();
        if ((int)fixtureUserData == Physic.DATA_FOOT)
            numFootContacts--;

        //if(numFootContacts == 0)
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