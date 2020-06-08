package com.luismichu.greyadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.luismichu.greyadventure.Manager.GameState;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.MyDatabaseManager;
import com.luismichu.greyadventure.Manager.MyPreferenceManager;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class Play implements Screen {
    private GreyAdventure greyAdventure;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private MyAssetManager assetManager;
    private MyPreferenceManager preferenceManager;
    private Sound selectSound, deleteOptionSound;
    private Stage stage;
    private OrthographicCamera cameraUI;
    private FitViewport viewportUI;
    private boolean fading;
    private Skin skin;

    private float alpha;

    public Play(GreyAdventure greyAdventure){
        this.greyAdventure = greyAdventure;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.WHITE);

        assetManager = new MyAssetManager();
        assetManager.loadPlay();
        skin = assetManager.getSkin(MyAssetManager.AssetDescriptors.skin);
        selectSound = assetManager.getSound(MyAssetManager.AssetDescriptors.selectSound);
        deleteOptionSound = assetManager.getSound(MyAssetManager.AssetDescriptors.deleteOptionSound);

        preferenceManager = new MyPreferenceManager();

        cameraUI = new OrthographicCamera();
        cameraUI.setToOrtho(false);
        int[] resolution = preferenceManager.getResolution();
        viewportUI = new FitViewport(resolution[0], resolution[1], cameraUI);
        cameraUI.position.set(resolution[0] / 2f,resolution[1] / 2f,0);
        cameraUI.zoom = 1f;

        stage = new Stage(viewportUI, batch);
        Gdx.input.setInputProcessor(stage);
        createTable();

        alpha = 0;
        fading = true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        if(fading) {
            if (alpha < 1)
                alpha += 0.5 * delta;
        } else
            alpha -= 0.9 * delta;

        stage.act();

        batch.setColor(1, 1, 1, alpha);
        batch.setProjectionMatrix(cameraUI.combined);

        batch.begin();

        for(Actor t : stage.getActors())
            t.draw(batch, alpha);

        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(1, 1, 1, alpha);
        shapeRenderer.setProjectionMatrix(cameraUI.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for(Actor t : stage.getActors()) {
            if(t.getClass() != Label.class)
                shapeRenderer.rect(t.getX(), t.getY(), t.getWidth(), t.getHeight());
        }
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void createTable() {
        final GameState gameState1 = MyDatabaseManager.read(1);
        final GameState gameState2 = MyDatabaseManager.read(2);
        final GameState gameState3 = MyDatabaseManager.read(3);

        Table t1 = new Table();
        t1.setHeight(cameraUI.viewportHeight * 0.2f);
        t1.setWidth(cameraUI.viewportWidth * 0.8f);
        t1.setPosition(cameraUI.viewportWidth * 0.1f, cameraUI.viewportHeight * 0.65f);
        final Label player1 = new Label(gameState1 != null ? gameState1.name : "Vacio", skin);
        player1.setAlignment(Align.left);
        final Label level1 = new Label(gameState1 != null ? "Nivel " + gameState1.level : "", skin);
        level1.setAlignment(Align.bottomRight);
        t1.row();
        t1.add(player1).width(cameraUI.viewportWidth * 0.3f).height(cameraUI.viewportHeight * 0.2f);
        t1.add(level1).width(cameraUI.viewportWidth * 0.35f).height(cameraUI.viewportHeight * 0.1f).padLeft(cameraUI.viewportHeight * 0.05f).align(Align.bottom);
        final Label delete1 = new Label(gameState1 != null ? "Eliminar" : "", skin);
        delete1.setAlignment(Align.topRight);
        delete1.setWidth(cameraUI.viewportWidth * 0.35f);
        delete1.setHeight(cameraUI.viewportHeight * 0.1f);
        delete1.setPosition(cameraUI.viewportWidth * 0.49f, cameraUI.viewportHeight * 0.75f);

        Table t2 = new Table();
        t2.setHeight(cameraUI.viewportHeight * 0.2f);
        t2.setWidth(cameraUI.viewportWidth * 0.8f);
        t2.setPosition(cameraUI.viewportWidth * 0.1f, cameraUI.viewportHeight * 0.4f);
        final Label player2 = new Label(gameState2 != null ? gameState2.name : "Vacio", skin);
        player2.setAlignment(Align.left);
        player2.setWidth(cameraUI.viewportWidth * 0.2f);
        player2.setHeight(cameraUI.viewportHeight * 0.2f);
        final Label level2 = new Label(gameState2 != null ? "Nivel " + gameState2.level : "", skin);
        level2.setAlignment(Align.bottomRight);
        level2.setWidth(cameraUI.viewportWidth * 0.4f);
        t2.row();
        t2.add(player2).width(cameraUI.viewportWidth * 0.3f).height(cameraUI.viewportHeight * 0.2f);
        t2.add(level2).width(cameraUI.viewportWidth * 0.35f).height(cameraUI.viewportHeight * 0.1f).padLeft(cameraUI.viewportHeight * 0.05f).align(Align.bottom);
        final Label delete2 = new Label(gameState2 != null ? "Eliminar" : "", skin);
        delete2.setAlignment(Align.topRight);
        delete2.setWidth(cameraUI.viewportWidth * 0.35f);
        delete2.setHeight(cameraUI.viewportHeight * 0.1f);
        delete2.setPosition(cameraUI.viewportWidth * 0.49f, cameraUI.viewportHeight * 0.5f);

        Table t3 = new Table();
        t3.setHeight(cameraUI.viewportHeight * 0.2f);
        t3.setWidth(cameraUI.viewportWidth * 0.8f);
        t3.setPosition(cameraUI.viewportWidth * 0.1f, cameraUI.viewportHeight * 0.15f);
        final Label player3 = new Label(gameState3 != null ? gameState3.name : "Vacio", skin);
        player3.setAlignment(Align.left);
        player3.setWidth(cameraUI.viewportWidth * 0.2f);
        player3.setHeight(cameraUI.viewportHeight * 0.2f);
        final Label level3 = new Label(gameState3 != null ? "Nivel " + gameState3.level : "", skin);
        level3.setAlignment(Align.bottomRight);
        level3.setWidth(cameraUI.viewportWidth * 0.4f);
        t3.row();
        t3.add(player3).width(cameraUI.viewportWidth * 0.3f).height(cameraUI.viewportHeight * 0.2f);
        t3.add(level3).width(cameraUI.viewportWidth * 0.35f).height(cameraUI.viewportHeight * 0.1f).padLeft(cameraUI.viewportHeight * 0.05f).align(Align.bottom);
        final Label delete3 = new Label(gameState3 != null ? "Eliminar" : "", skin);
        delete3.setAlignment(Align.topRight);
        delete3.setWidth(cameraUI.viewportWidth * 0.35f);
        delete3.setHeight(cameraUI.viewportHeight * 0.1f);
        delete3.setPosition(cameraUI.viewportWidth * 0.49f, cameraUI.viewportHeight * 0.25f);

        player1.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(preferenceManager.isMusicOn())
                    selectSound.play(preferenceManager.getVolume() / 100f);
                if(gameState1 == null || gameState1.destroyed){
                    insertName(1);
                } else {
                    Gdx.input.setInputProcessor(null);
                    GreyAdventure.music.stop();
                    fading = false;
                    Timer.schedule(new Timer.Task() {
                                       @Override
                                       public void run() {
                                           greyAdventure.setScreen(new Game(greyAdventure, gameState1));
                                           dispose();
                                       }
                                   }, 1.2f / Game.WORLD_SPEED);

                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        player2.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(preferenceManager.isMusicOn())
                    selectSound.play(preferenceManager.getVolume() / 100f);
                if(gameState2 == null || gameState2.destroyed){
                    insertName(2);
                } else {
                    Gdx.input.setInputProcessor(null);
                    GreyAdventure.music.stop();
                    fading = false;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            greyAdventure.setScreen(new Game(greyAdventure, gameState2));
                            dispose();
                        }
                    }, 1.2f / Game.WORLD_SPEED);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        player3.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(preferenceManager.isMusicOn())
                    selectSound.play(preferenceManager.getVolume() / 100f);
                if(gameState3 == null || gameState3.destroyed){
                    insertName(3);
                } else {
                    Gdx.input.setInputProcessor(null);
                    GreyAdventure.music.stop();
                    fading = false;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            greyAdventure.setScreen(new Game(greyAdventure, gameState3));
                            dispose();
                        }
                    }, 1.2f / Game.WORLD_SPEED);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        delete1.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(MyDatabaseManager.read(1) != null) {
                    if (preferenceManager.isMusicOn())
                        deleteOptionSound.play(preferenceManager.getVolume() / 100f);
                    gameState1.destroyed = true;
                    MyDatabaseManager.delete(1);
                    player1.setText("Vacio");
                    level1.setText("");
                    delete1.setText("");
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });

        delete2.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(MyDatabaseManager.read(2) != null) {
                    if (preferenceManager.isMusicOn())
                        deleteOptionSound.play(preferenceManager.getVolume() / 100f);
                    gameState2.destroyed = true;
                    MyDatabaseManager.delete(2);
                    player2.setText("Vacio");
                    level2.setText("");
                    delete2.setText("");
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });

        delete3.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(MyDatabaseManager.read(3) != null) {
                    if (preferenceManager.isMusicOn())
                        deleteOptionSound.play(preferenceManager.getVolume() / 100f);
                    gameState3.destroyed = true;
                    MyDatabaseManager.delete(3);
                    player3.setText("Vacio");
                    level3.setText("");
                    delete3.setText("");
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });

        stage.addActor(t1);
        stage.addActor(t2);
        stage.addActor(t3);
        stage.addActor(delete1);
        stage.addActor(delete2);
        stage.addActor(delete3);
    }

    private void insertName(final int position){
        stage.clear();

        final TextField txtField = new TextField("", skin);
        txtField.setBounds(cameraUI.viewportWidth * 0.15f, cameraUI.viewportHeight * 0.65f, cameraUI.viewportWidth * 0.7f, cameraUI.viewportWidth * 0.05f);

        Label lbl = new Label("Introduce tu nombre de heroe", skin);
        lbl.setAlignment(Align.center);
        lbl.setBounds(cameraUI.viewportWidth * 0.15f, cameraUI.viewportHeight * 0.75f, cameraUI.viewportWidth * 0.7f, cameraUI.viewportWidth * 0.05f);

        final Label lblError = new Label("Debe tener entre 4 y 20 caracteres", skin);
        lblError.setAlignment(Align.center);
        lblError.setFontScale(0.75f);
        lblError.setBounds(cameraUI.viewportWidth * 0.15f, cameraUI.viewportHeight * 0.5f, cameraUI.viewportWidth * 0.7f, cameraUI.viewportWidth * 0.05f);

        TextButton btConfirmar = new TextButton("Confirmar", skin);
        btConfirmar.setBounds(cameraUI.viewportWidth * 0.4f, cameraUI.viewportHeight * 0.1f, cameraUI.viewportWidth * 0.2f, cameraUI.viewportWidth * 0.05f);

        TextButton btCancelar = new TextButton("Cancelar", skin);
        btCancelar.getLabel().setFontScale(0.7f);
        btCancelar.setBounds(cameraUI.viewportWidth * 0.05f, cameraUI.viewportHeight * 0.06f, cameraUI.viewportWidth * 0.1f, cameraUI.viewportWidth * 0.04f);

        stage.addActor(txtField);
        stage.addActor(lbl);
        stage.addActor(btConfirmar);
        stage.addActor(btCancelar);
        stage.setKeyboardFocus(txtField);

        btConfirmar.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(preferenceManager.isMusicOn())
                    selectSound.play(preferenceManager.getVolume() / 100f);
                String name = txtField.getText();
                if(name == null || name.trim().length() < 4 || name.trim().length() > 20){
                    if(!stage.getActors().contains(lblError, false))
                        stage.addActor(lblError);
                } else {
                    GreyAdventure.music.stop();
                    Gdx.input.setInputProcessor(null);
                    final GameState gameState = new GameState(position, name, 0);
                    MyDatabaseManager.insert(gameState);
                    fading = false;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            greyAdventure.setScreen(new Game(greyAdventure, gameState));
                            dispose();
                        }
                    }, 1.2f / Game.WORLD_SPEED);
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });

        btCancelar.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(preferenceManager.isMusicOn())
                    selectSound.play(preferenceManager.getVolume() / 100f);
                stage.clear();
                createTable();

                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
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
        batch.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
    }
}