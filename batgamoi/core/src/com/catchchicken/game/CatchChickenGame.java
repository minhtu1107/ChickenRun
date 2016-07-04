package com.catchchicken.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CatchChickenGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	BoardScreen bScreen;
	private ActionResolver actionResolver;
	
	public CatchChickenGame(ActionResolver act)
	{
		this.actionResolver = act;
	}
	@Override
	public void create () {
//		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
		bScreen = new BoardScreen(actionResolver);
	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
		if(delta > 0)
		{
			bScreen.update(delta);
		}
		bScreen.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		try {
			Assets.ReleaseAll();
		} catch (Exception e) {
			super.dispose();
		}

	}
	public void showFullAds(boolean isShow)
	{
		actionResolver.showAds(isShow);
	}
	public void GameShareFBReset(boolean success)
	{
		if(success)
			bScreen.GameShareFBReset();
		else
			bScreen.GameReset();
	}
}
