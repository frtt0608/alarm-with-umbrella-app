package com.heon9u.alarm_weather_app.Alarm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.Location.LocationListView;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class AlarmListView extends Fragment implements View.OnClickListener {

    Context context;
    RecyclerView recyclerView;
    AppCompatImageButton createAlarm, manageLocation;
    AlarmAdapter alarmAdapter;
    AlarmDatabase alarmDB;
    ArrayList<Alarm> alarmList;
    UnifiedNativeAd nativeAd;
    CardView adContainer;
    TextView noAlarmText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // RecyclerView로 alarm 리스트 페이지 호출
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        adContainer = view.findViewById(R.id.adContainer);
        noAlarmText = view.findViewById(R.id.noAlarmText);
        context = getContext();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // alarm 생성 버튼
        createAlarm = view.findViewById(R.id.createAlarm);
        createAlarm.setOnClickListener(this);
        manageLocation = view.findViewById(R.id.manageLocation);
        manageLocation.setOnClickListener(this);

        alarmDB = new AlarmDatabase(getContext());
        takeAdapter();
        initAdMob();

        return view;
    }

    public void takeAdapter() {
        displayAlarm();
        alarmAdapter = new AlarmAdapter(getActivity(), alarmList);
        recyclerView.setAdapter(alarmAdapter);
    }

    void displayAlarm() {
        alarmList = alarmDB.readAllAlarm();

        if(alarmList.size() == 0) {
            noAlarmText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noAlarmText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        alarmDB.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        takeAdapter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.createAlarm:
                Intent createAlarmIntent = new Intent(getActivity(), AlarmSetActivity.class);
                createAlarmIntent.putExtra("REQUEST_STATE", "create");
                startActivity(createAlarmIntent);
                break;
            case R.id.manageLocation:
                Intent menuLocationIntent = new Intent(getActivity(), LocationListView.class);
                startActivity(menuLocationIntent);
                break;
        }
    }

    public void initAdMob() {
        MobileAds.initialize(getContext(), initializationStatus -> { });

        AdLoader.Builder builder = new AdLoader.Builder(getContext(), getString(R.string.ad_native));
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

    @Override
    public void onDestroy() {
        if(nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }
}
