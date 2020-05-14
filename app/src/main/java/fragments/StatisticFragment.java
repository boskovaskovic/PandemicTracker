package fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bvinstruments.pandemictracker.databinding.FragmentStatisticsBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import adapters.DataAdapter;
import data.RequestedData;
import model.DataSet;

public class StatisticFragment extends Fragment {

    public StatisticCallback callback;
    private FragmentStatisticsBinding binding;
    private List<DataSet> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {

        binding = FragmentStatisticsBinding.inflate(inflater);
        setupRecyclerView();
        String key = getArguments().getString("name", "NY");
        if ("USA".equals(key)) loadUSAState();
        else loadData(key);
        return binding.getRoot();


    }

    private void loadData(final String state) {


        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        final String url = "https://covidtracking.com/api/v1/states/" + RequestedData.statesMap.get(state) + "/current.json";


        callback.showLoading();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {


                    JSONObject ob = new JSONObject(response);
                    final List<DataSet> chartList = new ArrayList<>();


                    Iterator<String> keys = RequestedData.dataMapNames.keySet().iterator();


                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = ob.getString(key);
                        if (value != null && value != "null") {
                            String convertedName = RequestedData.dataMapNames.get(key);
                            list.add(new DataSet(convertedName, value));
                            if ("Positive".equals(convertedName) || "Negative".equals(convertedName) || "Death".equals(convertedName))
                                chartList.add(new DataSet(convertedName, value));
                        }


                    }

                    callback.prepareForData();
                    callback.setChartData(chartList);
                    binding.dataList.getAdapter().notifyDataSetChanged();


                } catch (Exception e) {


                    callback.showUnknownError();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isInternetConnectionAvailable()) callback.showInternetError();
                else callback.showUnknownError();

            }
        });

        queue.add(stringRequest);


    }

    private void loadUSAState() {


        RequestQueue queue = Volley.newRequestQueue(getContext());

        final String url = "https://covidtracking.com/api/v1/us/current.json";


        callback.showLoading();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {


                    JSONObject ob = new JSONObject(response.replace("[", "").replace("]", ""));
                    final List<DataSet> chartList = new ArrayList<>();


                    Iterator<String> keys = RequestedData.dataMapNames.keySet().iterator();


                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = ob.getString(key);
                        if (value != null && value != "null") {
                            String convertedName = RequestedData.dataMapNames.get(key);
                            list.add(new DataSet(convertedName, value));
                            if ("Positive".equals(convertedName) || "Negative".equals(convertedName) || "Death".equals(convertedName))
                                chartList.add(new DataSet(convertedName, value));
                        }


                    }

                    callback.prepareForData();
                    callback.setChartData(chartList);
                    binding.dataList.getAdapter().notifyDataSetChanged();


                } catch (Exception e) {


                    callback.showUnknownError();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isInternetConnectionAvailable()) callback.showInternetError();
                else callback.showUnknownError();

            }
        });

        queue.add(stringRequest);


    }

    private void setupRecyclerView() {

        binding.dataList.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(this.getContext(), 6, RecyclerView.VERTICAL, false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            private Random r = new Random();
            private int[] array = new int[]{2, 3};

            @Override
            public int getSpanSize(int position) {

                switch (position % 5) {
                    // first two items span 3 columns each
                    case 0:
                    case 1:
                        return 3;
                    // next 3 items span 2 columns each
                    case 2:
                    case 3:
                    case 4:
                        return 3;
                }

                return 0;

            }
        });

        binding.dataList.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        DividerItemDecoration dividerItemDecorationHorizontal = new DividerItemDecoration(this.getContext(), DividerItemDecoration.HORIZONTAL);
        //  binding.dataList.addItemDecoration(dividerItemDecoration);
        //binding.dataList.addItemDecoration(dividerItemDecorationHorizontal);
        DataAdapter adapter = new DataAdapter(list);
        binding.dataList.setAdapter(adapter);


    }

    private boolean isInternetConnectionAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (StatisticCallback) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface StatisticCallback {

        void prepareForData();

        void showLoading();

        void showInternetError();

        void showUnknownError();

        void setChartData(List<DataSet> list);


    }
}
