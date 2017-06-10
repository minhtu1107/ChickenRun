package com.catchchicken.game;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication implements ActionResolver {
	
//	private FacebookManager fbManager;
	private AdsManager adsManager;
	RelativeLayout layout;
	CatchChickenGame mygame;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		initialize(new CatchChickenGame(), config);
        // Create the layout
        layout = new RelativeLayout(this);
        mygame = new CatchChickenGame();
		mygame.setActionResolver(this);
        View gameView = initializeForView(mygame, config);
        
        ///////Add Admob
        adsManager = new AdsManager();

        layout.addView(gameView);

        RelativeLayout.LayoutParams adParams = 
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        adsManager.addAdsView(this, layout, adParams);
        adsManager.addInterstitialAdView(this, layout, adParams);
        setContentView(layout);
		showAds(false);
//		fbManager = new FacebookManager(mygame);
	}

	public int counter = 0;
	public int rate_counter = 0;
	@Override
	public void showAds(final boolean isShow) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			public void run() {
				if(isShow) {
					counter++;
					rate_counter++;
				}
				if(counter<5) {
					adsManager.showAdd(isShow);
				}
				else {
					adsManager.showInterstitialAd(isShow);
					counter = 0;
				}
				if(rate_counter==8)
				{
					RateGame();
				}
				else
				{
					rate_counter=0;
				}
			}
		});
	}
	
    @Override
    public void onResume() {
    	super.onResume();
    	adsManager.onResume();
    }

    @Override
    public void onPause() {
    	adsManager.onPause();
    	super.onPause();
    }

    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() {
    	// Destroy the AdView.
    	adsManager.onDestroy();
    	super.onDestroy();
    }
	@Override
	public void LoginFB(String shareMessage) {
		// TODO Auto-generated method stub
//		fbManager.SignIn(shareMessage);
	}

	@Override
	public void onBackPressed() {
		// your code.
		mygame.SaveGame();
		super.onBackPressed();
	}

	public void RateGame()
	{
		final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
		}
	}
}
