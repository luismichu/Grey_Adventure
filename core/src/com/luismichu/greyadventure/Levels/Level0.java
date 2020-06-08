package com.luismichu.greyadventure.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.luismichu.greyadventure.Manager.*;
import com.luismichu.greyadventure.Manager.Physic.MyPhysicManager;

public class Level0 extends Level {
    private static Level0 level0;

    public static Level0 create(MyAssetManager assetManager, MyPreferenceManager preferenceManager, MyPhysicManager physicManager,
                                OrthographicCamera camera, OrthographicCamera cameraUI, GameState gameState){
        level0 = new Level0(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);
        return getInstance();
    }

    public static Level0 getInstance(){
        return level0;
    }

    private Level0(MyAssetManager assetManager, MyPreferenceManager preferenceManager, MyPhysicManager physicManager,
                   OrthographicCamera camera, OrthographicCamera cameraUI, GameState gameState){
        super(assetManager, preferenceManager, physicManager, camera, cameraUI, gameState);

        dialog.setMessages(Dialogs.get("l0d1"));
        dialoging = true;
    }

    @Override
    protected void loadDialog(String name) {

    }

    @Override
    public void update() {
        if(!paused) {
            if(fading){
                if(alpha2 > 0)
                    alpha2 -= 0.9 * Gdx.graphics.getDeltaTime();
                else
                    fading = false;
            }
            else if (dialoging)
                dialog.update();

            cameraUI.update();

            if(!dialoging) {
                MyDatabaseManager.update(gameState.position, gameState.level + 1);
                gameState = MyDatabaseManager.read(gameState.position);
                next = true;
                dispose();
            }
        }
    }

    @Override
    public void draw() {
        if(dialoging)
            dialog.draw();

        if(paused){
            if(alpha < 0.4)
                alpha += 0.8 * Gdx.graphics.getDeltaTime();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(0, 0, 0, alpha);
            shapeRenderer.setProjectionMatrix(cameraUI.combined);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, cameraUI.viewportWidth, cameraUI.viewportHeight);
            shapeRenderer.setColor(0, 0, 0, alpha + 0.4f);
            shapeRenderer.rect(0, 0, cameraUI.viewportWidth, 200);
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);

            stage.act();
            stage.draw();
        }
        else if(alpha > 0){
            alpha -= 0.8 * Gdx.graphics.getDeltaTime();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(0, 0, 0, alpha);
            shapeRenderer.setProjectionMatrix(cameraUI.combined);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, cameraUI.viewportWidth, cameraUI.viewportHeight);
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        if(fading){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(0, 0, 0, alpha2);
            shapeRenderer.setProjectionMatrix(cameraUI.combined);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, cameraUI.viewportWidth, cameraUI.viewportHeight);
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.ENTER:
                if(dialoging) {
                    if(dialog.hasFinished()) {
                        if (!dialog.next())
                            dialoging = false;
                    }
                    else dialog.finish();
                }

            break;

            case Input.Keys.ESCAPE:
                if(paused)
                    resume();
                else
                    pause();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose(){
        Dialogs.dispose();
        level0 = null;
    }
}