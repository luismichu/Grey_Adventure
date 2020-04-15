package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;

public class MyWindowListener implements Lwjgl3WindowListener{
    private static MyWindowListener windowListener;

    public static void instantiate(){
        if(windowListener == null)
            windowListener = new MyWindowListener();
    }

    private MyWindowListener(){
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        window.setWindowListener(this);
    }

    @Override
    public void created(Lwjgl3Window window) {

    }

    @Override
    public void iconified(boolean isIconified) {

    }

    @Override
    public void maximized(boolean isMaximized) {
        System.out.println(isMaximized);
    }

    @Override
    public void focusLost() {

    }

    @Override
    public void focusGained() {

    }

    @Override
    public boolean closeRequested() {
        System.out.println("closing...");
        Gdx.app.exit();
        return false;
    }

    @Override
    public void filesDropped(String[] files) {

    }

    @Override
    public void refreshRequested() {

    }
}
