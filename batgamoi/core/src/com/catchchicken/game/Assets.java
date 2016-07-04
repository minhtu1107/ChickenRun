package com.catchchicken.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

public class Assets {

	//BG
	public static TextureAtlas BG_atlas;
	public static TextureRegion BGTexture;
	public static TextureAtlas BG2_atlas;
	public static TextureRegion BG2Texture;
	//Ga
//	public static TextureAtlas Ga_atlas;
	public static Skeleton Ga_skeleton;
	public static AnimationState Ga_state;
	
	//box
	public static TextureAtlas Box_atlas;
	public static TextureRegion BoxTexture;
	public static TextureRegion DaTexture;
	public static TextureRegion CayTexture;
	public static TextureRegion DuiGaTexture;
	
	//effect
	public static Skeleton FadeIn_skeleton;
	public static AnimationState FadeIn_state;
	public static Skeleton FadeOut_skeleton;
	public static AnimationState FadeOut_state;
	
	public static Skeleton Boy_skeleton;
	public static AnimationState Boy_state;
	
	//splash - tutorial
	public static TextureAtlas Splash_atlas;
	public static TextureRegion SplashTexture;
	public static TextureRegion SplashBGTexture;
	
	public static TextureAtlas Tutor_atlas;
	public static TextureRegion TutorTexture;
	
	public static TextureAtlas UI_atlas;
	public static TextureRegion SoundTexture;
	public static TextureRegion MusicTexture;
	public static TextureRegion LvTexture;

	public static TextureAtlas Congrat_atlas;
	public static TextureAtlas Fade_atlas;

	public static void LoadSplash()
	{
		Splash_atlas = new TextureAtlas(Gdx.files.internal("data/splash.atlas"));
		SplashTexture = Splash_atlas.findRegion("splash");
		SplashBGTexture = Splash_atlas.findRegion("splashbg");
		
		Tutor_atlas = new TextureAtlas(Gdx.files.internal("data/Huongdan.atlas"));
		TutorTexture = Tutor_atlas.findRegion("Huongdan");
	}
	
	public static void LoadCommon()
	{
		LoadSplash();
		
		BG_atlas = new TextureAtlas(Gdx.files.internal("data/BG.atlas"));
		BGTexture = BG_atlas.findRegion("BG");
		
		BG2_atlas = new TextureAtlas(Gdx.files.internal("data/BG2.atlas"));
		BG2Texture = BG2_atlas.findRegion("BG2");

		UI_atlas = new TextureAtlas(Gdx.files.internal("data/UI/UI.atlas"));
		Congrat_atlas = new TextureAtlas(Gdx.files.internal("data/UI/Congrat.atlas"));
		Fade_atlas = new TextureAtlas(Gdx.files.internal("data/UI/fade.atlas"));

		TextureAtlas Ga_atlas = new TextureAtlas(Gdx.files.internal("data/Ga/Ga.atlas"));
		SkeletonJson json = new SkeletonJson(Ga_atlas);
		json.setScale(BoardScreen.scale_ratio);
		SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("data/Ga/Ga.json"));
		Ga_skeleton = new Skeleton(skeletonData);
		Ga_skeleton.setPosition(250, 20);
		AnimationStateData stateData = new AnimationStateData(skeletonData);
		Ga_state = new AnimationState(stateData);
		Ga_state.setAnimation(0, "chay", true);
		
		Box_atlas = new TextureAtlas(Gdx.files.internal("data/box.atlas"));
		BoxTexture = Box_atlas.findRegion("box");
		DaTexture = Box_atlas.findRegion("Da");
		CayTexture = Box_atlas.findRegion("Cay");
		DuiGaTexture = Box_atlas.findRegion("Duiga");
		
		TextureAtlas Fade_atlas = new TextureAtlas(Gdx.files.internal("data/Effect/Bienmat/Bien mat.atlas"));
		json = new SkeletonJson(Fade_atlas);
		json.setScale(BoardScreen.scale_ratio);
		skeletonData = json.readSkeletonData(Gdx.files.internal("data/Effect/Bienmat/Bien mat.json"));
		FadeOut_skeleton = new Skeleton(skeletonData);
		FadeOut_skeleton.setPosition(250, 20);
		stateData = new AnimationStateData(skeletonData);
		FadeOut_state = new AnimationState(stateData);
		FadeOut_state.setAnimation(0, "animation", true);
		
		Fade_atlas = new TextureAtlas(Gdx.files.internal("data/Effect/Xuathien/Xuat hien.atlas"));
		json = new SkeletonJson(Fade_atlas);
		json.setScale(BoardScreen.scale_ratio);
		skeletonData = json.readSkeletonData(Gdx.files.internal("data/Effect/Xuathien/Xuat hien.json"));
		FadeIn_skeleton = new Skeleton(skeletonData);
		FadeIn_skeleton.setPosition(500, 20);
		stateData = new AnimationStateData(skeletonData);
		FadeIn_state = new AnimationState(stateData);
		FadeIn_state.setAnimation(0, "animation", true);
		
		Fade_atlas = new TextureAtlas(Gdx.files.internal("data/Boy/skeleton.atlas"));
		json = new SkeletonJson(Fade_atlas);
		json.setScale(0.6f*BoardScreen.scale_ratio);
		skeletonData = json.readSkeletonData(Gdx.files.internal("data/Boy/skeleton.json"));
//		System.out.println("path" + skeletonData.getImagesPath());
		Boy_skeleton = new Skeleton(skeletonData);
		Boy_skeleton.setPosition(500, 20);
		stateData = new AnimationStateData(skeletonData);
		Boy_state = new AnimationState(stateData);
//		Boy_state.setAnimation(0, "animation", true);
		
		SoundEffect.hit.sound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/hit.ogg"));
		SoundEffect.lose.sound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/lose.ogg"));
		SoundEffect.win.sound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/win.ogg"));
		SoundEffect.BGM.sound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/BGM.ogg"));
	}
	
	enum SoundEffect {
		BGM, win, lose, hit;

		Sound sound;
		float volume = 1;

		void play () {
			sound.play(volume);
		}
		void loop () {
			sound.loop(volume);
		}
		void stop () {
			sound.stop();
		}
	}

	public static void ReleaseAll()
	{
		//BG
		BG_atlas.dispose();
		BGTexture = null;
		BG2_atlas.dispose();
		BG2Texture = null;
		//Ga
//	public static TextureAtlas Ga_atlas;
		Ga_skeleton = null;
		Ga_state = null;

		//box
		Box_atlas.dispose();
		BoxTexture = null;
		DaTexture =	null;
		CayTexture = null;
		DuiGaTexture = null;

		//effect
		FadeIn_skeleton = null;
		FadeIn_state = null;
		FadeOut_skeleton = null;
		FadeOut_state = null;

		Boy_skeleton = null;
		Boy_state = null;

		//splash - tutorial
		Splash_atlas.dispose();
		SplashTexture = null;
		SplashBGTexture = null;

		Tutor_atlas.dispose();
		TutorTexture = null;

		UI_atlas.dispose();
		SoundTexture = null;
		MusicTexture = null;
		LvTexture = null;

		Congrat_atlas.dispose();
		Fade_atlas.dispose();
	}
}
