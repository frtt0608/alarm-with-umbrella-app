package com.heon9u.alarm_weather_app.alarm;

import android.content.Context;
import android.content.Intent;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.heon9u.alarm_weather_app.dto.Alarm;
import com.heon9u.alarm_weather_app.location.LocationListView;
import com.heon9u.alarm_weather_app.R;
import com.heon9u.alarm_weather_app.databinding.AlarmFragmentBinding;

import java.util.List;

public class AlarmListView extends Fragment {

    private AlarmViewModel alarmViewModel;
    public AlarmFragmentBinding alarmFragmentBinding;
    Context context;
    RecyclerView recyclerView;
    AlarmAdapter alarmAdapter;
    UnifiedNativeAd nativeAd;
    CardView adContainer;
    TextView noAlarmText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        initAdMob();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // RecyclerView로 alarm 리스트 페이지 호출
        alarmFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.alarm_fragment, container, false);
        alarmFragmentBinding.setAlarmClick(this);
        View view = alarmFragmentBinding.getRoot();

        adContainer = view.findViewById(R.id.adContainer);
        noAlarmText = view.findViewById(R.id.noAlarmText);

        alarmAdapter = new AlarmAdapter();

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(alarmAdapter);

        attachItemTouchHelperToAdapter();

        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        alarmViewModel.getAllAlarms().observe(getViewLifecycleOwner(), new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                alarmAdapter.submitList(alarms);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void createAlarmClick(View view) {
        Intent createAlarmIntent = new Intent(getActivity(), AlarmSetActivity.class);
        createAlarmIntent.putExtra("REQUEST_STATE", "create");
        startActivity(createAlarmIntent);
    }

    public void manageLocationClick(View view) {
        Intent menuLocationIntent = new Intent(getActivity(), LocationListView.class);
        startActivity(menuLocationIntent);
    }

    public void attachItemTouchHelperToAdapter() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                alarmViewModel.delete(alarmAdapter.getAlarmAt(viewHolder.getBindingAdapterPosition()));
                Toast.makeText(getContext(), "알람을 삭제했습니다.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
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
