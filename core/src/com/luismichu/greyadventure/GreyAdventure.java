package com.luismichu.greyadventure;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.luismichu.greyadventure.Manager.MyPreferenceManager;
import com.luismichu.greyadventure.Manager.MyWindowListener;

public class GreyAdventure extends Game {
	static float worldSpeed = 1f;

	@Override
	public void create () {
		MyWindowListener.instantiate();
		setScreen(new MainMenu(this));
	}
}
