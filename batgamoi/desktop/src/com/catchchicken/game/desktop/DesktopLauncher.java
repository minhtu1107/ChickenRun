package com.catchchicken.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.catchchicken.game.CatchChickenGame;

public class DesktopLauncher {
	static CatchChickenGame mygame;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 540;
		mygame = new CatchChickenGame();
		mygame.setActionResolver(new ActionResolverDesktop());
		new LwjglApplication(mygame, config);
	}
}
