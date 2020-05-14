package com.bvinstruments.pandemictracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieDrawable;
import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.DefaultAdListener;
import com.amazon.device.ads.InterstitialAd;
import com.bvinstruments.pandemictracker.databinding.ActivityMainBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.StateAdapter;
import data.RequestedData;
import fragments.StateSheetFragment;
import fragments.StatisticFragment;
import model.DataSet;

public class MainActivity extends AppCompatActivity implements StatisticFragment.StatisticCallback {


    private ActivityMainBinding binding;
    private StateSheetFragment stateSheetFragment;
    private String last_state = "USA";
    private PieChart chart;
    private InterstitialAd ad;
    private ConnectivityManager.NetworkCallback networkCallback;
    private ConnectivityManager manager;
    private static final String APP_KEY = "37dfe3ebc4b24affa4532e9dfd3242e6"; // Sample Application Key. Replace this value with your Application Key.
    private static final String LOG_TAG = "InterstitialAdSample"; // Tag used to prefix all log messages.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initializeAd();
        chart = binding.chart;
        setupChart();
        stateSheetFragment = new StateSheetFragment();
        setupTopBar();
        initializeNetworkCallback();


    }

    @Override
    protected void onStart() {
        super.onStart();

        setNetworkCallback();
        loadState(last_state);
    }

    private void initializeNetworkCallback() {
        networkCallback = new ConnectivityManager.NetworkCallback() {


            @Override
            public void onAvailable(Network network) {

                super.onAvailable(network);

                loadState(last_state);  //exception


            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);

            }
        };
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private void setNetworkCallback() {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {

            manager.registerDefaultNetworkCallback(networkCallback);


        } else {

            NetworkRequest request = new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            manager.registerNetworkCallback(request, networkCallback);

        }

    }


    private void setupChart() {


        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);


        chart.setCenterText(new SpannableString("Pandeim INFO"));

        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener


        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);


    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.searchBox.clearFocus();


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (manager != null && networkCallback != null)
            manager.unregisterNetworkCallback(networkCallback);


    }


    private boolean isInternetConnectionAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();


    }


    public void showMiddleAnimation(final String animationFile, final int maxFrame, final boolean repeat, String message) {

        binding.nameDesease.setVisibility(View.GONE);
        binding.middlePoint.setVisibility(View.GONE);
        binding.endPoint.setVisibility(View.GONE);
        binding.lottie.setAnimation(animationFile);
        binding.lottie.setVisibility(View.VISIBLE);
        if (message != null) {

            binding.errorMessage.setVisibility(View.VISIBLE);
            binding.errorMessage.setText(message);
        }


        binding.lottie.setMaxFrame(maxFrame);
        if (repeat) {
            binding.lottie.setRepeatMode(LottieDrawable.RESTART);
            binding.lottie.setRepeatCount(LottieDrawable.INFINITE);

        } else binding.lottie.setRepeatCount(0);


        binding.lottie.playAnimation();


    }


    @Override
    public void prepareForData() {

        binding.lottie.cancelAnimation();
        binding.lottie.setVisibility(View.GONE);
        binding.errorMessage.setVisibility(View.GONE);
        binding.endPoint.setVisibility(View.VISIBLE);
        binding.middlePoint.setVisibility(View.VISIBLE);
        binding.nameDesease.setVisibility(View.VISIBLE);


    }


    @Override
    public void onBackPressed() {

        finish();

    }


    private void loadState(String state) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (binding.searchBox.hasFocus()) {

                    binding.searchBox.setText("");
                    binding.searchBox.clearFocus();
                    hideKeyboard();

                }

            }
        });


        StatisticFragment statisticFragment = new StatisticFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", state);
        statisticFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frame, statisticFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        last_state = state;
        showAd();
        RequestedData.numberOfActions++;


    }


    private void setupTopBar() {

        stateSheetFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        CardView bottomsheetlayout = findViewById(R.id.bottom_sheet);


        binding.topMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSheetFragment.callback = new StateAdapter.StateCallback() {
                    @Override
                    public void text(String data) {


                        loadState(data);
                        stateSheetFragment.dismiss();

                    }
                };

                stateSheetFragment.show(getSupportFragmentManager(), "state_sheet_fragment");
            }
        });

        binding.topRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadState(last_state);
            }
        });


        List<String> list = new ArrayList<>();
        list.addAll(RequestedData.statesMap.keySet());
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        binding.searchBox.setAdapter(searchAdapter);
        binding.searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String state = (String) parent.getItemAtPosition(position);

                loadState(state);
            }
        });


    }


    @Override
    public void setChartData(List<DataSet> list) {

        chart.setCenterText(new SpannableString(last_state));
        chart.setMinAngleForSlices(15);
        ArrayList<PieEntry> entries = new ArrayList<>();
        Collections.sort(list);
        for (model.DataSet set : list)
            entries.add(new PieEntry(Float.parseFloat(set.getResult()), set.getTitle()));


        PieDataSet dataSet = new PieDataSet(entries, "Covid-19");
        dataSet.setSliceSpace(1.5f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors


        dataSet.setColors(ColorTemplate.rgb("ffc107"), ColorTemplate.rgb("00e676"), ColorTemplate.rgb("ff1744"));
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.5f);
        dataSet.setValueLineColor(Color.WHITE);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    private void initializeAd() {
        AdRegistration.enableLogging(false);
        AdRegistration.enableTesting(false);
        ad = new InterstitialAd(this);
        ad.setListener(new AdListener());
        AdRegistration.setAppKey(APP_KEY);




    }

    private void showAd() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (RequestedData.numberOfActions % 5 == 0)
                    ad.loadAd();
                if (ad.isReady()) {

                    ad.showAd();

                }

            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.searchBox.getWindowToken(), 0);
    }

    @Override
    public void showInternetError() {

        showMiddleAnimation("broken_cable.json", 100, false, getString(R.string.internet_error_message));
    }

    @Override
    public void showUnknownError() {
        showMiddleAnimation("attention.json", 18, false, getString(R.string.server_error_message));
    }

    @Override
    public void showLoading() {
        showMiddleAnimation("blue_circle_loader.json", 60, true, null);
    }

    private class AdListener extends DefaultAdListener{
        @Override
        public void onAdLoaded(Ad ad, AdProperties adProperties) {
            super.onAdLoaded(ad, adProperties);
        }

        @Override
        public void onAdFailedToLoad(Ad ad, AdError error) {
            super.onAdFailedToLoad(ad, error);
        }

        @Override
        public void onAdDismissed(Ad ad) {
            super.onAdDismissed(ad);
        }
    }

}
