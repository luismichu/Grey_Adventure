package com.luismichu.greyadventure.Manager.Physic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;

public class PhysicObject {
    private BodyDef.BodyType type;
    private boolean fixedRotation, bullet;
    private Vector2 position, size;
    private float linearDamping, angle;
    private float density, restitution, friction;
    private short groupIndex;
    private Body body;

    public PhysicObject(){
        type = BodyDef.BodyType.DynamicBody;
        fixedRotation = false;
        bullet = false;
        position = new Vector2(0, 0);
        size = new Vector2(1, 1);
        linearDamping = 0;
        angle = 0;
        density = 1;
        restitution = 0.3f;
        friction = 0.5f;
        groupIndex = 0;
    }

    public void createObject(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.bullet = bullet;
        bodyDef.linearDamping = linearDamping;
        bodyDef.angle = angle;
        bodyDef.position.set(position.x, position.y);

        body = MyPhysicManager.getInstance().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x, size.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;
        fixtureDef.filter.groupIndex = groupIndex;

        body.createFixture(fixtureDef).setUserData(2);

        shape.dispose();
    }

    public Body getBody(){
        return body;
    }

    public void setTypeDynamicBody() {
        this.type = BodyDef.BodyType.DynamicBody;
    }

    public void setTypeStaticBody() {
        this.type = BodyDef.BodyType.StaticBody;
    }

    public void setTypeKinematicBody() {
        this.type = BodyDef.BodyType.KinematicBody;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public void setBullet(boolean bullet) {
        this.bullet = bullet;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setGroupIndex(short groupIndex) {
        this.groupIndex = groupIndex;
    }
}
