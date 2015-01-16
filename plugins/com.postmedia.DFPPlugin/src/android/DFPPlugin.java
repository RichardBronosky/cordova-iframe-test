/**
 * Plugin to ser DFP Ads on mobile devices
 * 
 * author: waheed ashraf
 * Created on: March 3rd, 2014
 * 
 */

package com.postmedia.DFPPlugin;

import com.google.android.gms.ads.*;
import com.google.android.gms.ads.doubleclick.*;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.google.android.gms.ads.AdListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

/**
 * This class represents the native implementation for the DFPAds Cordova
 * plugin. This plugin can be used to request DFP ads natively via the Google
 * DFP SDK. The Google DFP SDK is a dependency for this plugin.
 */
public class DFPPlugin extends CordovaPlugin {
	private PublisherAdView adView;
	private RelativeLayout adViewLayer;
	public static boolean debug;
	private AdSize adSize;
	private int backgroundColor;
	private String publisherId;
	private JSONObject extras;
	InterstitialAd interstitial;
	
	/**
	 * This is the main method for the DFPAds plugin. All API calls go through
	 * here. This method determines the action, and executes the appropriate
	 * call.
	 * 
	 * @param action
	 *            The action that the plugin should execute.
	 * @param inputs
	 *            The input parameters for the action.
	 * @param callbackId
	 *            The callback ID. This is currently unused.
	 * @return A PluginResult representing the result of the provided action. A
	 *         status of INVALID_ACTION is returned if the action is not
	 *         recognized.
	 */
	@Override
	public boolean execute(String action, final JSONArray inputs, final CallbackContext callbackContext) throws JSONException {
		PluginResult result = null;
		if (action.equals("cordovaCreateBannerAd")) {
			this.cordova.getActivity().runOnUiThread(new Runnable() {
				public void run() {
					callbackContext.sendPluginResult(createBannerAd(inputs));	
				}
			});
			
			result = new PluginResult(Status.OK);
			
		} else if (action.equals("cordovaRemoveAd")) {
			this.cordova.getActivity().runOnUiThread(new Runnable() {
				public void run() {
					removeAd(adView, adViewLayer);
				}
			});
			
			result = new PluginResult(Status.OK);
		} else if (action.equals("cordovaRequestAd")) {
			result = new PluginResult(Status.OK);

		} else if (action.equals("cordovaSetDebugMode")) {
			JSONObject options = inputs.getJSONObject(0);

			this.debug = Boolean.parseBoolean(options.getString("debug"));
			
			result = new PluginResult(Status.OK);
		} else if (action.equals("cordovaCreateInterstitialAd")) {
			this.cordova.getActivity().runOnUiThread(new Runnable() {
				public void run() {
					callbackContext.sendPluginResult(createInterstitialAd(inputs));	
				}
			});
			
			result = new PluginResult(Status.OK);
		} else {
			Log.d("AdDFP", String.format("Invalid action passed: %s", action));
			result = new PluginResult(Status.INVALID_ACTION);
		}
		callbackContext.sendPluginResult(result);

		return true;
	}
	
	private PublisherAdView createPublisherAdView(String pubId, String size, JSONObject extras, int backgroundColor) {
		
		try {
			PublisherAdView adView = new PublisherAdView(this.webView.getContext());
			adView.setAdUnitId(pubId);
			adView.setAdSizes(adSizeFromSize(size));
	
			adView.setBackgroundColor(backgroundColor);
			adView.setAdListener(new BannerListener());
			
			return adView;
		} catch(Exception ex) {
			Log.d("AdView Create Error", "error");
			return null;
		}

	}
	
	private InterstitialAd createInterstitialAdView(String publisherId) {
		
		InterstitialAd interstitial = new InterstitialAd(this.webView.getContext());
		
		try {
			interstitial.setAdUnitId(publisherId);
			interstitial.setAdListener(new InterstitialListener());
			interstitial.loadAd(new AdRequest.Builder().build());
			
			return interstitial;
			
		} catch(Exception ex) {
			Log.d("InterstitialAdView Create Error", "error");
			return null;
		}
	}
	
