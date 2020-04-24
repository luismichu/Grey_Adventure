package com.luismichu.greyadventure;

import com.badlogic.gdx.Game;
import com.luismichu.greyadventure.Manager.MyWindowListener;

public class GreyAdventure extends Game {
	static float worldSpeed = 1f;

	@Override
	public void create () {
		MyWindowListener.instantiate();
		//setScreen(new com.luismichu.greyadventure.Game(this));
		setScreen(new MainMenu(this));
	}
}
