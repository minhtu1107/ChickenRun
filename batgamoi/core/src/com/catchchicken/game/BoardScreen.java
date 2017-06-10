package com.catchchicken.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.AnimationState.TrackEntry;

public class BoardScreen extends InputAdapter 
{
	//////////// render
	PolygonSpriteBatch batch;
	SkeletonRenderer renderer;
	OrthographicCamera camera;
	//////////// constant
	public static float scr_w = 360;
	public static float scr_h = 640;
	public static float scale_ratio = 1f;
	///////////
	//10 + (5+9)*5
	public BoxActor[] g_MoveActor;
	public int Line = 11;
	public BoxActor BoxAlign;
	public Vector2[] g_MovePos;
	public int MaxBoardIdx = 80;
	///////////
	private Stage stage;
	private Table table;
	private Skin buttonSkin;
//	private TextButton button;
	private TextButton[] g_Button;
	private TextButton BtnPlay;
	private TextButton BtnTutor;
	private TextButton BtnMusic;
	private TextButtonStyle Musicstyle = new TextButtonStyle();
//	private boolean isMusicOn = !true;
	private TextButton BtnSound;
	private TextButtonStyle Soundstyle = new TextButtonStyle();
//	private boolean isSoundOn = !true;
	private TextButton BtnShare;
	private TextButton BtnCont;
	private TextButton BtnExit;
	///////////
	public int m_CurrentIdx = 45;
	public int m_NextIdx = 35;
	public boolean m_Moving = false;
	public int m_MoveStep = 1;
	
	public final int MAX_MOVE = 500;
	public final int LEFT 			= 0;
	public final int TOP_LEFT 		= LEFT + 1;
	public final int BOT_LEFT 		= TOP_LEFT + 1;
	public final int RIGHT 			= BOT_LEFT + 1;
	public final int TOP_RIGHT 		= RIGHT + 1;
	public final int BOT_RIGHT 		= TOP_RIGHT + 1;
	public final int m_SubFindDir[][] = 
		{
			{TOP_LEFT, BOT_LEFT},
			{LEFT, TOP_RIGHT},
			{LEFT, BOT_RIGHT},
			{TOP_RIGHT, BOT_RIGHT},
			{TOP_LEFT, RIGHT},
			{RIGHT, BOT_LEFT}
		};
	
	public int MaxMove[];
	public int MoveId[];
	
	private Array<Animation> BoyAnim;
//	private int m_level = 3;
	private String m_SkinName[] = 
		{
			// "default",
			"Capt3",
			"Capt2",
			"Capt1",
			"Cap0",
			"Cap1",
			"Cap2",
			"Cap3",
			"Cap4",
			"Cap5",
			"Cap6",
			"Cap7",
			"Cap8"
		};
	private String m_lvName[] = 
		{
			"_0013_I",
			"_0013_I",
			"_0013_I",
			"_0013_I",
			"_0012_II",
			"_0011_III",
			"_0010_IV",
			"_0009_V",
			"_0000_VI",
			"_0001_VII",
			"_0008_VIII",
			"_0007_IX"
		};
	private String m_AnimName[] = 
		{
			"An",
			"Help me",
			"Like shit",
			"Oh no",
			"Buon",
			"I love",
			"Met",
			"Tho",
		};
	private int numMoves = 0;
	EndActor EndScreen;
	private int game_state;
	private ActionResolver actionResolver;
	private BitmapFont Bmfont;
	GlyphLayout layout;
	public BoardScreen(ActionResolver act)
	{
		actionResolver = act;
		scr_w = Gdx.graphics.getWidth();
		scr_h = Gdx.graphics.getHeight();
		scale_ratio = scr_h/1080;
		batch = new PolygonSpriteBatch();
		renderer = new SkeletonRenderer();
		Assets.LoadCommon();
		
		//Init button
		stage = new Stage(new ScreenViewport());
		table = new Table();
		stage.addActor(table);
		buttonSkin = new Skin();
		buttonSkin.addRegions(Assets.Box_atlas);
		Gdx.input.setInputProcessor(stage);
		TextButtonStyle style = new TextButtonStyle(); //** Button properties **//
        style.up = null;	//buttonSkin.getDrawable("box");
        style.down = null;	//buttonSkin.getDrawable("Da");
        style.font = new BitmapFont();
        
        Line = MapInfo.s_MapIdx.length;
        MaxBoardIdx = 0;
        for(int i = 0; i< Line; i++)
        {
        	MaxBoardIdx += MapInfo.s_MapIdx[i].length;
        }
        g_Button = new TextButton[MaxBoardIdx];
		g_MoveActor = new BoxActor[MaxBoardIdx];
		g_MovePos = new Vector2[MaxBoardIdx];
		float centerX = scr_w/2;
		float centerY = scr_h/2;
		float w = Assets.BoxTexture.getRegionWidth()*BoardScreen.scale_ratio;
		float h = Assets.BoxTexture.getRegionHeight()*BoardScreen.scale_ratio;
		int row = 0;
		int col = 0;
		for(int i = 0; i< MaxBoardIdx; i++)
		{
			g_MoveActor[i] = new BoxActor();
			col = i;
			row = 0;
	        for(int j = 0; j< Line; j++)
	        {
	        	if(col - MapInfo.s_MapIdx[j].length < 0)
	        	{
	        		break;
	        	}
	        	else
	        	{
	        		col -= MapInfo.s_MapIdx[j].length;
	        	}
	        	row++;
	        }
	        g_MoveActor[i].PosX = centerX + MapInfo.s_MapIdx[row][col]*w;
	        g_MoveActor[i].PosY = centerY + ((Line)/2 - row)*(h-10*BoardScreen.scale_ratio);
	        g_MoveActor[i].m_link[LEFT] = MapInfo.s_LeftIdx[i];
	        g_MoveActor[i].m_link[RIGHT] = MapInfo.s_RightIdx[i];
	        g_MoveActor[i].m_link[TOP_LEFT] = MapInfo.s_TopLeft[i];
	        g_MoveActor[i].m_link[TOP_RIGHT] = MapInfo.s_TopRight[i];
	        g_MoveActor[i].m_link[BOT_LEFT] = MapInfo.s_BotLeft[i];
	        g_MoveActor[i].m_link[BOT_RIGHT] = MapInfo.s_BotRight[i];
	        g_MovePos[i] = new Vector2(g_MoveActor[i].PosX, g_MoveActor[i].PosY);
	        initButton(i, style, w*0.8f, h*0.8f);
		}
		Assets.Ga_skeleton.setPosition(g_MovePos[m_CurrentIdx].x, g_MovePos[m_CurrentIdx].y - h/4);
		SaveManager.getInstance().LoadGame();
		if(SaveManager.getInstance().data.m_isFirstLaunch == 1) {
			g_MoveActor[14].setTextureIdx(1);
			g_MoveActor[22].setTextureIdx(1);
			g_MoveActor[32].setTextureIdx(1);
			g_MoveActor[43].setTextureIdx(1);
			g_MoveActor[55].setTextureIdx(1);
			g_MoveActor[66].setTextureIdx(1);
			g_MoveActor[16].setTextureIdx(1);
			g_MoveActor[59].setTextureIdx(1);
			g_MoveActor[37].setTextureIdx(1);
			SaveManager.getInstance().data.m_isFirstLaunch = 2;
			SaveManager.getInstance().SaveGame();
		}
		else
		{
			MapReset();
		}
		
		MaxMove = new int[BOT_RIGHT+1];
		MoveId = new int[BOT_RIGHT+1];
		
		//Button
		BtnTutor = new TextButton("", style);
		BtnTutor.setPosition(0, 0);
		BtnTutor.setHeight(scr_h);
		BtnTutor.setWidth(scr_w);
		BtnTutor.addListener(new InputListener() 
		{
          public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) 
          {
//        	  Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//

        	  return true;
          }
 
          public void touchUp (InputEvent event, float x, float y, int pointer, int button) 
          {
			  game_state = Gamedef.STATE_GAMEPLAY;
			  table.removeActor(BtnTutor);
			  table.addActor(BtnMusic);
			  table.addActor(BtnSound);
          }
		});

