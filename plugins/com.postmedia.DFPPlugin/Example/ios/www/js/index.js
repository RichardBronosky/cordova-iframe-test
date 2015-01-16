/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicity call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        app.debugMode();
        app.removeAd();
        app.addBanner();
        
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');
        var addBannerButton = document.getElementById('addbanner');
        var addInterstitialButton = document.getElementById('addintersitial');
        var removeAdButton = document.getElementById('removead');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
        
        addBannerButton.addEventListener('click', function(e) {
            app.addBanner();
        });
        
        addInterstitialButton.addEventListener('click', function(e) {
            app.addInterstitial();
        });
        
        removeAdButton.addEventListener('click', function(e) {
            app.removeAd();
        });
    },
    addBanner: function() {
        var successCreateBannerView = function() {
            console.log("addBanner Success");
            DFPPlugin.requestAd({
                'isTesting': true
            }, success, error);
        };
        
        var success = function() {
            console.log("requestAd Success");
        };
        var error = function(message) {
            console.log("requestAd Failed " + message);
        };
        
        var options = {
            //'adUnitId': '/6253334/dfp_example_ad/banner',
            'adUnitId': '/3081/ch.ip/news/index',
            'adSize': 'BANNER',
            'tags': {
                'pr': 'ch',
                'ck': 'news',
                'imp': 'news',
                'loc': 'top',
                'nk': 'print',
                'page': 'index'
            }
        };

        DFPPlugin.createBannerAd(options, successCreateBannerView, error);
    },
    
    addInterstitial: function() {
        var successCreateInterstitialView = function() { console.log("addInterstitial Success"); DFPPlugin.requestAd({'isTesting': false}, success, error); };
        var success = function() { console.log("requestAd Success"); };
        var error = function(message) { console.log("Oopsie! " + message); };
    
        var options = {
            'adUnitId': '/6253334/dfp_example_ad/interstitial'
            //'adUnitId': '/3081/oc.ip/news/index'
        }
        DFPPlugin.createInterstitialAd(options, successCreateInterstitialView, error);
    },
    
    removeAd: function() {
        var successRemoveAd = function() { console.log("remove ad Success"); };

        var error = function(message) { console.log("remove ad failed! " + message); };
        
        var options = {};
        
        DFPPlugin.removeAd();
    },
    
    debugMode: function() {
        var successDebug = function() { console.log("debug Success"); };
        
        var error = function(message) { console.log("debug failed! " + message); };
        
        DFPPlugin.debugMode({'debug': true}, successDebug, error);
    }
};
