package com.luismichu.greyadventure.Manager.Physic;

public class Data {
    public short data;
    public String dialog;
    public int enemy;

    public Data(short data) {
        this.data = data;
    }

    public Data(short data, int enemy) {
        this.data = data;
        this.enemy = enemy;
    }

    public Data(short data, String dialog) {
        this.data = data;
        this.dialog = dialog;
    }
}