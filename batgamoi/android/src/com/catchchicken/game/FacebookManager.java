package com.catchchicken.game;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Array;
import com.facebook.share.widget.ShareDialog;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;
import de.tomgrill.gdxfacebook.core.JsonResult;
import de.tomgrill.gdxfacebook.core.SignInMode;
import de.tomgrill.gdxfacebook.core.SignInResult;

public class FacebookManager {
//	static FacebookManager m_instance;
	GDXFacebook facebook;
	CatchChickenGame mygame;

	FacebookManager(CatchChickenGame game)
	{
		GDXFacebookConfig config = new GDXFacebookConfig();
		config.APP_ID = "134507623619953"; // required
		config.PREF_FILENAME = ".facebookSessionData"; // optional
		config.GRAPH_API_VERSION = "v2.5"; // optional, default is v2.5
		
		facebook = GDXFacebookSystem.install(config);
		mygame = game;
	}
	
//	public static FacebookManager getInstance()
//	{
//		if(m_instance == null)
//		{
//			m_instance = new FacebookManager();
//		}
//		return m_instance;
//	}

	public void SetGameInstance(CatchChickenGame ins)
	{
		mygame = ins;
	}

	public void SignIn(final String shareMessage)
	{
		Array<String> permissions = new Array<String>();
//		permissions.add("email");
//		permissions.add("public_profile");
//		permissions.add("user_friends");
		permissions.add("publish_actions");

		facebook.signIn(SignInMode.PUBLISH, permissions, new GDXFacebookCallback<SignInResult>() {
		    @Override
		    public void onSuccess(SignInResult result) {
		        // Login successful
				PostToWall(shareMessage);
				System.out.println("FacebookManager ============================= success");
		    }

		    @Override
		    public void onError(GDXFacebookError error) {
		        // Error handling
				System.out.println("FacebookManager ============================= onError");
				//mygame.GameShareFBReset(false);
		    }

		    @Override
		    public void onCancel() {
		        // When the user cancels the login process
		    }

		    @Override
		    public void onFail(Throwable t) {
		        // When the login fails
				//mygame.GameShareFBReset(false);
		    }
		});
	}

	public void PostToWall(String shareMessage)
	{
		GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me/feed").useCurrentAccessToken();
		request.setMethod(Net.HttpMethods.POST);
		request.putField("message", shareMessage);
//		request.putField("link", "https://github.com/TomGrill/gdx-facebook");
//		request.putField("caption", "gdx-facebook");

		facebook.newGraphRequest(request, new GDXFacebookCallback<JsonResult>() {

			@Override
			public void onSuccess(JsonResult result) {
				// Success
				System.out.println("FacebookManager ============================= PostToWall Success");
				mygame.GameShareFBReset(true);
			}

			@Override
			public void onError(GDXFacebookError error) {
				// Error
				System.out.println("FacebookManager ============================= PostToWall Error");
				System.out.println("FacebookManager " + error.getErrorMessage());
				//mygame.GameShareFBReset(false);
			}

			@Override
			public void onFail(Throwable t) {
				// Fail
				System.out.println("FacebookManager ============================= PostToWall Fail");
				//mygame.GameShareFBReset(false);
			}

			@Override
			public void onCancel() {
				// Cancel
			}

		});
	}
}
