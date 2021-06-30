package com.heon9u.alarm_weather_app.anotherTools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.heon9u.alarm_weather_app.R;

public class AdBannerClass {

    Context context;
    Display display;
    public AdView adView;

    public AdBannerClass(Context context, Display display) {
        this.context = context;
        this.display = display;
        adView = new AdView(context);
        adView.setAdUnitId(context.getString(R.string.ad_banner));
        loadBanner();
    }

    public void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    public AdSize getAdSize() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }
}
