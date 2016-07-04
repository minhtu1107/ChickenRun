package com.catchchicken.game;

public class Gamedef {

	static final int STATE_SPLASH = 0;
	static final int STATE_TUTORIAL = STATE_SPLASH + 1;
	static final int STATE_GAMEPLAY = STATE_TUTORIAL + 1;
	static final int STATE_RESULT = STATE_GAMEPLAY + 1;
	static final int STATE_WIN = STATE_RESULT + 1;
	static final int STATE_LOSE = STATE_WIN + 1;
}
