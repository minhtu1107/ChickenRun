package com.catchchicken.game;

import java.sql.Time;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.spine.SkeletonRenderer;

public class BoxActor {

	public float PosX;
	public float PosY;
	public TextureRegion m_Texture;
//	public int m_letf;
//	public int m_right;
//	public int m_topletf;
//	public int m_topright;
//	public int m_bottomletf;
//	public int m_bottomright;
	public int[] m_link;
	public boolean m_effect;
	public BoxActor()
	{
		PosX = 0;
		PosY = 0;
		m_Texture = Assets.BoxTexture;
//		m_letf = -1;
//		m_right = -1;
//		m_topletf = -1;
//		m_topright = -1;
//		m_bottomletf = -1;
//		m_bottomright = -1;
		m_link = new int[6];
		m_effect = false;
	}
	
	private float m_timer = 0;
	private long m_current = 0;
	public void draw(PolygonSpriteBatch batch, SkeletonRenderer renderer)
	{
		float w = m_Texture.getRegionWidth()*BoardScreen.scale_ratio;
		float h = m_Texture.getRegionHeight()*BoardScreen.scale_ratio;
		if(m_effect)
		{
			m_current = TimeUtils.millis();
			m_timer = Assets.FadeIn_skeleton.getData().findAnimation("animation").getDuration();
			m_timer = m_timer/Assets.FadeIn_state.getTimeScale();
//			System.out.println("time ---- " + m_timer);
			m_effect = false;
		}
		if(TimeUtils.millis() - m_current > m_timer*1000)
		{
			m_timer = 0;
			m_current = 0;
		}
		if(m_timer == 0 || (m_timer !=0 && TimeUtils.millis() - m_current > m_timer*1000/3))
		{
			batch.draw(m_Texture, PosX - w/2, PosY - h/2, w, h);
		}

		if(m_timer>0)
		{
			Assets.FadeIn_skeleton.setPosition(PosX, PosY - h + 10);
			Assets.FadeIn_state.update(Gdx.graphics.getDeltaTime());
			Assets.FadeIn_state.apply(Assets.FadeIn_skeleton);
			Assets.FadeIn_skeleton.updateWorldTransform();
			renderer.draw(batch, Assets.FadeIn_skeleton);
			batch.draw(Assets.BoxTexture, PosX - w/2, PosY - h/2, w, h);
		}
	}
	
	public void setTexture(TextureRegion texture)
	{
		m_Texture = texture;
	}
	
	public void setTextureIdx(int i)
	{
		if(m_Texture != Assets.BoxTexture)
			return;
		if(i%2==0)
		{
			m_Texture = Assets.DaTexture;
			m_effect = true;
		}
		else
			m_Texture = Assets.CayTexture;
	}
	
	public boolean isBlock()
	{
		return (m_Texture != Assets.BoxTexture);
	}
}