		initUIButton();
		BoyAnim = Assets.Boy_skeleton.getData().getAnimations();
		Iterator<Animation> iter = BoyAnim.iterator();
		while (iter.hasNext())
		{
			Animation anim = iter.next();
//			System.out.println(anim.getName());
		}
//		Assets.Boy_skeleton.getData().setImagesPath("data/Boy/skeleton2.png");
		Array<com.esotericsoftware.spine.Skin> Boyskin = Assets.Boy_skeleton.getData().getSkins();
		Iterator<com.esotericsoftware.spine.Skin> iter2 = Boyskin.iterator();
		while (iter2.hasNext())
		{
			com.esotericsoftware.spine.Skin anim = iter2.next();
//			System.out.println(anim.getName());
		}
		//Read
		SaveManager.getInstance().LoadGame();
//		FileHandle file = Gdx.files.local("data/savefile/data.sav");
//		if(file.exists())
//		{
//			try
//			{
//				m_level = Integer.valueOf(file.readString());
//			}
//			catch(Exception e)
//			{
//				m_level = 3;
//			}
//			if(m_level<0 || m_level>m_SkinName.length-1)
//				m_level = 3;
//		}
		Assets.Boy_skeleton.setSkin(m_SkinName[SaveManager.getInstance().data.m_level]);
		float duration = Assets.Boy_skeleton.getData().findAnimation(m_AnimName[6]).getDuration();
//		System.out.println("" + duration);
//		TrackEntry current = Assets.Boy_state.setAnimation(0, m_AnimName[6], false);
//		Assets.Boy_state.addAnimation(0, m_AnimName[5], false, duration);
//		System.out.println("" + current.getTime());
//		System.out.println("" + current.getEndTime());
//		for(int i=1;i<m_AnimName.length;i++)
//		{
//			Assets.Boy_state.addAnimation(0, m_AnimName[i], true, duration);
//			duration = Assets.Boy_skeleton.getData().findAnimation(m_AnimName[i]).getDuration();
//		}
//		Assets.Boy_state.getData().setMix(m_AnimName[6], m_AnimName[5], 0.0f);
//		Assets.Boy_state.getData().setMix(m_AnimName[5], m_AnimName[6], 0.0f);
		m_current_boy_anim = MathUtils.random(4,7);
		Assets.Boy_state.setAnimation(0, m_AnimName[m_current_boy_anim], false);
		Assets.Boy_skeleton.updateWorldTransform();
		
		Vector2 offset = new Vector2(), size = new Vector2();
		Assets.Boy_skeleton.getBounds(offset, size);
		Assets.Boy_skeleton.setPosition(scr_w - size.x/2 - 10, 0);
//		SetChickenAnim();
		Assets.LvTexture = Assets.UI_atlas.findRegion(m_lvName[SaveManager.getInstance().data.m_level]);
		
