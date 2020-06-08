package com.luismichu.greyadventure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.luismichu.greyadventure.Manager.MyWindowListener;

public class GreyAdventure extends Game {
	public static Music music;

	@Override
	public void create () {
		MyWindowListener.instantiate();
		setScreen(new MainMenu(this, true));
	}
}