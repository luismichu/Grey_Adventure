package com.luismichu.greyadventure.Manager.Physic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;

public class PhysicObject {
    private BodyDef.BodyType type;
    private boolean fixedRotation, bullet;
    private Vector2 position, size;
    private float linearDamping, angle;
    private float density, restitution, friction;
    private short groupIndex;
    private short userData;
    protected Body body;

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
        userData = 0;
    }

    public void createObject(boolean sensor){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.bullet = bullet;
        bodyDef.linearDamping = linearDamping;
        bodyDef.angle = angle;
        bodyDef.position.set(position.x, position.y);

        body = MyPhysicManager.getInstance().createBody(bodyDef);

        createFixture(null, sensor);
    }

    public void createFixture(Vector2 pos, boolean sensor){
        PolygonShape shape = new PolygonShape();
        if(pos == null)
            shape.setAsBox(size.x, size.y);
        else
            shape.setAsBox(size.x, size.y, pos, 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = sensor;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;
        fixtureDef.filter.groupIndex = groupIndex;

        body.createFixture(fixtureDef).setUserData(userData);

        shape.dispose();
    }

    public void createCircleFixture(Vector2 pos, boolean sensor){
        CircleShape shape = new CircleShape();
        shape.setRadius(size.x);
        if(pos != null)
            shape.setPosition(pos);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = sensor;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;
        fixtureDef.filter.groupIndex = groupIndex;

        body.createFixture(fixtureDef).setUserData(userData);

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

    public void setUserData(short userData) {
        this.userData = userData;
    }

    public void setStatic(){ body.setType(BodyDef.BodyType.StaticBody); }

    public void setDynamic(){ body.setType(BodyDef.BodyType.DynamicBody); }
}
