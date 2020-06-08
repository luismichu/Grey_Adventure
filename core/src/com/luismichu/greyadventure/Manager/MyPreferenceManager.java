package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;

import java.awt.*;

public class MyPreferenceManager {
    Preferences prefs;

    public MyPreferenceManager(){
        prefs = Gdx.app.getPreferences(Preference.PREFERENCE);
    }

    public boolean isMusicOn(){
        return prefs.getBoolean(Preference.MUSIC, true);
    }

    public void setMusic(boolean state){
        prefs.putBoolean(Preference.MUSIC, state);
    }

    public int getVolume(){
        return (int)(prefs.getFloat(Preference.VOLUME, 1) * 100);
    }

    public void setVolume(int percentage){
        prefs.putFloat(Preference.VOLUME, percentage / 100f);
    }

    public int[] getResolution(){
        return new int[]{prefs.getInteger(Preference.RESOLUTION_WIDTH, Preference.DEFAULT_WIDTH),
                prefs.getInteger(Preference.RESOLUTION_HEIGHT, Preference.DEFAULT_HEIGHT)};
    }

    public void setResolution(int[] resolution){
        prefs.putInteger(Preference.RESOLUTION_WIDTH, resolution[0]);
        prefs.putInteger(Preference.RESOLUTION_HEIGHT, resolution[1]);
    }

    public void setFullScreen(boolean state){
        prefs.putBoolean(Preference.FULLSCREEN, state);

        if(state) {
            Graphics.Monitor monitor = Gdx.graphics.getMonitor();
            Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(monitor);
            Gdx.graphics.setFullscreenMode(displayMode);
            setVSync(isVSync());
        }
        else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Gdx.graphics.setWindowedMode((int)(screenSize.width * 0.7), (int)(screenSize.height * 0.7));
        }
    }

    public boolean isFullScreen(){
        return prefs.getBoolean(Preference.FULLSCREEN, true);
    }

    public void setVSync(boolean state){
        Gdx.graphics.setVSync(state);
        prefs.putBoolean(Preference.VSYNC, state);
    }

    public boolean isVSync(){
        return prefs.getBoolean(Preference.VSYNC, true);
    }

    public void setTextSpeed(float textSpeed){
        prefs.putFloat(Preference.TEXT_SPEED, textSpeed);
    }

    public float getTextSpeed(){
        return prefs.getFloat(Preference.TEXT_SPEED, Preference.MEDIUM);
    }

    public void flush(){
        prefs.flush();
    }

    public static class Preference{
        public static final String PREFERENCE = "myPrefs";
        public static final String MUSIC = "music";
        public static final String VOLUME = "volume";
        public static final String RESOLUTION_WIDTH = "res_w";
        public static final int DEFAULT_WIDTH = 1280;
        public static final String RESOLUTION_HEIGHT = "res_h";
        public static final int DEFAULT_HEIGHT = 720;
        public static final String FULLSCREEN = "fullscreen";
        public static final String VSYNC = "vsync";
        public static final String TEXT_SPEED = "text_speed";
        public static final float FAST = 0.1f;
        public static final float MEDIUM = 0.15f;
        public static final float SLOW = 0.2f;
    }
}