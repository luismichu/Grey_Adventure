package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class MyAssetManager {
    private AssetManager manager;

    public MyAssetManager(){
        manager = new AssetManager();
        AssetDescriptors.initialize();
    }

    public void loadMainMenu(){
        manager.load(AssetDescriptors.skin);
        manager.load(AssetDescriptors.mainMenuBackground);
        manager.load(AssetDescriptors.greyAdventureLogo);
        manager.finishLoading();
    }

    public void loadPlay(){
        manager.load(AssetDescriptors.skin);
        manager.finishLoading();
    }

    public void loadGame(){
        for(AssetDescriptor<Texture> texture : AssetDescriptors.greyStanding)
            manager.load(texture);

        for(AssetDescriptor<Texture> texture : AssetDescriptors.greyRunningL)
            manager.load(texture);

        for(AssetDescriptor<Texture> texture : AssetDescriptors.greyRunningR)
            manager.load(texture);
    }

    public void loadAssets(){
        for(AssetDescriptor<Texture> texture : AssetDescriptors.greyStanding)
            manager.load(texture);

        for(AssetDescriptor<Texture> texture : AssetDescriptors.greyRunningL)
            manager.load(texture);

        for(AssetDescriptor<Texture> texture : AssetDescriptors.greyRunningR)
            manager.load(texture);

        for(AssetDescriptor<Music> music : AssetDescriptors.music)
            manager.load(music);
    }

    public float getProgress(){
        return manager.getProgress() * 100;
    }

    public boolean update(){
        return manager.update();
    }

    public Texture getTexture(AssetDescriptor<Texture> t){
        return manager.get(t);
    }

    public Array<Texture> getTextures(Array<AssetDescriptor<Texture>> arrayAssetDescriptorTexture){
        Array<Texture> arrayTexture = new Array<>();

        for(AssetDescriptor<Texture> t : arrayAssetDescriptorTexture)
            arrayTexture.add(manager.get(t));
        return arrayTexture;
    }

    public Array<Music> getMusics(Array<AssetDescriptor<Music>> arrayAssetDescriptorMusic){
        Array<Music> arrayMusic = new Array<>();

        for(AssetDescriptor<Music> t : arrayAssetDescriptorMusic)
            arrayMusic.add(manager.get(t));
        return arrayMusic;
    }

    public Skin getSkin(AssetDescriptor<Skin> skin){
        return manager.get(skin);
    }

    public static class AssetDescriptors {
        public static final Array<AssetDescriptor<Texture>> greyStanding = new Array<>();
        public static final Array<AssetDescriptor<Texture>> greyRunningL = new Array<>();
        public static final Array<AssetDescriptor<Texture>> greyRunningR = new Array<>();
        public static final Array<AssetDescriptor<Music>> music = new Array<>();
        public static final AssetDescriptor<Skin> skin = new AssetDescriptor<>(Assets.SKIN, Skin.class);
        public static final AssetDescriptor<Texture> mainMenuBackground = new AssetDescriptor<>(Assets.MAIN_MENU_BACKGROUND, Texture.class);
        public static final AssetDescriptor<Texture> greyAdventureLogo = new AssetDescriptor<>(Assets.GREY_ADVENTURE_LOGO, Texture.class);

        private AssetDescriptors(){}

        public static void initialize(){
            for (int i = 0; i < Assets.GREY_STANDING_NUM; i++)
                greyStanding.add(new AssetDescriptor<>(Assets.GREY_STANDING + i + Assets.GREY_STANDING_EXT, Texture.class));

            for (int i = 0; i < Assets.GREY_RUNNING_NUM; i++)
                greyRunningL.add(new AssetDescriptor<>(Assets.GREY_RUNNING_L + i + Assets.GREY_RUNNING_EXT, Texture.class));

            for (int i = 0; i < Assets.GREY_RUNNING_NUM; i++)
                greyRunningR.add(new AssetDescriptor<>(Assets.GREY_RUNNING_R + i + Assets.GREY_RUNNING_EXT, Texture.class));
        }

        private static class Assets{
            public static final String GREY_STANDING = "sprites/grey_";
            public static final String GREY_STANDING_EXT = ".png";
            public static final int GREY_STANDING_NUM = 3;

            public static final String GREY_RUNNING_L = "sprites/running/grey_running_l_";
            public static final int GREY_RUNNING_NUM = 6;
            public static final String GREY_RUNNING_EXT = ".png";
            public static final String GREY_RUNNING_R = "sprites/running/grey_running_r_";

            public static final String SKIN = "skins/craftacular/skin/craftacular-ui.json";

            public static final String MAIN_MENU_BACKGROUND = "maps/main_menu_background.png";
            public static final String GREY_ADVENTURE_LOGO = "sprites/grey_adventure_logo.png";

            public static final String MUSIC = "musica";

            private Assets(){}
        }
    }
}
