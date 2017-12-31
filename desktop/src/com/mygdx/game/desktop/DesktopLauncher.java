package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.comp460game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 720;
		config.height = 480;
		//config.fullscreen = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		new LwjglApplication(new comp460game(), config);
	}
}
