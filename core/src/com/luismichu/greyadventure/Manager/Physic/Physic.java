package com.luismichu.greyadventure.Manager.Physic;

import com.badlogic.gdx.math.Vector2;

public class Physic{
    public static final int P2M = 32;
    public static Vector2 gravity = new Vector2(0, -9.8f);
    public static final float TIME_STEP = 1f;
    public static final int VELOCITY_ITERATIONS = 10, POSITION_ITERATIONS = 10;

    public static final short DATA_GROUND = 1;
    public static final short DATA_GREY = 1 << 2;
    public static final short DATA_FOOT = 1 << 3;
    public static final short DATA_DEATH = 1 << 4;
    public static final short DATA_HIDDEN = 1 << 5;
    public static final short DATA_ENEMY = 1 << 6;
    public static final short DATA_END = 1 << 7;
    public static final short DATA_DIALOG = 1 << 8;

    public static final short CATEGORY_GROUND = 1;
    public static final short CATEGORY_PLAYER = 1 << 2;
    public static final short CATEGORY_HIDDEN = 1 << 5;

    public static final short MASK_GROUND = CATEGORY_PLAYER | CATEGORY_HIDDEN;
    public static final short MASK_PLAYER = CATEGORY_GROUND | CATEGORY_HIDDEN;
    public static final short MASK_HIDDEN = CATEGORY_GROUND | CATEGORY_PLAYER;

    public static final short GROUP_GROUND = 1;
    public static final short GROUP_PLAYER = 2;
    public static final short GROUP_HIDDEN = 3;

    public static final String OBJECT_LAYER = "O1";
    public static final String DEATH_LAYER = "death";
    public static final String HIDDEN_LAYER = "hidden";
    public static final String ENEMIES_LAYER = "enemies";
    public static final String DIALOGS_LAYER = "dialogs";
    public static final String END_LAYER = "end";

    private Physic(){}
}