		game_state = Gamedef.STATE_SPLASH;
		EndScreen = new EndActor();
		EndScreen.setVisible(false);
		Bmfont = new BitmapFont(Gdx.files.internal("data/font1.fnt"), Gdx.files.internal("data/font1.png"), false);
		Bmfont.getData().setScale(scale_ratio);
		layout = new GlyphLayout();

		Assets.GaS_skeleton.updateWorldTransform();
		Assets.GaS_skeleton.getBounds(offset, size);
		Assets.GaS_skeleton.setPosition(scr_w/2 + size.x/3, 3*scr_h/8);
		Assets.BoyS_skeleton.setPosition(scr_w/2 + size.x/3, 3*scr_h/8);
	}
	
	public float GetDeltaX(int idx)
	{
		return 0;
	}
	
	public float GetDeltaY(int idx)
	{
		return 0;
	}
	
	public void initUIButton()
	{
		Musicstyle.up = null;	//buttonSkin.getDrawable("box");
		Musicstyle.down = null;	//buttonSkin.getDrawable("Da");
		Musicstyle.font = new BitmapFont();
        
		buttonSkin.addRegions(Assets.UI_atlas);
		float btnSize = buttonSkin.getRegion("_0001_Sound-off").getRegionWidth()*scale_ratio;
		BtnMusic = new TextButton("", Musicstyle);
		if(SaveManager.getInstance().isMusicOn())
		{
			Musicstyle.up = buttonSkin.getDrawable("_0005_Sound");
			Assets.SoundEffect.BGM.loop();
		}
		else
			Musicstyle.up = buttonSkin.getDrawable("_0001_Sound-off");
		BtnMusic.setPosition(20*scale_ratio + btnSize + 20*scale_ratio, scr_h - 20*scale_ratio - btnSize);
		BtnMusic.setHeight(btnSize);
		BtnMusic.setWidth(btnSize);
		BtnMusic.addListener(new InputListener() 
		{
          public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) 
          {
//        	  Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
        	  return true;
          }
 
          public void touchUp (InputEvent event, float x, float y, int pointer, int button) 
          {
      		if(SaveManager.getInstance().isMusicOn())
      		{
				Musicstyle.up = buttonSkin.getDrawable("_0001_Sound-off");
				Assets.SoundEffect.BGM.stop();
				SaveManager.getInstance().data.m_isMusicOn = 2;
      		}
    		else
    		{
				Musicstyle.up = buttonSkin.getDrawable("_0005_Sound");
				Assets.SoundEffect.BGM.loop();
				SaveManager.getInstance().data.m_isMusicOn = 1;
    		}
			  SaveManager.getInstance().SaveGame();
          }
		});

		Soundstyle.up = null;	//buttonSkin.getDrawable("box");
        Soundstyle.down = null;	//buttonSkin.getDrawable("Da");
        Soundstyle.font = new BitmapFont();
		BtnSound = new TextButton("", Soundstyle);
		if(SaveManager.getInstance().isSoundOn())
			Soundstyle.up = buttonSkin.getDrawable("_0004_Music");
		else
			Soundstyle.up = buttonSkin.getDrawable("_0000_Music-off");
		BtnSound.setPosition(20*scale_ratio, scr_h - 20*scale_ratio - btnSize);
		BtnSound.setHeight(btnSize);
		BtnSound.setWidth(btnSize);
		BtnSound.addListener(new InputListener() 
		{
          public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) 
          {
//        	  Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
        	  return true;
          }
 
          public void touchUp (InputEvent event, float x, float y, int pointer, int button) 
          {
        	  if(SaveManager.getInstance().isSoundOn()) {
				  Soundstyle.up = buttonSkin.getDrawable("_0000_Music-off");
				  SaveManager.getInstance().data.m_isSoundOn = 2;
			  }
        	  else {
				  Soundstyle.up = buttonSkin.getDrawable("_0004_Music");
				  SaveManager.getInstance().data.m_isSoundOn = 1;
			  }
			  SaveManager.getInstance().SaveGame();
//			  actionResolver.LoginFB();
          }
		});

		final float btnW = buttonSkin.getRegion("Exit").getRegionWidth()*scale_ratio;
		final float btnH = buttonSkin.getRegion("Exit").getRegionHeight()*scale_ratio;
		float start = scr_w - btnW*3 - 2*20*scale_ratio;
		start = start/2;

		TextButtonStyle shareSty = new TextButtonStyle();
		shareSty.up = buttonSkin.getDrawable("_0001_F1");;	//buttonSkin.getDrawable("box");
		shareSty.down = null;	//buttonSkin.getDrawable("Da");
		shareSty.font = new BitmapFont();
		BtnShare = new TextButton("", shareSty);
		BtnShare.setPosition(start, scr_h/6);
		BtnShare.setHeight(btnH);
		BtnShare.setWidth(btnW);
		BtnShare.addListener(new InputListener()
		{
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
//				BtnShare.setHeight(0.8f*btnH);
				BtnShare.setWidth(0.9f*btnW);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button)
			{
				BtnShare.setHeight(btnH);
				BtnShare.setWidth(btnW);
				actionResolver.showAds(false);
//				actionResolver.LoginFB(SaveManager.getInstance().data.m_level, numMoves);
			}
		});

		start += btnW + 20*scale_ratio;
		TextButtonStyle ContSty = new TextButtonStyle();
		ContSty.up = buttonSkin.getDrawable("Cont");;	//buttonSkin.getDrawable("box");
		ContSty.down = null;	//buttonSkin.getDrawable("Da");
		ContSty.font = new BitmapFont();
		BtnCont = new TextButton("", ContSty);
		BtnCont.setPosition(start, scr_h/6);
		BtnCont.setHeight(btnH);
		BtnCont.setWidth(btnW);
		BtnCont.addListener(new InputListener()
		{
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
//				BtnCont.setHeight(0.8f*btnH);
				BtnCont.setWidth(0.9f*btnW);
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button)
			{
				BtnCont.setHeight(btnH);
				BtnCont.setWidth(btnW);
				actionResolver.showAds(false);
				GameReset();
			}
		});

		start += btnW + 20*scale_ratio;
		TextButtonStyle ExitSty = new TextButtonStyle();
		ExitSty.up = buttonSkin.getDrawable("Exit");;	//buttonSkin.getDrawable("box");
		ExitSty.down = null;	//buttonSkin.getDrawable("Da");
		ExitSty.font = new BitmapFont();
		BtnExit = new TextButton("", ExitSty);
		BtnExit.setPosition(start, scr_h/6);
		BtnExit.setHeight(btnH);
		BtnExit.setWidth(btnW);
		BtnExit.addListener(new InputListener()
		{
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
//				BtnExit.setHeight(0.8f*btnH);
				BtnExit.setWidth(0.9f*btnW);
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button)
			{
				BtnExit.setHeight(btnH);
				BtnExit.setWidth(btnW);
				actionResolver.showAds(false);
				Gdx.app.exit();
			}
		});

		buttonSkin.addRegions(Assets.Splash_atlas);
		TextButtonStyle style = new TextButtonStyle(); //** Button properties **//
		style.up = buttonSkin.getDrawable("_0006_Play");
		style.down = null;	//buttonSkin.getDrawable("Da");
		style.font = new BitmapFont();
		BtnPlay = new TextButton("", style);
		float playW = buttonSkin.getRegion("_0006_Play").getRegionWidth()*scale_ratio;
		float playH = buttonSkin.getRegion("_0006_Play").getRegionHeight()*scale_ratio;
		BtnPlay.setPosition(scr_w/2 - playW/2, playH/4);
		BtnPlay.setHeight(playH);
		BtnPlay.setWidth(playW);
		BtnPlay.addListener(new InputListener()
		{
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button)
			{
				game_state = Gamedef.STATE_TUTORIAL;
				table.removeActor(BtnPlay);
				table.addActor(BtnTutor);
			}
		});
		table.addActor(BtnPlay);
