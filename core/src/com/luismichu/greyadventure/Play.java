package com.luismichu.greyadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.luismichu.greyadventure.Manager.MyAssetManager;
import com.luismichu.greyadventure.Manager.MyPreferenceManager;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class Play implements Screen {
    private GreyAdventure greyAdventure;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private MyAssetManager assetManager;
    private MyPreferenceManager preferenceManager;

    private Stage stage;
    private OrthographicCamera cameraUI;
    private FitViewport viewportUI;

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
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        if(alpha < 1)
            alpha += 0.5 * delta;

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

        for(Actor t : stage.getActors())
            shapeRenderer.rect(t.getX(), t.getY(), t.getWidth(), t.getHeight());

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void createTable() {
        Table t1 = new Table();
        t1.setHeight(cameraUI.viewportHeight * 0.2f);
        t1.setWidth(cameraUI.viewportWidth * 0.8f);
        t1.setPosition(cameraUI.viewportWidth * 0.1f, cameraUI.viewportHeight * 0.65f);
        Label player1 = new Label("Luismichu", skin);
        player1.setAlignment(Align.left);
        player1.setWidth(cameraUI.viewportWidth * 0.2f);
        player1.setHeight(cameraUI.viewportHeight * 0.2f);
        Label level1 = new Label("Nivel 1", skin);
        level1.setAlignment(Align.bottomRight);
        level1.setWidth(cameraUI.viewportWidth * 0.4f);
        t1.row();
        t1.add(player1).width(cameraUI.viewportWidth * 0.3f).height(cameraUI.viewportHeight * 0.2f);
        t1.add(level1).width(cameraUI.viewportWidth * 0.35f).height(cameraUI.viewportHeight * 0.1f).padLeft(cameraUI.viewportHeight * 0.05f).align(Align.bottom);

        Table t2 = new Table();
        t2.setHeight(cameraUI.viewportHeight * 0.2f);
        t2.setWidth(cameraUI.viewportWidth * 0.8f);
        t2.setPosition(cameraUI.viewportWidth * 0.1f, cameraUI.viewportHeight * 0.4f);
        Label player2 = new Label("Juancho", skin);
        player2.setAlignment(Align.left);
        player2.setWidth(cameraUI.viewportWidth * 0.2f);
        player2.setHeight(cameraUI.viewportHeight * 0.2f);
        Label level2 = new Label("Nivel 2", skin);
        level2.setAlignment(Align.bottomRight);
        level2.setWidth(cameraUI.viewportWidth * 0.4f);
        t2.row();
        t2.add(player2).width(cameraUI.viewportWidth * 0.3f).height(cameraUI.viewportHeight * 0.2f);
        t2.add(level2).width(cameraUI.viewportWidth * 0.35f).height(cameraUI.viewportHeight * 0.1f).padLeft(cameraUI.viewportHeight * 0.05f).align(Align.bottom);

        Table t3 = new Table();
        t3.setHeight(cameraUI.viewportHeight * 0.2f);
        t3.setWidth(cameraUI.viewportWidth * 0.8f);
        t3.setPosition(cameraUI.viewportWidth * 0.1f, cameraUI.viewportHeight * 0.15f);
        Label player3 = new Label("Vacio", skin);
        player3.setAlignment(Align.left);
        player3.setWidth(cameraUI.viewportWidth * 0.2f);
        player3.setHeight(cameraUI.viewportHeight * 0.2f);
        Label level3 = new Label("", skin);
        level3.setAlignment(Align.bottomRight);
        level3.setWidth(cameraUI.viewportWidth * 0.4f);
        t3.row();
        t3.add(player3).width(cameraUI.viewportWidth * 0.3f).height(cameraUI.viewportHeight * 0.2f);
        t3.add(level3).width(cameraUI.viewportWidth * 0.35f).height(cameraUI.viewportHeight * 0.1f).padLeft(cameraUI.viewportHeight * 0.05f).align(Align.bottom);

        t1.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("tocado");
                greyAdventure.setScreen(new Game(greyAdventure));
                dispose();
                super.touchUp(event, x, y, pointer, button);
            }
        });

        stage.addActor(t1);
        stage.addActor(t2);
        stage.addActor(t3);
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
