package com.bvinstruments.pandemictracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bvinstruments.pandemictracker.databinding.ActivitySplashBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.RequestedData;
import db.State;
import db.StateDatabase;

public class SplashScreen extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private StateDatabase db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = StateDatabase.getInstance(this);

        //binding.lottie.playAnimation();
        //  loadStatesFromDatabase();
        new LoaderTask().execute();


    }


    private void loadStatesFromDatabase() {
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {

                StateDatabase db = StateDatabase.getInstance(getApplicationContext());
                RequestedData.stateList.add(new State("USA", "USA"));
                RequestedData.stateList.addAll(db.stateDao().getStates());
                for (State s : RequestedData.stateList)
                    RequestedData.statesMap.put(s.getName(), s.getInitials());

            }
        });


        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);


    }


    private class LoaderTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            binding.lottie.playAnimation();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            StateDatabase db = StateDatabase.getInstance(getApplicationContext());
            RequestedData.stateList.add(new State("USA", "USA"));
            RequestedData.stateList.addAll(db.stateDao().getStates());
            for (State s : RequestedData.stateList)
                RequestedData.statesMap.put(s.getName(), s.getInitials());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);


        }
    }


}