//		BtnShare.setVisible(false);
//		BtnCont.setVisible(false);
//		BtnExit.setVisible(false);
	}
	public void initButton(int index, TextButtonStyle style, float w, float h)
	{
		g_Button[index] = new TextButton(""/*+index*/, style); //** Button text and style **//
		g_Button[index].setPosition(g_MovePos[index].x - w/2, g_MovePos[index].y - h/2); //** Button location **//
		g_Button[index].setHeight(h); //** Button Height **//
		g_Button[index].setWidth(w); //** Button Width **//
		g_Button[index].addListener(new InputListener() {
//            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
////            	Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
//                    return true;
//            }
//           
//            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            	
//            }
        });
		table.addActor(g_Button[index]);
	}

	public void MapReset()
	{
		float h = Assets.BoxTexture.getRegionHeight()*BoardScreen.scale_ratio;
		m_CurrentIdx = MathUtils.random(45,48);
		Assets.Ga_skeleton.setPosition(g_MovePos[m_CurrentIdx].x, g_MovePos[m_CurrentIdx].y - h/4);
		int curidx = 0;
		int MaxStone = 7;
		if(SaveManager.getInstance().data.m_level <= 5)
			MaxStone = 8;
		else if(SaveManager.getInstance().data.m_level <= 2)
			MaxStone = 9;

		for(int i = 0; i< Line; i++)
		{
			int rnd = MathUtils.random(1,100);
			if(rnd % 3 == 0 || Line - i - MaxStone <= 0)
			{
				rnd = MathUtils.random(curidx,curidx + MapInfo.s_MapIdx[i].length - 1);
				if(rnd != m_CurrentIdx)
				{
					g_MoveActor[rnd].setTextureIdx(1);
					MaxStone--;
				}
			}
			curidx += MapInfo.s_MapIdx[i].length;
		}
	}
	public void GameReset()
	{
		table.removeActor(EndScreen);
		EndScreen.setVisible(false);
		EndScreen.SetLvup(false, null, "");
		table.removeActor(BtnShare);
		table.removeActor(BtnCont);
		table.removeActor(BtnExit);
		for(int i = 0; i< MaxBoardIdx; i++)
		{
			g_MoveActor[i].setTexture(Assets.BoxTexture);
			g_Button[i].setVisible(true);
		}
//		BtnTutor.setVisible(false);
//		BtnShare.setVisible(false);
//		BtnCont.setVisible(false);
//		BtnExit.setVisible(false);

		MapReset();

        Assets.Boy_skeleton.setSkin(m_SkinName[SaveManager.getInstance().data.m_level]);
        Assets.LvTexture = Assets.UI_atlas.findRegion(m_lvName[SaveManager.getInstance().data.m_level]);

		numMoves = 0;
        game_state = Gamedef.STATE_GAMEPLAY;
	}
	
	public void GameShareFBReset()
	{
		SaveManager.getInstance().data.m_level++;
		SaveManager.getInstance().SaveGame();
		GameReset();
	}
	private String ShareString;
	public void ShowGameReset(boolean win)
	{
		if(win)
		{
			ShareString = "Chicken was catched after " + numMoves + " moves. Try it now!";
			System.out.println("win " + m_SkinName[SaveManager.getInstance().data.m_level]);
			if(SaveManager.getInstance().data.m_level < m_SkinName.length-1)
				SaveManager.getInstance().data.m_level++;
			game_state = Gamedef.STATE_WIN;
			rewardPosX = g_MovePos[m_CurrentIdx].x;
			rewardPosY = g_MovePos[m_CurrentIdx].y;
			m_MoveStep = 0;
			if(SaveManager.getInstance().isSoundOn())
				Assets.SoundEffect.win.play();
		}
		else
		{
			ShareString = "Chicken run after " + numMoves + " moves. Try it now!";
			System.out.println("lose " + m_SkinName[SaveManager.getInstance().data.m_level]);
			if(SaveManager.getInstance().data.m_level > 0)
				SaveManager.getInstance().data.m_level--;
			game_state = Gamedef.STATE_LOSE;
			if(SaveManager.getInstance().isSoundOn())
				Assets.SoundEffect.lose.play();
		}
		for(int i = 0; i< MaxBoardIdx; i++)
		{
			g_Button[i].setVisible(false);
		}
//		//write
//		FileHandle file = Gdx.files.local("data/savefile/data.sav");
//		file.writeString(Integer.toString(SaveManager.getInstance().m_level ),false);
		SaveManager.getInstance().SaveGame();
		m_current = 0;
		//BtnReset.setVisible(true);
//		BtnShare.setVisible(true);
//		BtnCont.setVisible(true);
//		BtnExit.setVisible(true);
//		actionResolver.showAds(true);
	}

	private boolean m_showFadeOut = false;
	private float m_timer = 0;
	private long m_current = 0;

	private float m_ChickenTimer = 0;
	private float m_totalTime = -1;
	public void SetChickenAnim()
	{
		if(m_Moving)
		{
			TrackEntry current = Assets.Ga_state.getCurrent(0);
			Animation newAnim = Assets.Ga_skeleton.getData().findAnimation("chay");
			if(current == null || current.getAnimation() == null || current.getAnimation() != newAnim)
			{
				if(current_dir<RIGHT)
				{
					if(last_dir == RIGHT)
					{
						Assets.Ga_skeleton.setFlipX(true);
						last_dir = LEFT;
					}
				}
				else
				{
					if(last_dir == LEFT)
					{
						Assets.Ga_skeleton.setFlipX(false);
						last_dir = RIGHT;
					}
				}
				Assets.Ga_skeleton.setSlotsToSetupPose();
				Assets.Ga_skeleton.setBonesToSetupPose();
				Assets.Ga_state.setAnimation(0, "chay", true);
			}
		}
		else
		{
			String[] anim = {"mo", "tho", "gay"};
			TrackEntry current = Assets.Ga_state.getCurrent(0);
			if(current == null || current.isComplete())
			{
				int rnd = MathUtils.random(0,2);
				Assets.Ga_skeleton.setSlotsToSetupPose();
				Assets.Ga_skeleton.setBonesToSetupPose();
				Assets.Ga_state.setAnimation(0, anim[rnd], false);
			}
		}
		Assets.Ga_state.update(Gdx.graphics.getDeltaTime());
		Assets.Ga_state.apply(Assets.Ga_skeleton);
		Assets.Ga_skeleton.updateWorldTransform();
	}

	public int m_current_boy_anim = 0;
	public void updateBoyAnim()
	{
		TrackEntry current = Assets.Boy_state.getCurrent(0);
		if(current == null || current.isComplete())
		{
			if(game_state == Gamedef.STATE_WIN && m_current_boy_anim != -1)
			{
				int level = SaveManager.getInstance().data.m_level;
				Assets.Boy_skeleton.setSlotsToSetupPose();
				Assets.Boy_state.setAnimation(0, "An", true);
				m_current_boy_anim = -1;
				if(level<m_SkinName.length-1)
					level += 1;
				EndScreen.SetLvup(true, "An", m_SkinName[level]);
			}
			else if(game_state == Gamedef.STATE_LOSE && m_current_boy_anim != -1)
			{
				int level = SaveManager.getInstance().data.m_level;
				Assets.Boy_skeleton.setSlotsToSetupPose();
//				float duration = Assets.Boy_skeleton.getData().findAnimation("Like shit").getDuration();
//				Assets.Boy_state.setAnimation(0, "Like shit", false);
				String anim = "Oh no";
				if(SaveManager.getInstance().data.m_level == 0 || SaveManager.getInstance().data.m_level == 1)
					anim = "Help me";
//				if(m_level <= 2)
//					Assets.Boy_state.addAnimation(0, anim, true, duration);
				Assets.Boy_state.setAnimation(0, anim, true);
				m_current_boy_anim = -1;
//				System.out.println("yyyyyyyyy " + Assets.Boy_skeleton.getX());
				if(level>0)
					level -= 1;
				EndScreen.SetLvup(false, anim, m_SkinName[level]);
			}
			else if(game_state != Gamedef.STATE_LOSE && game_state != Gamedef.STATE_LOSE)
			{
				Assets.Boy_skeleton.setSlotsToSetupPose();
				m_current_boy_anim = MathUtils.random(4,7);
				Assets.Boy_state.setAnimation(0, m_AnimName[m_current_boy_anim], false);
//				System.out.println("xxxxxxx " + Assets.Boy_skeleton.getX());
			}
//			if(game_state == Gamedef.STATE_LOSE)
//				System.out.println("aaaaaaa " + Assets.Boy_skeleton.getX());
			if(game_state != Gamedef.STATE_LOSE)
			{
				Vector2 offset = new Vector2(), size = new Vector2();
				Assets.Boy_skeleton.getBounds(offset, size);
				Assets.Boy_skeleton.setPosition(scr_w - size.x/2 - 10, 0);
			}
//			if(game_state == Gamedef.STATE_LOSE)
//				System.out.println("bbbbbbb " + Assets.Boy_skeleton.getX());
		}
		
		Assets.Boy_state.update(Gdx.graphics.getDeltaTime());
		Assets.Boy_state.apply(Assets.Boy_skeleton);
		Assets.Boy_skeleton.updateWorldTransform();
	}
	
	public void updateGamePlay(float delta)
	{
		for(int i=0; i<MaxBoardIdx;i++)
		{
			if(m_Moving)
				break;
			if(m_CurrentIdx != -1 && !g_MoveActor[i].isBlock() && i!=m_CurrentIdx && g_Button[i].isPressed())
			{
				if(SaveManager.getInstance().isSoundOn())
					Assets.SoundEffect.hit.play();
				g_MoveActor[i].setTextureIdx(2);
				m_NextIdx = PathFinding(m_CurrentIdx);
				if(m_NextIdx != -2)
				{
					m_Moving = true;
					m_totalTime = -1;
					numMoves++;
				}
				else if(m_NextIdx == -2)
				{
					m_showFadeOut = true;
					Assets.FadeOut_skeleton.setPosition(Assets.Ga_skeleton.getX(), Assets.Ga_skeleton.getY());
					m_current = TimeUtils.millis();
					m_timer = Assets.FadeOut_skeleton.getData().findAnimation("animation").getDuration();
				}
				else if(m_NextIdx == -1)
				{
				}
			}
		}
		updateMoving();
		if(m_showFadeOut)
		{
			Assets.FadeOut_state.update(Gdx.graphics.getDeltaTime());
			Assets.FadeOut_state.apply(Assets.FadeOut_skeleton);
			Assets.FadeOut_skeleton.updateWorldTransform();

			if(TimeUtils.millis() - m_current > m_timer*1000)
			{
				m_current = 0;
				m_timer = 0;
				m_showFadeOut = false;
				if(m_NextIdx == -1)
				{
					ShowGameReset(false);
				}
				else
				{
					ShowGameReset(true);
				}
			}
		}
		
		updateBoyAnim();
		
		m_ChickenTimer+=delta;
		SetChickenAnim();
	}
	
	public void update(float delta)
	{
		switch(game_state)
		{
			case Gamedef.STATE_SPLASH:
			{
//				if(m_current == 0)
//				{
//					m_current = TimeUtils.millis();
//				}
//				if(TimeUtils.millis() - m_current > 2000)
//				{
//					m_current = 0;
//					game_state = Gamedef.STATE_TUTORIAL;
//					table.addActor(BtnTutor);
//				}
				Assets.GaS_state.update(Gdx.graphics.getDeltaTime());
				Assets.GaS_state.apply(Assets.GaS_skeleton);
				Assets.GaS_skeleton.updateWorldTransform();

				Assets.BoyS_state.update(Gdx.graphics.getDeltaTime());
				Assets.BoyS_state.apply(Assets.BoyS_skeleton);
				Assets.BoyS_skeleton.updateWorldTransform();
				break;
			}
			case Gamedef.STATE_TUTORIAL:
			{
//				if(m_current == 0)
//				{
//					m_current = TimeUtils.millis();
//				}
//				if(TimeUtils.millis() - m_current > 500)
//				{
//					m_current = 0;
//					game_state = Gamedef.STATE_GAMEPLAY;
//				}
				game_counter++;
				if(game_counter > 10000)
					game_counter = 0;
				break;
			}
			case Gamedef.STATE_GAMEPLAY:
			{
				updateGamePlay(delta);
				break;
			}
			case Gamedef.STATE_LOSE:
			case Gamedef.STATE_WIN:
			{
				updateBoyAnim();
				if(!updateReward() && EndScreen.Ready() && !EndScreen.isVisible())
				{
					EndScreen.setVisible(true);
					table.addActor(EndScreen);
					table.addActor(BtnShare);
					table.addActor(BtnCont);
					table.addActor(BtnExit);
					actionResolver.showAds(true);
//					if(m_current == 0)
//					{
//						m_current = TimeUtils.millis();
//					}
//					if(TimeUtils.millis() - m_current > 3000)
//					{
//						m_current = 0;
//						GameReset();
//					}
				}
				break;
			}
		}
	}
	
	public void updateMoving()
	{
//		int nextIdx = -1;
		if(m_Moving)
		{
			float h = Assets.BoxTexture.getRegionHeight()*BoardScreen.scale_ratio;
//			nextIdx = PathFinding(m_CurrentIdx);	//g_MoveActor[m_CurrentIdx].m_topletf;
			float newPosX = 0;
			float newPosY = 0;
			if(m_NextIdx == -1)
			{
				float w = Assets.BoxTexture.getRegionWidth()*BoardScreen.scale_ratio;
				if(current_dir==LEFT)
				{
					newPosX = g_MovePos[m_CurrentIdx].x - w;
					newPosY = g_MovePos[m_CurrentIdx].y;
				}
				else if(current_dir==TOP_LEFT)
				{
					newPosX = g_MovePos[m_CurrentIdx].x - w/2;
					newPosY = g_MovePos[m_CurrentIdx].y + h;
				}
				else if(current_dir==BOT_LEFT)
				{
					newPosX = g_MovePos[m_CurrentIdx].x - w/2;
					newPosY = g_MovePos[m_CurrentIdx].y - h;
				}
				else if(current_dir==RIGHT)
				{
					newPosX = g_MovePos[m_CurrentIdx].x + w;
					newPosY = g_MovePos[m_CurrentIdx].y;
				}
				else if(current_dir==TOP_RIGHT)
				{
					newPosX = g_MovePos[m_CurrentIdx].x + w/2;
					newPosY = g_MovePos[m_CurrentIdx].y + h;
				}
				else if(current_dir==BOT_RIGHT)
				{
					newPosX = g_MovePos[m_CurrentIdx].x + w/2;
					newPosY = g_MovePos[m_CurrentIdx].y - h;
				}
			}
			else
			{
				newPosX = g_MovePos[m_NextIdx].x;
				newPosY = g_MovePos[m_NextIdx].y;
			}
			float deltaX = (newPosX - g_MovePos[m_CurrentIdx].x)/50;
			float A = -1;
			//Ax + By = 0
			//A*(x-g_MovePos[m_CurrentIdx].x) + B*(y - g_MovePos[m_CurrentIdx].y) = 0
			//A*(g_MovePos[m_CurrentIdx].x - g_MovePos[nextIdx].x) + B*(g_MovePos[m_CurrentIdx].y - g_MovePos[nextIdx].y) = 0
			if(g_MovePos[m_CurrentIdx].y - newPosY != 0)
				A = -(g_MovePos[m_CurrentIdx].y - newPosY)/(g_MovePos[m_CurrentIdx].x - newPosX);
			float posX = g_MovePos[m_CurrentIdx].x + deltaX*m_MoveStep;
			float posY = (A == -1)?g_MovePos[m_CurrentIdx].y:(-A*(posX-g_MovePos[m_CurrentIdx].x) + g_MovePos[m_CurrentIdx].y);
			Assets.Ga_skeleton.setPosition(posX, posY - h/4);
			m_MoveStep++;
		}
		if(m_MoveStep == 51)
		{
			m_Moving = false;
			m_MoveStep = 1;
			m_CurrentIdx = m_NextIdx;
			if(m_NextIdx == -1)
			{
				m_showFadeOut = true;
				Assets.FadeOut_skeleton.setPosition(Assets.Ga_skeleton.getX(), Assets.Ga_skeleton.getY());
				m_current = TimeUtils.millis();
				m_timer = Assets.FadeOut_skeleton.getData().findAnimation("animation").getDuration();
			}
		}
	}
	
	private int current_dir = 0;
	private int last_dir = RIGHT;
	public int PathFinding(int idx)
	{
//		if(true)
//			return -2;
//		int Move = FindWithDir(idx, LEFT, 1);
//		int nextIdx = g_MoveActor[idx].m_link[LEFT];
//		for(int dir = TOP_LEFT; dir <= BOT_RIGHT; dir++)
//		{
//			int newMove = FindWithDir(idx, dir, 1);
//			if(newMove < Move)
//			{
//				Move = newMove;
//				nextIdx = g_MoveActor[idx].m_link[dir];
//			}
//		}
		int curDir = LEFT;
		int curMove = -1;
		int maxMove = -1;
		for(int dir = LEFT; dir <= BOT_RIGHT; dir++)
		{
			MaxMove[dir] = FindWithDir(idx, dir, 1);
			MoveId[dir] = g_MoveActor[idx].m_link[dir];
			if(dir == LEFT)
			{
				curMove = MaxMove[dir];
				maxMove = MaxMove[dir];
			}

			if(dir > LEFT)
			{
				if(MaxMove[dir] < curMove)
				{
					curMove = MaxMove[dir];
					curDir = dir;
				}
				else if(maxMove < MaxMove[dir])
				{
					maxMove = MaxMove[dir];
				}
			}
		}
		
		//check again
		if(curMove >= MAX_MOVE)
		{
			int newMove = MAX_MOVE;
			for(int dir = LEFT; dir <= BOT_RIGHT; dir++)
			{
				int dis = MaxMove[dir] - MAX_MOVE;
				if(dis>=2)
				{
					for(int deep = 0; deep<dis-1; deep++)
					{
						int linkIdx = GetLinkIdx(idx, dir, deep);
						for(int subdir = LEFT; subdir <= BOT_RIGHT; subdir++)
						{
							int Moves = FindWithDir(linkIdx, subdir, 1);
							if(Moves<newMove)
							{
								newMove = Moves;
								curDir = dir;
							}
						}
					}
				}
			}
//			if(newMove==MAX_MOVE)
//				return -1;
		}

		if(MaxMove[curDir] == MAX_MOVE)
		{
			for(int dir = LEFT; dir <= BOT_RIGHT; dir++)
			{
				if(MaxMove[dir]>MAX_MOVE)
				{
					curDir = dir;
					curMove = MaxMove[dir];
					break;
				}
			}
		}
		//try to run
		if(curMove >= MAX_MOVE)
		{
			for(int dir = LEFT; dir <= BOT_RIGHT; dir++)
			{
				if(MaxMove[dir]>curMove)
				{
					curDir = dir;
					curMove = MaxMove[dir];
					break;
				}
			}
		}
		if(curMove == MAX_MOVE)
			return -2;
		current_dir = curDir;
		return MoveId[curDir];
	}
	
	public int GetLinkIdx(int idx, int dir, int deep)
	{
		int ret = g_MoveActor[idx].m_link[dir];
		for(int i = 0; i<deep; i++)
		{
			ret = g_MoveActor[ret].m_link[dir];
		}
		return ret;
	}
	public int SubFinding(int idx, int dir, int deep)
	{
		int MoveDir0 = FindWithDir(idx, m_SubFindDir[dir][0], deep-1);
		int MoveDir1 = FindWithDir(idx, m_SubFindDir[dir][1], deep-1);
		if(MoveDir0>MoveDir1)
			return MoveDir1;
		else
			return MoveDir0;
	}
	
	public int FindWithDir(int idx, int dir, int deep)
	{
		int nextIdx = g_MoveActor[idx].m_link[dir];
		if(nextIdx==-1)
			return 0;
		if(g_MoveActor[nextIdx].isBlock())
		{
			if(idx == m_CurrentIdx || deep == 0)
				return MAX_MOVE;
			else if(deep > 0)
				return SubFinding(idx, dir, deep);
		}
		return 1 + FindWithDir(nextIdx, dir, deep);
	}
	
	private float rewardPosX = -1;
	private float rewardPosY = -1;
	public void drawReward(PolygonSpriteBatch batch)
	{
		if(rewardPosX != -1)
		{
//			System.out.println("dui ga");
			Vector2 offset = new Vector2(), size = new Vector2();
			Assets.Boy_skeleton.getBounds(offset, size);
			float targetX = scr_w - size.x;
			float targetY = size.y / 2;
			
			float deltaX = (targetX - rewardPosX)/50;
			float A = -1;
			if(rewardPosY - targetY != 0)
				A = -(rewardPosY - targetY)/(rewardPosX - targetX);
			float posX = rewardPosX + deltaX*m_MoveStep;
			float posY = (A == -1)?rewardPosY:(-A*(posX-rewardPosX) + rewardPosY);
			batch.begin();
			batch.draw(Assets.DuiGaTexture, 
					posX - Assets.DuiGaTexture.getRegionWidth()*scale_ratio/2,
					posY - Assets.DuiGaTexture.getRegionHeight()*scale_ratio/2,
					Assets.DuiGaTexture.getRegionWidth()*scale_ratio,
					Assets.DuiGaTexture.getRegionHeight()*scale_ratio);
			batch.end();
		}
//		batch.begin();
//		batch.draw(Assets.DuiGaTexture, 
//				g_MovePos[m_CurrentIdx].x - Assets.DuiGaTexture.getRegionWidth()*scale_ratio/2,
//				g_MovePos[m_CurrentIdx].y - Assets.DuiGaTexture.getRegionHeight()*scale_ratio/2,
//				Assets.DuiGaTexture.getRegionWidth()*scale_ratio,
//				Assets.DuiGaTexture.getRegionHeight()*scale_ratio);
//		batch.end();
	}
	
	public boolean updateReward()
	{
		if(game_state == Gamedef.STATE_LOSE)
			return false;
		if(rewardPosX != -1)
		{
			m_MoveStep++;
			if(m_MoveStep == 51)
			{
				m_MoveStep = 0;
				rewardPosX = -1;
				rewardPosY = -1;
				return false;
			}
			return true;
		}
		return false;
	}
	private int game_counter = 0;
	public void render()
	{
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		switch(game_state)
		{
			case Gamedef.STATE_SPLASH:
			{
				batch.begin();
				
				//Draw BG
				batch.draw(Assets.SplashBGTexture,
							0,
							0,
							scr_w,
							scr_h);

				renderer.draw(batch, Assets.GaS_skeleton);
				renderer.draw(batch, Assets.BoyS_skeleton);

				batch.end();
				stage.draw();
				break;
			}
			case Gamedef.STATE_TUTORIAL:
			{
				batch.begin();
				batch.draw(Assets.TutorTexture, 
						0,
						0,
						scr_w,
						scr_h);
				if(game_counter%100<50) {
					layout.setText(Bmfont, "Tap to continue");
					Bmfont.draw(batch, layout, scr_w / 2 - layout.width / 2, 200 * scale_ratio);
				}
				batch.end();
				break;
			}
			case Gamedef.STATE_WIN:
			case Gamedef.STATE_LOSE:
			case Gamedef.STATE_GAMEPLAY:
			{
				batch.begin();
				
				//Draw BG
				batch.draw(Assets.BGTexture, 
							(scr_w - Assets.BGTexture.getRegionWidth()*scale_ratio)/2,
							0,
							Assets.BGTexture.getRegionWidth()*scale_ratio,
							Assets.BGTexture.getRegionHeight()*scale_ratio);
				batch.draw(Assets.BG2Texture, 
						(scr_w - Assets.BG2Texture.getRegionWidth()*scale_ratio)/2,
						0,
						Assets.BG2Texture.getRegionWidth()*scale_ratio,
						Assets.BG2Texture.getRegionHeight()*scale_ratio);
				
				for(int i=0; i<MaxBoardIdx;i++)
				{
					if(g_MoveActor[i] != null)
						g_MoveActor[i].draw(batch, renderer);
				}

				layout.setText(Bmfont, "Move(s): " + numMoves);
				Bmfont.draw(batch, layout, scr_w - 20*scale_ratio - layout.width, scr_h - 20*scale_ratio);
				batch.end();
				
//				if(BtnReset!=null && BtnReset.isVisible())

				
				batch.begin();
//				BoxAlign.draw(batch);
				if(m_showFadeOut)
					renderer.draw(batch, Assets.FadeOut_skeleton);
				if(!m_showFadeOut && game_state == Gamedef.STATE_GAMEPLAY)
					renderer.draw(batch, Assets.Ga_skeleton);
				
				renderer.draw(batch, Assets.Boy_skeleton);
				
				batch.draw(Assets.LvTexture, 
						20*scale_ratio,
						20*scale_ratio,
						Assets.LvTexture.getRegionWidth()*scale_ratio,
						Assets.LvTexture.getRegionHeight()*scale_ratio);
				
				batch.end();
				drawReward(batch);
				stage.draw();
				break;
			}
		}
	}
}