	PluginResult createBannerAd(JSONArray inputs) {
		
		String publisherId;
		String size;
		int backgroundColor = Color.BLACK;		
		
		try {
			JSONObject options = inputs.getJSONObject(0);
			publisherId = options.getString("adUnitId");
			size = options.getString("adSize");
			extras = options.getJSONObject("tags");
			backgroundColor = Color.parseColor(options.getString("backgroundColor"));
			
			adView = this.createPublisherAdView(publisherId, size, extras, backgroundColor);
			
			adViewLayer = new RelativeLayout(this.webView.getContext());
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			if (adSizeFromSize(size) == AdSize.BANNER) {
				// This line causes some major performance issues on Android
				// keeping here for reference
				//adView.setPadding(0, 1, 0, 0);
				
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			} else {

				int topMargin = (webView.getHeight() / 2) - 400;
				if (topMargin < 200)
					topMargin = 200;
				layoutParams.topMargin = topMargin;
			}
			adViewLayer.addView(adView, layoutParams);
			ViewGroup.LayoutParams outerLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			Bundle bundle = new Bundle();
			
			Iterator<?> keys = extras.keys();

	        while( keys.hasNext() ){
	            String key = (String)keys.next();
	            try {
					bundle.putString(key, extras.getString(key));
				} catch (JSONException e) {
					
				}
	        }

			this.cordova.getActivity().addContentView(adViewLayer, outerLayout);
			
			//Geotargeting for ads.
			String locationProvider = LocationManager.NETWORK_PROVIDER;
			
			this.webView.getContext();
			
			LocationManager locationManager = (LocationManager) this.cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);
			Location location = new Location("");

			// Define a listener that responds to location updates
			LocationListener locationListener = new LocationListener() {
			    public void onLocationChanged(Location localLocation) {
			      // Called when a new location is found by the network location provider.
			    	//location.set(localLocation);
			    }

			    public void onStatusChanged(String provider, int status, Bundle extras) {}

			    public void onProviderEnabled(String provider) {}

			    public void onProviderDisabled(String provider) {}
			  };

			// Register the listener with the Location Manager to receive location updates
			location.setLatitude((locationManager.getLastKnownLocation(locationProvider)).getLatitude());
			location.setLongitude((locationManager.getLastKnownLocation(locationProvider)).getLongitude());
			
			
			//IF YOU WANT TO TEST TORONTO
			//Location location = new Location("");
			//location.setLatitude(43.7001100);
			//location.setLongitude(-79.4163000);
			// Galaxy S4 will not show ads if this is enabled
			//location.setAccuracy(100);
			
			adView.loadAd(new PublisherAdRequest.Builder().addNetworkExtras(new AdMobExtras(bundle)).build());
			
			return new PluginResult(Status.OK);
		} catch (JSONException exception) {
			Log.w("AdDFP", String.format("Got JSON Exception: %s", exception.getMessage()));
			return new PluginResult(Status.JSON_EXCEPTION);
		} catch (NullPointerException nullException) {
			Log.d("DFP error", nullException.toString());
			return new PluginResult(Status.CLASS_NOT_FOUND_EXCEPTION);
		}
	}
	
	PluginResult createInterstitialAd(JSONArray inputs) {
		
		String publisherId;
		
		try {
			JSONObject options = inputs.getJSONObject(0);
			publisherId = options.getString("adUnitId");
			interstitial = this.createInterstitialAdView(publisherId);
			//interstitial.loadAd(new AdRequest.Builder().addTestDevice("0F34AF291E7685B685BAA47508B60A29").build());
			
			return new PluginResult(Status.OK);
			
		} catch (JSONException exception) {
			Log.w("AdDFP", String.format("Got JSON Exception: %s", exception.getMessage()));
			return new PluginResult(Status.JSON_EXCEPTION);
		} catch (NullPointerException nullException) {
			Log.d("DFP error", nullException.toString());
			return new PluginResult(Status.CLASS_NOT_FOUND_EXCEPTION);
		}
	}
	
	PluginResult removeAd(PublisherAdView adView, RelativeLayout layout) {
		if(adView != null && layout != null) {
			layout.removeView(adView);
		}
		
		return new PluginResult(Status.OK);
	}
	
	PluginResult presentDebugInfo(String publisherId, AdSize adSize) {
		
		if(publisherId == null || adSize == null) {
			return new PluginResult(Status.NO_RESULT);

		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this.webView.getContext());
			dialog.setTitle("Ad Debugging");
			StringBuilder message = new StringBuilder();
			message.append(String.format("Ad Unit ID: %s\n", publisherId));
			message.append(String.format("Ad Size:  {%d, %d}\n", adSize.getWidth(), adSize.getHeight()));
			
			Iterator<?> keys = extras.keys();
	        while( keys.hasNext() ){
	            String key = (String)keys.next();
	            try {
	            	message.append(String.format("%s = %s\n", key, extras.getString(key)));
				} catch (JSONException e) {}
	        }
			dialog.setMessage(message);
			dialog.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
				}
			});
			dialog.show();
			
			return new PluginResult(Status.OK);
			
		}
	}
	
	/**
	 * This class implements the DFPAds ad listener events. It forwards the
	 * events to the JavaScript layer. To listen for these events.
	 */
	private class BannerListener extends AdListener {
		@Override
		public void onAdClosed() {
			webView.loadUrl("javascript:cordova.fireDocumentEvent('onAdClosed');");
		}

		@Override
		public void onAdFailedToLoad(int errorCode) {
			webView.loadUrl(String.format("javascript:cordova.fireDocumentEvent('onAdFailedToLoad', { 'error': '%s' });", errorCode));
		}

		@Override
		public void onAdLeftApplication() {
			webView.loadUrl("javascript:cordova.fireDocumentEvent('onAdLeftApplication');");
		}

		@Override
		public void onAdLoaded() {
			webView.loadUrl("javascript:cordova.fireDocumentEvent('onAdLoaded');");
		}

		@Override
		public void onAdOpened() {
			if (DFPPlugin.debug) {
				presentDebugInfo(adView.getAdUnitId(), adView.getAdSize());
			}
			webView.loadUrl("javascript:cordova.fireDocumentEvent('onAdOpened');");
		}
	}
	
	public class InterstitialListener extends AdListener {
		@Override
		public void onAdLoaded() {
			interstitial.show();
		}
	}

	/**
	 * Gets an AdSize object from the string size passed in from JavaScript.
	 * Returns null if an improper string is provided.
	 * 
	 * @param size
	 *            The string size representing an ad format constant.
	 * @return An AdSize object used to create a banner.
	 */
	public static AdSize adSizeFromSize(String size) {
		if ("BANNER".equals(size)) {
			return AdSize.BANNER;
		} else if ("BIGBOX".equals(size)) {
			return new AdSize(300, 250);
		} else {
			return null;
		}
	}

	@Override
	public void onDestroy() {

		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}
}
