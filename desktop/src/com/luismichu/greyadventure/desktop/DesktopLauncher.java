package com.luismichu.greyadventure.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.luismichu.greyadventure.GreyAdventure;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Grey Adventure");
		config.useVsync(true);
		config.setResizable(false);
		config.setWindowIcon(Files.FileType.Internal, "sprites/icon.bmp");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		config.setWindowedMode((int)(screenSize.width * 0.7), (int)(screenSize.height * 0.7));
		new Lwjgl3Application(new GreyAdventure(), config);
	}
}
