package com.luismichu.greyadventure.Manager.Physic;

import com.badlogic.gdx.math.Vector2;

public class Physic{
    public static Vector2 gravity = new Vector2(0, -98f);
    public static final float TIME_STEP = 1/60f;
    public static final int VELOCITY_ITERATIONS = 10, POSITION_ITERATIONS = 10;

    public static final short DATA_FOOT = 3;

    public static final String OBJECT_LAYER = "O1";

    private Physic(){}
}