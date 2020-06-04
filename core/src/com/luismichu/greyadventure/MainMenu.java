package com.luismichu.greyadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.MyPreferenceManager;
import com.luismichu.greyadventure.Manager.MyShaderLoader;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class MainMenu implements Screen {
    private GreyAdventure greyAdventure;
    private MyAssetManager assetManager;
    private MyPreferenceManager preferenceManager;
    private SpriteBatch batch;
    private FrameBuffer tableBuffer1, tableBuffer2;
    private OrthographicCamera camera, cameraUI;
    private FitViewport viewport, viewportUI;
    private Stage stage;
    private Table optionsTable, menuTable;
    private Skin skin;
    private boolean fading, movingMenu, movingOptions, starting = true;

    private final int TILE_SIZE = 32;
    private float alpha, y;

    private Texture background, logo;
    private Sprite menuTableSprite, optionsTableSprite;

    private float elapsedTime;

    public MainMenu(GreyAdventure greyAdventure, boolean starting){
        this.greyAdventure = greyAdventure;
        this.starting = starting;
    }

    @Override
    public void show() {
        assetManager = new MyAssetManager();
        assetManager.loadMainMenu();

        preferenceManager = new MyPreferenceManager();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        int totalMapWidth = TILE_SIZE * 16;
        int totalMapHeight = TILE_SIZE * 9;
        viewport = new FitViewport(totalMapWidth, totalMapHeight, camera);
        camera.position.set(totalMapWidth / 2f,totalMapHeight / 2f,0);
        camera.zoom = 1f;

        cameraUI = new OrthographicCamera();
        cameraUI.setToOrtho(false);
        int[] resolution = preferenceManager.getResolution();
        viewportUI = new FitViewport(resolution[0], resolution[1], cameraUI);
        cameraUI.position.set(resolution[0] / 2f,resolution[1] / 2f,0);
        cameraUI.zoom = 1f;

        tableBuffer1 = new FrameBuffer(Pixmap.Format.RGBA8888, resolution[0], resolution[1], true);
        tableBuffer2 = new FrameBuffer(Pixmap.Format.RGBA8888, resolution[0], resolution[1], true);

        stage = new Stage(viewportUI, batch);
        Gdx.input.setInputProcessor(stage);
        skin = assetManager.getSkin(MyAssetManager.AssetDescriptors.skin);

        background = assetManager.getTexture(MyAssetManager.AssetDescriptors.mainMenuBackground);
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logo = assetManager.getTexture(MyAssetManager.AssetDescriptors.greyAdventureLogo);
        logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        fading = false;
        movingMenu = false;
        movingOptions = false;

        elapsedTime = 0;
        alpha = 1;
        y = 0;

        createMenuTable();
        createOptionsTable();

        menu();
    }

    @Override
    public void render(float delta) {
        delta /= 1; //TODO
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        if(starting){
            elapsedTime += delta;
            batch.setColor(1, 1, 1, alpha);
            batch.begin();
            batch.setProjectionMatrix(cameraUI.combined);
            batch.draw(logo, cameraUI.viewportWidth / 2f - logo.getWidth() / 2f, cameraUI.viewportHeight / 2f - logo.getHeight() / 2f);
            batch.end();
            if(elapsedTime > 3) {
                alpha -= 1.5f * delta;
                if(alpha <= -0.5f) {
                    alpha = 1;
                    elapsedTime = 0;
                    starting = false;
                    preferenceManager.setFullScreen(preferenceManager.isFullScreen());
                }
            }
        }
        else {
            elapsedTime += 20 * delta;
            if (elapsedTime > background.getWidth())
                elapsedTime = 0;

            stage.act();

            if (fading) {
                alpha -= 1.2f * delta;
                y += 60f * delta;
                menuTableSprite.setPosition(menuTableSprite.getX(), menuTableSprite.getY() - y);

                batch.setColor(1, 1, 1, alpha);
                batch.begin();
                batch.setProjectionMatrix(camera.combined);
                batch.draw(background, -elapsedTime, 0);
                batch.draw(background, background.getWidth() - elapsedTime, 0);
                batch.setProjectionMatrix(cameraUI.combined);
                menuTableSprite.draw(batch);
                batch.end();

                if(alpha <= -1) {
                    greyAdventure.setScreen(new Play(greyAdventure));
                    dispose();
                }
            }
            else {
                batch.setProjectionMatrix(camera.combined);
                batch.setColor(1, 1, 1, alpha);
                batch.begin();
                batch.draw(background, -elapsedTime, 0);
                batch.draw(background, background.getWidth() - elapsedTime, 0);
                batch.end();

                if (movingMenu || movingOptions) {
                    if (movingMenu) {
                        menuTableSprite.setPosition(menuTableSprite.getX() - 2400 * delta, menuTableSprite.getY());
                        optionsTableSprite.setPosition(optionsTableSprite.getX() - 2400 * delta, optionsTableSprite.getY());
                        alpha -= 1.2f * delta;
                        if (optionsTableSprite.getX() <= 10)
                            movingMenu = false;
                    } else {
                        menuTableSprite.setPosition(menuTableSprite.getX() + 2400 * delta, menuTableSprite.getY());
                        optionsTableSprite.setPosition(optionsTableSprite.getX() + 2400 * delta, optionsTableSprite.getY());
                        alpha += 1.2f * delta;
                        if (menuTableSprite.getX() >= -10)
                            movingOptions = false;
                    }

                    batch.setProjectionMatrix(cameraUI.combined);
                    batch.begin();
                    menuTableSprite.draw(batch);
                    optionsTableSprite.draw(batch);
                    batch.end();
                } else
                    stage.draw();
            }
        }
    }

    private void createMenuTable(){
        menuTable = new Table();
        menuTable.setWidth(stage.getWidth());
        menuTable.align(Align.center | Align.top);

        menuTable.setPosition(0, preferenceManager.getResolution()[1]);

        TextButton btStart = new TextButton("Nueva partida",skin);
        btStart.getLabel().setFontScale(0.9f);

        TextButton btOpciones = new TextButton("Opciones",skin);
        btOpciones.getLabel().setFontScale(0.9f);

        TextButton btSalir = new TextButton("Salir",skin);
        btSalir.getLabel().setFontScale(0.9f);

        Label lbl = new Label("Grey Adventure", skin);
        Label lbl2 = new Label("Grey Adventure", skin);

        lbl.setFontScale(2.6f);
        lbl2.setFontScale(2.5f);
        lbl.setColor(Color.BLACK);
        menuTable.padTop(70);

        Stack stack = new Stack();
        stack.add(lbl);
        stack.add(lbl2);
        menuTable.add(stack).padBottom(100);

        menuTable.row();
        menuTable.add(btStart).width(550).height(70).padBottom(30);

        menuTable.row();
        menuTable.add(btOpciones).width(550).height(70).padBottom(30);

        menuTable.row();
        menuTable.add(btSalir).width(550).height(70).padBottom(30);

        btStart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fading = true;

                SpriteBatch spriteBatch = new SpriteBatch();
                tableBuffer1.begin();
                spriteBatch.setProjectionMatrix(cameraUI.combined);
                spriteBatch.begin();
                menuTable.draw(spriteBatch, 1);
                spriteBatch.end();
                tableBuffer1.end();

                menuTableSprite = new Sprite(tableBuffer1.getColorBufferTexture());
                menuTableSprite.flip(false, true);
            }
        });

        btSalir.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        btOpciones.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!fading) {
                    SpriteBatch spriteBatch = new SpriteBatch();
                    tableBuffer1.begin();
                    spriteBatch.setProjectionMatrix(cameraUI.combined);
                    spriteBatch.begin();
                    menuTable.draw(spriteBatch, 1);
                    spriteBatch.end();
                    tableBuffer1.end();

                    menuTableSprite = new Sprite(tableBuffer1.getColorBufferTexture());
                    menuTableSprite.flip(false, true);
                    menuTableSprite.setPosition(80, menuTableSprite.getY());

                    tableBuffer2.begin();
                    spriteBatch.begin();
                    optionsTable.draw(spriteBatch, 1);
                    spriteBatch.end();
                    tableBuffer2.end();

                    optionsTableSprite = new Sprite(tableBuffer2.getColorBufferTexture());
                    optionsTableSprite.flip(false, true);
                    optionsTableSprite.setPosition(1200, optionsTableSprite.getY());

                    movingMenu = true;
                    options();

                    spriteBatch.dispose();
                }
            }
        });
    }

    private void createOptionsTable(){
        optionsTable = new Table();
        optionsTable.setWidth(stage.getWidth());
        optionsTable.align(Align.center | Align.top);

        optionsTable.setPosition(0, preferenceManager.getResolution()[1]);

        final TextButton btMusic = new TextButton("Musica: " + (preferenceManager.isMusicOn() ? "ON" : "OFF"),skin);
        btMusic.getLabel().setFontScale(0.85f);

        final Slider sliVolume = new Slider(0, 100, 1, false, skin);
        sliVolume.setValue(preferenceManager.getVolume());
        sliVolume.setColor(preferenceManager.isMusicOn() ? Color.WHITE : Color.GRAY);
        sliVolume.setDisabled(!preferenceManager.isMusicOn());

        final Label lblVolume = new Label("Volumen: " + preferenceManager.getVolume() + "%", skin);
        lblVolume.setFontScale(0.85f);
        lblVolume.setWidth(500);
        lblVolume.setAlignment(Align.center);
        lblVolume.setColor(preferenceManager.isMusicOn() ? Color.WHITE : Color.GRAY);
        lblVolume.setTouchable(null);

        final TextButton btFullScreen = new TextButton("Pantalla completa: " + (preferenceManager.isFullScreen() ? "ON" : "OFF") ,skin);
        btFullScreen.getLabel().setFontScale(0.85f);

        final TextButton btVSync = new TextButton("Sincronizacion vertical: " + (preferenceManager.isVSync() ? "ON" : "OFF") ,skin);
        btVSync.getLabel().setFontScale(0.85f);

        final TextButton btTextSpeed = new TextButton("",skin);
        String textSpeedText = "Velocidad del texto: ";
        if(preferenceManager.getTextSpeed() == MyPreferenceManager.Preference.SLOW)
            textSpeedText += "Lento";
        else if(preferenceManager.getTextSpeed() == MyPreferenceManager.Preference.MEDIUM)
            textSpeedText += "Medio";
        else if(preferenceManager.getTextSpeed() == MyPreferenceManager.Preference.FAST)
            textSpeedText += "Rapido";
        btTextSpeed.setText(textSpeedText);
        btTextSpeed.getLabel().setFontScale(0.85f);

        TextButton btVolver = new TextButton("Volver",skin);
        btVolver.getLabel().setFontScale(0.85f);

        Label lbl = new Label("Opciones", skin);

        lbl.setFontScale(1f);
        optionsTable.padTop(60);

        optionsTable.add(lbl).colspan(2).padBottom(80);

        optionsTable.row();
        optionsTable.add(btMusic).width(500).height(70).padBottom(30).padRight(15);
        Stack stack = new Stack();
        stack.add(sliVolume);
        stack.add(lblVolume);
        optionsTable.add(stack).width(500).height(70).padBottom(30);

        optionsTable.row();
        optionsTable.add(btFullScreen).width(500).height(70).padBottom(30).padRight(15);
        optionsTable.add(btVSync).width(500).height(70).padBottom(30);

        optionsTable.row();
        optionsTable.add(btTextSpeed).width(500).height(70).padBottom(30).padRight(15);

        optionsTable.row();
        optionsTable.add(btVolver).width(500).height(70).padTop(20).colspan(2);

        btMusic.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                preferenceManager.setMusic(!preferenceManager.isMusicOn());
                boolean state = preferenceManager.isMusicOn();
                btMusic.setText("Musica: " + (state ? "ON" : "OFF"));
                sliVolume.setDisabled(!state);
                if(state) {
                    lblVolume.setColor(Color.WHITE);
                    sliVolume.setColor(Color.WHITE);
                }
                else {
                    lblVolume.setColor(Color.GRAY);
                    sliVolume.setColor(Color.GRAY);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        sliVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                preferenceManager.setVolume((int)sliVolume.getValue());
                lblVolume.setText("Volumen: " + preferenceManager.getVolume() + "%");
            }
        });

        btFullScreen.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                preferenceManager.setFullScreen(!preferenceManager.isFullScreen());
                btFullScreen.setText("Pantalla completa: " + (preferenceManager.isFullScreen() ? "ON" : "OFF"));
                super.touchUp(event, x, y, pointer, button);
            }
        });

        btVSync.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                preferenceManager.setVSync(!preferenceManager.isVSync());
                btVSync.setText("Sincronizacion vertical: " + (preferenceManager.isVSync() ? "ON" : "OFF"));
                super.touchUp(event, x, y, pointer, button);
            }
        });

        btTextSpeed.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                String textSpeedText = "Velocidad del texto: ";
                if(preferenceManager.getTextSpeed() == MyPreferenceManager.Preference.SLOW) {
                    textSpeedText += "Medio";
                    preferenceManager.setTextSpeed(MyPreferenceManager.Preference.MEDIUM);
                }
                else if(preferenceManager.getTextSpeed() == MyPreferenceManager.Preference.MEDIUM) {
                    textSpeedText += "Rapido";
                    preferenceManager.setTextSpeed(MyPreferenceManager.Preference.FAST);
                }
                else if(preferenceManager.getTextSpeed() == MyPreferenceManager.Preference.FAST) {
                    textSpeedText += "Lento";
                    preferenceManager.setTextSpeed(MyPreferenceManager.Preference.SLOW);
                }
                btTextSpeed.setText(textSpeedText);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        btVolver.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                preferenceManager.flush();
                if(!fading) {
                    SpriteBatch spriteBatch = new SpriteBatch();
                    tableBuffer1.begin();
                    spriteBatch.setProjectionMatrix(cameraUI.combined);
                    spriteBatch.begin();
                    menuTable.draw(spriteBatch, 1);
                    spriteBatch.end();
                    tableBuffer1.end();

                    menuTableSprite = new Sprite(tableBuffer1.getColorBufferTexture());
                    menuTableSprite.flip(false, true);
                    menuTableSprite.setPosition(-1200, menuTableSprite.getY());

                    tableBuffer2.begin();
                    spriteBatch.begin();
                    optionsTable.draw(spriteBatch, 1);
                    spriteBatch.end();
                    tableBuffer2.end();

                    optionsTableSprite = new Sprite(tableBuffer2.getColorBufferTexture());
                    optionsTableSprite.flip(false, true);
                    optionsTableSprite.setPosition(-80, optionsTableSprite.getY());

                    movingOptions = true;
                    menu();

                    spriteBatch.dispose();

                }

                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private void menu(){
        stage.clear();
        stage.addActor(menuTable);
        stage.addListener(new InputListener());
    }

    private void options(){
        stage.clear();
        stage.addActor(optionsTable);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewportUI.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        skin.dispose();
        tableBuffer1.dispose();
        tableBuffer2.dispose();
    }
}
