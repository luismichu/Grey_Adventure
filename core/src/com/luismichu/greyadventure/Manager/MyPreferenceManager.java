package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Application;

import java.awt.*;

/**
 * Wraps LibGDX Preferences class
 */
public class MyPreferenceManager {
    Preferences prefs;

    /**
     * Creates a new instance of LibGDX Preferences from {@link Application#getPreferences(String name)}.
     */
    public MyPreferenceManager(){
        prefs = Gdx.app.getPreferences(Preference.PREFERENCE);
    }

    /**
     * @return true if music is on, else false.
     */
    public boolean isMusicOn(){
        return prefs.getBoolean(Preference.MUSIC, true);
    }

    /**
     * @param state true to set music on, else false.
     */
    public void setMusic(boolean state){
        prefs.putBoolean(Preference.MUSIC, state);
    }

    /**
     * @return volume percentage from 0 to 100.
     */
    public int getVolume(){
        return (int)(prefs.getFloat(Preference.VOLUME, 1) * 100);
    }

    /**
     * Sets the volume for all sounds and music.
     * @param percentage from 0 to 100
     */
    public void setVolume(int percentage){
        prefs.putFloat(Preference.VOLUME, percentage / 100f);
    }

    /**TODO update
     * @return Vector2 with resolution width and height.
     */
    public int[] getResolution(){
        return new int[]{prefs.getInteger(Preference.RESOLUTION_WIDTH, Preference.DEFAULT_WIDTH),
                prefs.getInteger(Preference.RESOLUTION_HEIGHT, Preference.DEFAULT_HEIGHT)};
    }

    /**TODO update
     * @param resolution Vector2 with resolution width and height.
     */
    public void setResolution(Integer[] resolution){
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

    //TODO doc
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

    /**
     * Hardcoded Strings with names of preferences.
     */
    private static class Preference{
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
