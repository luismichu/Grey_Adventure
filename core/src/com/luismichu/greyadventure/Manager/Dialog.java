package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

//TODO doc
public class Dialog {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private String message, currentMessage, lastMessage;
    private Array<String> messages;
    private Label label;
    private float textSpeed;
    private float elapsedTime;
    private float volume;
    private Array<Texture> backgrounds;
    private Texture background;
    private MyAssetManager assetManager;
    private MyPreferenceManager preferenceManager;
    private Sound textSound;
    private int width, height, charNum;

    public Dialog(String message, OrthographicCamera camera){
        this.message = message;
        this.camera = camera;
        batch = new SpriteBatch();

        currentMessage = "";
        lastMessage = "";
        elapsedTime = 0;
        charNum = 0;
        assetManager = new MyAssetManager();
        preferenceManager = new MyPreferenceManager();
        textSpeed = preferenceManager.getTextSpeed();
        messages = new Array<>();

        assetManager.loadDialog();
        label = new Label("", assetManager.getSkin(MyAssetManager.AssetDescriptors.skin));
        label.setAlignment(Align.topLeft);
        label.setWrap(true);
        backgrounds = assetManager.getTextures(MyAssetManager.AssetDescriptors.dialogueBox);

        background = backgrounds.get(0);

        textSound = assetManager.getSound(MyAssetManager.AssetDescriptors.textSound);
        volume = preferenceManager.getVolume() / 100f;
    }

    public Dialog(OrthographicCamera camera){
        this("", camera);
    }

    public Dialog(Array<String> messages, OrthographicCamera camera){
        this(messages.first(), camera);
        this.messages = messages;
        messages.removeValue(messages.first(), false);
    }

    public void update(){
        float delta = Gdx.graphics.getDeltaTime();
        textSpeed = preferenceManager.getTextSpeed();
        volume = preferenceManager.getVolume() / 100f;

        elapsedTime += delta;
        charNum = (int)(elapsedTime / textSpeed);
        if(charNum > message.length())
            background = backgrounds.get((int)(elapsedTime % 2));

        lastMessage = currentMessage;
        currentMessage = message.substring(0, Math.min(charNum, message.length()));
        label.setText(currentMessage);

        if(!lastMessage.equals(currentMessage) && !currentMessage.endsWith(" ")) {
            textSound.stop();
            textSound.play(volume);
        }
    }

    public void draw(){
        width = (int)(camera.viewportWidth * 0.8f);
        height = (int)(camera.viewportHeight * 0.2f);
        label.setBounds(camera.viewportWidth * 0.13f, camera.viewportHeight * 0.73f, width * 0.9f, height * 0.9f);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, camera.viewportWidth * 0.1f , camera.viewportHeight * 0.76f, width, height);
        label.draw(batch, 1f);
        batch.end();
    }

    public void setMessage(String newMessage){
        message = newMessage;
        currentMessage = "";
        elapsedTime = 0;
        background = backgrounds.get(0);
    }

    public void setMessages(Array<String> newMessages){
        setMessage(newMessages.first());
        messages.clear();
        messages.addAll(newMessages);
        messages.removeValue(messages.first(), false);
    }

    public void addMessage(String message){
        messages.add(message);
    }

    public boolean next(){
        int size = messages.size;
        if(size > 0) {
            setMessage(messages.first());
            messages.removeValue(messages.first(), false);
        }

        return size > 0;
    }

    public void finish(){
        elapsedTime = message.length() * textSpeed;
    }

    public boolean hasFinished(){
        return charNum > message.length();
    }
}
