package com.catchchicken.game;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdsManager {
	
	private AdView adView;
    
    /** The interstitial ad. */
    private InterstitialAd interstitialAd;
    
    private int adHeight = 0;
    
    public int getAdHeight() {
        return adHeight;
    }
    
    public View getAdView(){
    	return adView;
    }
    
	public void addAdsView(Activity activity, RelativeLayout layout,
						  RelativeLayout.LayoutParams params) {
        int screenLayout = activity.getResources().getConfiguration().screenLayout;
        if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= 4) {
//        	adView = new AdView(activity, AdSize.IAB_LEADERBOARD, activity.getString(R.string.admob_id2));
        	adView = new AdView(activity);
        	adView.setAdSize(AdSize.LEADERBOARD);
        	adView.setAdUnitId(activity.getString(R.string.admob_id2));
            adHeight = 90; // 728x90 size for xlarge screens (>= 960dp x 720dp)
        }
        else if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 3) {
//        	adView = new AdView(activity, AdSize.IAB_BANNER , activity.getString(R.string.admob_id2));
        	adView = new AdView(activity);
        	adView.setAdSize(AdSize.SMART_BANNER);
        	adView.setAdUnitId(activity.getString(R.string.admob_id2));
            adHeight = 75; // 468x60 size for large screens (>= 640dp x 480dp)
        }        
        else {
//        	adView = new AdView(activity, AdSize.BANNER, activity.getString(R.string.admob_id2));
        	adView = new AdView(activity);
        	adView.setAdSize(AdSize.BANNER);
        	adView.setAdUnitId(activity.getString(R.string.admob_id2));
            adHeight = 75; // 320x50 size for normal (>= 470dp x 320dp) and small screens
        }
        layout.addView(adView, params);
//        AdRequest request = new AdRequest();
        AdRequest request = new AdRequest.Builder().build();
//        request.addTestDevice(AdRequest.TEST_EMULATOR);
//        request.addTestDevice("C3747186C4B54043662767055C7B248F");
        adView.loadAd(request);
	}
    
    
	public void addAdsView(Activity activity, LinearLayout layout) {
        int screenLayout = activity.getResources().getConfiguration().screenLayout;
        if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= 4) {
//        	adView = new AdView(activity, AdSize.IAB_LEADERBOARD, activity.getString(R.string.admob_id2));
        	adView = new AdView(activity);
        	adView.setAdSize(AdSize.LEADERBOARD);
        	adView.setAdUnitId(activity.getString(R.string.admob_id2));
            adHeight = 90; // 728x90 size for xlarge screens (>= 960dp x 720dp)
        }
        else if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 3) {
//        	adView = new AdView(activity, AdSize.IAB_BANNER , activity.getString(R.string.admob_id2));
        	adView = new AdView(activity);
        	adView.setAdSize(AdSize.SMART_BANNER);
        	adView.setAdUnitId(activity.getString(R.string.admob_id2));
            adHeight = 75; // 468x60 size for large screens (>= 640dp x 480dp)
        }        
        else {
//        	adView = new AdView(activity, AdSize.BANNER, activity.getString(R.string.admob_id2));
        	adView = new AdView(activity);
        	adView.setAdSize(AdSize.BANNER);
        	adView.setAdUnitId(activity.getString(R.string.admob_id2));
            adHeight = 75; // 320x50 size for normal (>= 470dp x 320dp) and small screens
        }
        layout.addView(adView);
//        AdRequest request = new AdRequest();
        AdRequest request = new AdRequest.Builder().build();
//        request.addTestDevice(AdRequest.TEST_EMULATOR);
//        request.addTestDevice("5EA5B374B0B6F1201A685AACAC300DDD");
//        request.addTestDevice("C3747186C4B54043662767055C7B248F");
        adView.loadAd(request);
	}
	
	public void addInterstitialAdView(Activity activity, RelativeLayout layout,
				  RelativeLayout.LayoutParams params) {
//		int screenLayout = activity.getResources().getConfiguration().screenLayout;
//		interstitialAd = new InterstitialAd(activity, activity.getString(R.string.admob_id2));
		interstitialAd = new InterstitialAd(activity);
		interstitialAd.setAdUnitId(activity.getString(R.string.admob_id3));
//		request.addTestDevice(AdRequest.TEST_EMULATOR);
//		request.addTestDevice("C3747186C4B54043662767055C7B248F");
		loadInterstitialAd();
		interstitialAd.setAdListener(new AdListener()
        {  				
			@Override
			 public void onAdLoaded(){
				Log.d("ADS MANAGER","----------------onAdLoaded");
			}
			
			@Override
			  public void onAdFailedToLoad(int errorCode){
				Log.d("ADS MANAGER","----------------onAdFailedToLoad");
			}
			
			@Override
			  public void onAdOpened(){
				Log.d("ADS MANAGER","----------------onAdOpened");
			}
			
			@Override
			  public void onAdClosed(){
				loadInterstitialAd();
				Log.d("ADS MANAGER","----------------onAdClosed");
			}
			
			@Override
			  public void onAdLeftApplication(){
				Log.d("ADS MANAGER","----------------onAdLeftApplication");
			}
		});				
	}
	
    public void removeAdView(RelativeLayout layout) {
        if (adView != null) {
            layout.removeView(adView);
            adHeight = 0;
            adView = null;
        }
    }
	
	public void loadInterstitialAd(){
		AdRequest request = new AdRequest.Builder().build();
		interstitialAd.loadAd(request);		
	}

	public void showInterstitialAd(boolean isShow) {
//		interstitialAd = new InterstitialAd(activity, activity.getString(R.string.admob_id2));
//		AdRequest request = new AdRequest();
//		interstitialAd.loadAd(request);
		if (interstitialAd.isLoaded() && isShow) {
			interstitialAd.show();
		}
	}
	
	public void showAdd(boolean show) {
		if (show) {
			adView.setVisibility(View.VISIBLE);
		}
		else {
			adView.setVisibility(View.GONE);
		}
	}
	
	public void onResume() {
	    if (adView != null) {
	      adView.resume();
	    }
	}

	public void onPause() {
		if (adView != null) {
	      adView.pause();
	    }
	}

	/** Called before the activity is destroyed. */
	public void onDestroy() {
		// Destroy the AdView.
	  if (adView != null) {
		  adView.destroy();
	  }
	}
}
