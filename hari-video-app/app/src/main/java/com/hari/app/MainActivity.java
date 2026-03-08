package com.hari.app;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    SwipeRefreshLayout swipe;
    AdView adView;
    InterstitialAd videoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this);

        webView = findViewById(R.id.webview);
        swipe = findViewById(R.id.swipe);
        adView = findViewById(R.id.adView);

        AdRequest bannerRequest = new AdRequest.Builder().build();
        adView.loadAd(bannerRequest);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        webView.loadUrl("https://visual-melody.lovable.app/");

        swipe.setOnRefreshListener(() -> {
            webView.reload();
            swipe.setRefreshing(false);
        });

        loadVideoAd();
    }

    private void loadVideoAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,
            "ca-app-pub-3513090768952782/INTERSTITIAL_ID",
            adRequest,
            new InterstitialAdLoadCallback(){
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd){
                    videoAd = interstitialAd;
                }
            });
    }

    public void showVideoAd(){
        if(videoAd != null){
            videoAd.show(this);
            loadVideoAd(); // reload for next ad
        }
    }

    public boolean videoAdLoaded(){
        return videoAd != null;
    }
}
