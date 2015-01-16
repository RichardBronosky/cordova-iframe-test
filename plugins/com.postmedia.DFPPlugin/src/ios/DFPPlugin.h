//
//  DFPPlugin.h
//  DFPPlugin
//
//  Created by Donnie Marges on 1/29/2014.
//
//

#import <Cordova/CDV.h>
#import "DFPBannerView.h"
#import "DFPInterstitial.h"
#import "GADInterstitialDelegate.h"
#import "GADAdSizeDelegate.h"
#import "GADAppEventDelegate.h"
#import "GADBannerViewDelegate.h"
#import "DFPExtras.h"
#import <CoreLocation/CoreLocation.h>

@interface DFPPlugin : CDVPlugin <GADBannerViewDelegate, GADAppEventDelegate, GADInterstitialDelegate, UIGestureRecognizerDelegate, CLLocationManagerDelegate>
@property (strong, nonatomic)DFPBannerView *dfpBannerView;
@property (strong, nonatomic)DFPExtras *dfpBannerViewExtras;
@property (strong, nonatomic)DFPInterstitial *dfpInterstitialView;
@property BOOL debugMode;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (strong, nonatomic) CLLocation *currentLocation;

- (void)cordovaCreateBannerAd:(CDVInvokedUrlCommand *)command;
- (void)cordovaCreateInterstitialAd:(CDVInvokedUrlCommand *)command;
- (void)cordovaSetDebugMode:(CDVInvokedUrlCommand *)command;
- (GADAdSize)GADAdSizeFromString:(NSString *)string;
- (void)createBannerAdView:(NSString *)adUnitID adSize:(GADAdSize)adSize;
- (void)createInterstitialAdView:(NSString *)adUnitID;
- (void)resizeViews;
- (void)cordovaRemoveAd: (CDVInvokedUrlCommand *)command;
- (void)adView:(DFPBannerView *)banner didReceiveAppEvent:(NSString *)name withInfo:(NSString *)info;
- (void)interstitial:(DFPInterstitial *)interstitial didReceiveAppEvent:(NSString *)name withInfo:(NSString *)info;
- (void)interstitialDidReceiveAd:(DFPInterstitial *)interstitial;
- (void)interstitial:(DFPInterstitial *)interstitial didFailToReceiveAdWithError:(GADRequestError *)error;
- (void)adViewWillPresentScreen:(GADBannerView *)adView;
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation;
- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error;
- (void)dealloc;

@end
