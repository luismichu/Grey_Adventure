package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class MyAssetManager {
    private AssetManager manager;
    private Array<MyAssetDescriptor> queue;

    public MyAssetManager(){
        manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        AssetDescriptors.initialize();
        queue = new Array<>();
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
        for(MyAssetDescriptor texture : AssetDescriptors.greyStanding)
            manager.load(texture);

        for(MyAssetDescriptor texture : AssetDescriptors.greyRunningL)
            manager.load(texture);

        for(MyAssetDescriptor texture : AssetDescriptors.greyRunningR)
            manager.load(texture);
    }

    public void loadDialog(){
        manager.load(AssetDescriptors.skin);
        manager.load(AssetDescriptors.textSound);

        for(MyAssetDescriptor texture : AssetDescriptors.dialogueBox)
            manager.load(texture);

        manager.finishLoading();
    }

    public void addToQueue(MyAssetDescriptor assetDescriptor){
        queue.add(assetDescriptor);
    }

    public void addToQueue(Array<MyAssetDescriptor> assetDescriptors){
        for(MyAssetDescriptor assetDescriptor : assetDescriptors)
            queue.add(assetDescriptor);
    }

    public void loadQueue(){
        for(MyAssetDescriptor assetDescriptor : queue)
            manager.load(assetDescriptor);

        manager.finishLoading();
        queue.clear();
    }

    public float getProgress(){
        return manager.getProgress() * 100;
    }

    public boolean update(){
        return manager.update();
    }

    public Object get(MyAssetDescriptor t){ return manager.get(t); }

    public Texture getTexture(MyAssetDescriptor t){
        return (Texture) manager.get(t);
    }

    public Array<Texture> getTextures(Array<MyAssetDescriptor> arrayAssetDescriptorTexture){
        Array<Texture> arrayTexture = new Array<>();

        for(MyAssetDescriptor t : arrayAssetDescriptorTexture)
            arrayTexture.add((Texture) manager.get(t));
        return arrayTexture;
    }

    public Array<Music> getMusics(Array<MyAssetDescriptor> arrayAssetDescriptorMusic){
        Array<Music> arrayMusic = new Array<>();

        for(MyAssetDescriptor t : arrayAssetDescriptorMusic)
            arrayMusic.add((Music) manager.get(t));
        return arrayMusic;
    }

    public Sound getSound(MyAssetDescriptor assetDescriptorSound){
        return (Sound) manager.get(assetDescriptorSound);
    }

    public Skin getSkin(MyAssetDescriptor skin){
        return (Skin) manager.get(skin);
    }

    public static class MyAssetDescriptor extends AssetDescriptor{
        public MyAssetDescriptor(String fileName, Class c) {
            super(fileName, c);
        }
    }

    public static class AssetDescriptors {
        private static AssetDescriptors assetDescriptors;
        public static final Array<MyAssetDescriptor> greyStanding = new Array<>();
        public static final Array<MyAssetDescriptor> greyRunningL = new Array<>();
        public static final Array<MyAssetDescriptor> greyRunningR = new Array<>();
        public static final Array<MyAssetDescriptor> enemyRedAttack = new Array<>();
        public static final Array<MyAssetDescriptor> enemyGreenAttack = new Array<>();
        public static final Array<MyAssetDescriptor> enemyBlueAttack = new Array<>();
        public static final Array<MyAssetDescriptor> dialogueBox = new Array<>();
        public static final Array<MyAssetDescriptor> music = new Array<>();
        public static final MyAssetDescriptor textSound = new MyAssetDescriptor(Assets.TEXT_SOUND, Sound.class);
        public static final MyAssetDescriptor skin = new MyAssetDescriptor(Assets.SKIN, Skin.class);
        public static final MyAssetDescriptor mainMenuBackground = new MyAssetDescriptor(Assets.MAIN_MENU_BACKGROUND, Texture.class);
        public static final MyAssetDescriptor greyAdventureLogo = new MyAssetDescriptor(Assets.GREY_ADVENTURE_LOGO, Texture.class);
        public static final MyAssetDescriptor map1 = new MyAssetDescriptor(Assets.MAP1, TiledMap.class);
        public static final Array<String> dialogMap1 = new Array<>();

        private AssetDescriptors(){
            for (int i = 0; i < Assets.GREY_STANDING_NUM; i++)
                greyStanding.add(new MyAssetDescriptor(Assets.GREY_STANDING + i + Assets.GREY_STANDING_EXT, Texture.class));

            for (int i = 0; i < Assets.GREY_RUNNING_NUM; i++)
                greyRunningL.add(new MyAssetDescriptor(Assets.GREY_RUNNING_L + i + Assets.GREY_RUNNING_EXT, Texture.class));

            for (int i = 0; i < Assets.GREY_RUNNING_NUM; i++)
                greyRunningR.add(new MyAssetDescriptor(Assets.GREY_RUNNING_R + i + Assets.GREY_RUNNING_EXT, Texture.class));

            for (int i = 0; i < Assets.ENEMY_ATTACK_NUM; i++)
                enemyRedAttack.add(new MyAssetDescriptor(Assets.ENEMY_RED_ATTACK + i + Assets.ENEMY_ATTACK_EXT, Texture.class));

            for (int i = 0; i < Assets.ENEMY_ATTACK_NUM; i++)
                enemyGreenAttack.add(new MyAssetDescriptor(Assets.ENEMY_GREEN_ATTACK + i + Assets.ENEMY_ATTACK_EXT, Texture.class));

            for (int i = 0; i < Assets.ENEMY_ATTACK_NUM; i++)
                enemyBlueAttack.add(new MyAssetDescriptor(Assets.ENEMY_BLUE_ATTACK + i + Assets.ENEMY_ATTACK_EXT, Texture.class));

            for (int i = 0; i < Assets.DIALOGUE_BOX_NUM; i++)
                dialogueBox.add(new MyAssetDescriptor(Assets.DIALOGUE_BOX + i + Assets.DIALOGUE_BOX_EXT, Texture.class));

            dialogMap1.add("Hey, bienvenido viajero");
            dialogMap1.add("Como te va la vida");
            dialogMap1.add("Espero que bien");
            dialogMap1.add("A mi tambien. Gracias por preguntar!");
        }

        public static void initialize(){
            if(assetDescriptors == null)
                assetDescriptors = new AssetDescriptors();
        }

        private static class Assets{
            public static final String GREY_STANDING = "sprites/grey_";
            public static final String GREY_STANDING_EXT = ".png";
            public static final int GREY_STANDING_NUM = 3;

            public static final String GREY_RUNNING_L = "sprites/running/grey_running_l_";
            public static final int GREY_RUNNING_NUM = 6;
            public static final String GREY_RUNNING_EXT = ".png";
            public static final String GREY_RUNNING_R = "sprites/running/grey_running_r_";

            public static final String ENEMY_RED_ATTACK = "sprites/enemy/red/attack_";
            public static final String ENEMY_GREEN_ATTACK = "sprites/enemy/green/attack_";
            public static final String ENEMY_BLUE_ATTACK = "sprites/enemy/blue/attack_";
            public static final int ENEMY_ATTACK_NUM = 4;
            public static final String ENEMY_ATTACK_EXT = ".png";

            public static final String SKIN = "skins/craftacular/skin/craftacular-ui.json";

            public static final String MAIN_MENU_BACKGROUND = "maps/main_menu_background.png";
            public static final String GREY_ADVENTURE_LOGO = "sprites/grey_adventure_logo.png";

            public static final String MUSIC = "musica";
            public static final String TEXT_SOUND = "sounds/text3.wav";

            public static final String DIALOGUE_BOX = "sprites/dialoguebox_";
            public static final int DIALOGUE_BOX_NUM = 2;
            public static final String DIALOGUE_BOX_EXT = ".png";

            public static final String MAP1 = "maps/Mapa1.tmx";
            public static final String MAP2 = "maps/Mapa2.tmx";
            public static final String MAP3 = "maps/Mapa3.tmx";

            private Assets(){}
        }
    }
}
