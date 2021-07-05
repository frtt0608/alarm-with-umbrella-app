package com.heon9u.alarm_weather_app.anotherTools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.heon9u.alarm_weather_app.R;

public class AdNativeClass {
    Context context;
    public UnifiedNativeAd nativeAd;

    public AdNativeClass(Context context) {
        this.context = context;
    }

    public void initAdMob() {
        MobileAds.initialize(context, initializationStatus -> { });
    }

    public void setNativeAdMob(CardView adContainer) {

        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(R.string.ad_native));
        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            if(nativeAd != null)
                nativeAd = unifiedNativeAd;

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            UnifiedNativeAdView adView = (UnifiedNativeAdView) inflater
                    .inflate(R.layout.native_ad_layout, null);

            populateNativeAd(unifiedNativeAd, adView);
            adContainer.addView(adView);
        });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void populateNativeAd(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        adView.setIconView(adView.findViewById(R.id.adIcon));
        adView.setHeadlineView(adView.findViewById(R.id.adHeadLine));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if(nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }
}
