package com.luismichu.greyadventure.Manager.Physic;

import com.badlogic.gdx.math.Vector2;

public class Physic{
    public static final int P2M = 32;
    public static Vector2 gravity = new Vector2(0, -9.8f);
    public static final float TIME_STEP = 1/60f;
    public static final int VELOCITY_ITERATIONS = 10, POSITION_ITERATIONS = 10;

    public static final short DATA_GROUND = 1;
    public static final short DATA_GREY = 1 << 2;
    public static final short DATA_FOOT = 1 << 3;
    public static final short DATA_DEATH = 1 << 4;
    public static final short DATA_ENEMY = 1 << 5;

    public static final String OBJECT_LAYER = "O1";
    public static final String DEATH_LAYER = "death";

    private Physic(){}
}